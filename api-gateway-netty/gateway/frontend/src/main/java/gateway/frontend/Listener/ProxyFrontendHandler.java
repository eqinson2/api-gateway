package gateway.frontend.Listener;

import gateway.core.HttpSessionContext;
import gateway.core.SessionState;
import gateway.scheduler.DispatchScheduler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by snow_young on 16/3/23.
 *
 * 路由的过滤应该在这里完成
 * 负载均衡在发送数据的时候完成 ? 负载均衡什么时候 : 要发数据的时候
 * 接入后台的操作
 */
public class ProxyFrontendHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
    private static final Logger logger = LoggerFactory.getLogger(ProxyFrontendHandler.class);
    private HttpSessionContext httpsessionCtx;
    private String hostaddr;

    public ProxyFrontendHandler(){
        logger.info("proxyFrontedHandler here");
        httpsessionCtx = new HttpSessionContext();
    }

    // connection create
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception{
        hostaddr = ((InetSocketAddress)channelHandlerContext.channel().remoteAddress()).getAddress().getHostAddress();
        // record timeline
        // write metrics
        httpsessionCtx.setClienthannel(channelHandlerContext.channel());
    }

    // read data, mind : the connection is keep-alive(means that many read0 in one connection)
    // channelRead0 is called by channelRead, and release fullHttpRequest in the end.
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("from frontend read get, " + fullHttpRequest.getUri() + " " + fullHttpRequest.getMethod().name(
        ));
        // metrics record
        // timeline add
        httpsessionCtx.setClienthannel(channelHandlerContext.channel());
        httpsessionCtx.setHttpRequest(fullHttpRequest);
        httpsessionCtx.setState(SessionState.PRE_FILTER);
        // timeline listener
        // 插入scheduelr

        // reference guranteen
        // netty will destroy reqeust due to the own-refcount mechanism
        ReferenceCountUtil.retain(fullHttpRequest);

        if(!DispatchScheduler.getInstance().enqueue(httpsessionCtx)){
            httpsessionCtx.closeClientChannel();
        }
    }

    @Override public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        logger.error("frontend catch exception", cause);
        channelHandlerContext.close();
    }

    @Override public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception{
        logger.info("fronted channel inactive, hostaddr = " + hostaddr);

        if(httpsessionCtx.getSessionState() == SessionState.SEND_RESPONSE){
            // record
            logger.info("close when state is close");
        }else{
            logger.error("conn_client_exceptin_broken:[{}]", hostaddr);
            // record
            // clear self, problem : so when response from Listener come back, we should check
            httpsessionCtx.setState(SessionState.QUIT);
            httpsessionCtx.execute();
        }
        super.channelInactive(channelHandlerContext);
    }
}
