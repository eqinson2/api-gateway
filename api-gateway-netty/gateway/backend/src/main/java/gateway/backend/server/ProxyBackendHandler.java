package gateway.backend.server;

import gateway.core.HttpSessionContext;
import gateway.core.SessionState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * Created by snow_young on 16/3/23.
 */
public class ProxyBackendHandler extends ChannelInboundHandlerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(Proxy.class);
    private HttpSessionContext httpSessionContext;

    public ProxyBackendHandler(){

    }

    @Override public void channelActive(ChannelHandlerContext channelHandlerContext){ channelHandlerContext.read();}

    @Override public void channelRead(final ChannelHandlerContext channelHandlerContext, Object msg){
        logger.info("proxyBackendHandler read response, remote : " + msg);
        if(msg == null){
            logger.info("proxyBackendHandler read response, remote : null");
        }
        if(httpSessionContext == null){
            logger.info("httpSession null exception");
        }
        httpSessionContext.setHttpResponse(msg);
//        httpSessionContext.updateState();
        httpSessionContext.setState(SessionState.SEND_RESPONSE);
        httpSessionContext.execute();
    }

    public void setHttpSessionContext(HttpSessionContext httpSessionContext){
        this.httpSessionContext = httpSessionContext;
    }

    @Override public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception{
        logger.info("ServerBackend channel inactive, ip=");
        if(httpSessionContext != null){
            logger.error("conn_server_exception_broken: [{}] ", httpSessionContext.getHttpRequest());
            // record
            httpSessionContext.setState(SessionState.QUIT);
            httpSessionContext.closeClientChannel();
            httpSessionContext.closeServerChnnel();
//            httpSessionContext.execute();
        }else{
            logger.info("httpsession context null");
            // record
        }
        super.channelInactive(channelHandlerContext);
    }

    @Override public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        logger.error("ServerBackend caught exception : ", cause);
        channelHandlerContext.close();
    }
}
