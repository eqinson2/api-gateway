package gateway.core;

import gateway.core.backend.Server;
import gateway.core.backend.Backend;
import gateway.core.backend.ServerSession;
import io.netty.channel.*;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by snow_young on 16/3/23.
 *
 * 选择的差异性 ：
 *   serverChannel 添加 middleware, 锁的释放也放在middleware里面，这样，后台已有响应，经过middleware的时候
 *   就释放掉资源锁， 一定程度上会导致qps上涨（是否可行？）
 */
public class HttpSessionContext {
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionContext.class);

    private Channel clientChannel;
    private ServerSession serverSession;

    private HttpRequest httpRequest;
    private Object httpResponse;
    private volatile SessionState sessionState = SessionState.PRE_FILTER;
    private List<CloseListener> onclose = new ArrayList<>();

    public void setServerSession(ServerSession serverSession){ this.serverSession = serverSession; }

    public ServerSession getServerSession() {return this.serverSession;}

    public void setClienthannel(Channel channel){
        this.clientChannel = channel;
    }

    public HttpRequest getHttpRequest(){
        return this.httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public Object getHttpResponse(){
        return this.httpResponse;
    }

    public void setHttpResponse(Object httpResponse){
        this.httpResponse = httpResponse;
    }


    private ChannelHandlerContext clientToProxyCtx;
    public ChannelHandlerContext getClientToProxyCtx() {
        return clientToProxyCtx;
    }
    public void setClientToProxyCtx(ChannelHandlerContext clientToProxyCtx) {
        this.clientToProxyCtx = clientToProxyCtx;
    }

    public void updateState(){
        logger.info("sessionState : " + sessionState);
        switch (sessionState) {
            case PRE_FILTER:
                sessionState = SessionState.SEND_REQUEST;
                break;
            case SEND_REQUEST:
//                sessionState = SessionState.POST_FILTER;
                sessionState = SessionState.SEND_RESPONSE;
                break;
            case SEND_RESPONSE:
                sessionState = SessionState.POST_FILTER;
                break;
            case POST_FILTER:
                sessionState = SessionState.SEND_RESPONSE;
                break;
            case QUIT:
//                sessionState = SessionState.SEND_RESPONSE;
                doQuit();
                break;
        }
    }

    private Backend backend = null;
    private Server server;

    public void setBackend(Backend backend){
        this.backend = backend;
    }



    // 为什么需要同步 ? 避免代码重排序
    public synchronized void execute(){
        try{
            switch (sessionState){
                case PRE_FILTER:
//                    sessionState = SessionState.SEND_REQUEST;
                    logger.info("pre filter");
                    updateState();
                    execute();
                    break;
                case SEND_REQUEST:
                    // BIG BUG
                    logger.info("send request");
                    flushServerRequest();
                    break;
                case SEND_RESPONSE:
                    // return session
                    logger.info("send response ");
                    closeServerChnnel();
                    // session back callback! remmember to do
                    flushClientResponse();
                    break;
                case POST_FILTER:
                    logger.info("post filter");
                    updateState();
                    execute();
                    break;
                case QUIT:
//                    doQuit();
                    break;
            }

        }catch (Throwable e){
            logger.error("execute failed", e);
            doQuit();
        }
    }

    /**
     * not recommend
     * @param sessionState
     */
    public void setState(SessionState sessionState){
        this.sessionState = sessionState;
    }

    public SessionState getSessionState(){
        return this.sessionState;
    }

    // 是否需要添加监听器? 重载一个版本
    public void flushClientResponse(){
        if(clientChannel == null && clientChannel.isActive()){
            logger.info("channel not exits");
        }else{
            logger.info("channel exits");
        }
        clientChannel.writeAndFlush(httpResponse).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    // record
                    logger.error("fluash to client failed, " + getChannelInfo());
                    logger.error("error info : ", channelFuture.cause());
                }else{
                    // record
                     logger.info("flush to client success, " + getChannelInfo());
                }
                doQuit();
                // return session
            }
        });

        logger.info("wait response to client end");
    }

    // something strange
    private void flushServerRequest(){
        httpRequest.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        server =  backend.getServer();
        server.send(HttpSessionContext.this, httpRequest, new Listener() {
            @Override
            public void onSuccess(Object obj) {
                logger.info("proxy write to Listener success, " + getChannelInfo());
            }

            @Override
            public void onFail(Object obj) {
                // record
                logger.error("proxy write to Listener failed, " + getChannelInfo());
                logger.error("current state : " + sessionState);
                // here something wrong
                HttpSessionContext.this.doQuit();
            }

            // will be removed
            @Override
            public void onCreate() {
            }
        });
    }

    // 资源释放的结构需要调整
    private synchronized void doQuit(){
        clearChannel();
    }

    // close client channel
    // foreach 有一个并发修改异常
    private void clearChannel(){
        logger.info("clear channel");
        closeClientChannel();
        closeServerChnnel();
        onclose.forEach(closelistener -> {
            closelistener.onActive();
        });
        onclose.clear();
    }

//    private Semaphore sem;
//    private AtomicReference<Semaphore> sem = new AtomicReference<>();
//    public void setSemaphore(Semaphore sem){
//        this.sem.set(sem);
//    }

    public void closeClientChannel(){
        try{
//            this.sem.get().release();
            if(clientChannel != null){
                clientChannel.close();
            }
        }catch(Exception e){
            logger.error("close client channel failed, channel info : "+ getChannelInfo(), e);
        }
    }

    public void closeServerChnnel(){
        if(serverSession != null) {
            logger.info("serverSession : " + serverSession.toString());
            server.backSession(serverSession);
            serverSession = null;
        }
    }

    public void addCloseCallback(CloseListener listener){
        onclose.add(listener);
    }

    public String getChannelInfo(){
        return "clientChannel info && serverChannel info";
    }

    // for close callback
    public static interface CloseListener{
        void onActive();
    }
}
