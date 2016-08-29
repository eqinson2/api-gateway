package gateway.backend.server;

import com.google.common.net.HostAndPort;
import gateway.core.LifeCycle;
import gateway.core.backend.ServerSession;
import gateway.core.exception.QuitException;
import gateway.core.HttpSessionContext;
import gateway.core.Listener;
import gateway.util.HttpsSslContext;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by snow_young on 16/3/23.
 */
public class ServerSessionPool implements LifeCycle {
    private static final Logger logger = LoggerFactory.getLogger(ServerSessionPool.class);

    private ConcurrentLinkedDeque<NettyServerSession> serverConnections = new ConcurrentLinkedDeque<>();
    private int initSessionSize;
    private HostAndPort hostAndPort;
    private HttpsSslContext sslCtx;

    public ServerSessionPool(){}

    public ServerSessionPool(HostAndPort hostAndPort, HttpsSslContext sslCtx, int initialSize) {
        this.hostAndPort = hostAndPort;
        this.sslCtx = sslCtx;
        this.initSessionSize = initialSize;
    }

    public NettyServerSession getServerSession(HttpSessionContext httpSessionContext) {
        NettyServerSession serverSession = serverConnections.pollFirst();
        if (serverSession != null && serverSession.getServerChannel() != null && serverSession.getServerChannel().isActive() && !serverSession
                .isOverdue()) {
            serverSession.setHttpSessionContext(httpSessionContext);
            return serverSession;
        }

        if (serverSession != null) {
            try {
                serverSession.close();
            }catch(IOException e){
                logger.error("close resource serversession occurs error ", e);
            }
        }

        serverSession = initServerSession();
        serverSession.setHttpSessionContext(httpSessionContext);
        return serverSession;
    }

    // maybe callback
    public void send(HttpSessionContext httpSessionContext, HttpRequest req, Listener listener) {
        // we should check the channel state of httpSessionContext, if have, just send
        logger.info("current Listener session : " + serverConnections.size());
//        logger.info("current Listener session semapjore : " + count.availablePermits());
        NettyServerSession serverSession = serverConnections.pollFirst();
        if (serverSession != null && serverSession.getServerChannel() != null && serverSession.getServerChannel().isActive() && !serverSession
                .isOverdue()) {
            serverSession.setHttpSessionContext(httpSessionContext);
            // why i need to know Listener session? avoid gc?
             httpSessionContext.setServerSession(serverSession);
            logger.info("HttpSessionContext should begin send request");
            if(listener != null) {

//                logger.info("current Listener session semapjore : " + count.availablePermits());

                logger.info("listener is not null");
                serverSession.send(httpSessionContext, req, listener);
            }else{
                logger.error("not support now");
            }
            return;
        }

        if (serverSession != null){
            try {
                serverSession.close();
            }catch(IOException e){
                logger.error("close resource serversessio occurs error ", e);
            }
        }

        // never reuse the channel because proxyBackenHandler  is not a @Sharable handler, so can't be added or removed multiple times.
        // otherwise a exception will be thrown.
        serverSession = new NettyServerSession(hostAndPort, sslCtx);

        logger.info("create serversession here");
        serverSession.setHttpSessionContext(httpSessionContext);
        httpSessionContext.setServerSession(serverSession);
        serverSession.reConnect(httpSessionContext, req, listener);
    }

    // should be refator
    public void returnSession(NettyServerSession serverSession) {
        logger.info("return session : " + serverConnections.size());
        if (serverSession != null) {
            serverSession.setHttpSessionContext(null);
            serverSession.resetLife();
            serverConnections.add(serverSession);
        }
        logger.info("return session : " + serverConnections.size());
    }

    public String getServer() {
        return hostAndPort.getHostText() + ":" + hostAndPort.getPort();
    }

    public int size(){
        return serverConnections.size();
    }

    @Override
    public void start() {
        initSessionPool();
    }

    public synchronized void initSessionPool() {
        if (serverConnections.isEmpty()) {
            for (int i = 0; i < initSessionSize; i++) {
                try {
                    serverConnections.add(initServerSession());
                } catch (QuitException ignore) {
                }
            }
        }
    }

    // for constructor, sync io to ensure session established(a full req-resp channel)
    private NettyServerSession initServerSession() {
        NettyServerSession serverSession = new NettyServerSession(hostAndPort, sslCtx);
        try {
            serverSession.initialConnect();
            return serverSession;
        } catch (QuitException e) {
            logger.error("create Listener session failed", e);
            // record
            if (serverSession.getServerChannel() != null) {
                serverSession.getServerChannel().close();
            }
            return null;
        }
    }

    // should i close channel ? or eventloop do that petentially?
    @Override
    public void shutodwn() {
        for(ServerSession session : serverConnections){
            try {
                session.close();
            } catch (IOException e) {
                logger.error("close session error", e);
            }
        }
        if(serverConnections != null && serverConnections.size() != 0)
            serverConnections.pollFirst().shutodwn();
//        release();
    }

    // some particular senioary to condsider
    public void release() {
        ConcurrentLinkedDeque<NettyServerSession> serverConnectionsDeque = this.serverConnections;
        this.serverConnections = new ConcurrentLinkedDeque<>();
        NettyServerSession serverSession;
        logger.info("serverSessopnPool size : " + serverConnectionsDeque.size());
        while ((serverSession = serverConnectionsDeque.pollFirst()) != null) {
            try {
                serverSession.close();
            } catch (IOException e) {
                logger.error("close session error", e);
            }
        }
    }
}
