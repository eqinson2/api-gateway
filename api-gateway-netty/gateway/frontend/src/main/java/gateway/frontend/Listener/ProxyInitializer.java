package gateway.frontend.Listener;

import java.net.InetSocketAddress;

import gateway.util.HttpsSslContext;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gateway.config.ServerConfig;


/**
 * Created by snow_young on 16/3/22.
 * consit process chain, send to ProxyFrontendHandler finally.
 */
public class ProxyInitializer extends ChannelInitializer<SocketChannel>{
    private static final Logger logger = LoggerFactory.getLogger(ProxyInitializer.class);
    private HttpsSslContext serverSslCtx;

    public ProxyInitializer(HttpsSslContext serverSslCtx){
        this.serverSslCtx = serverSslCtx;
    }

    /**
     * initailize each connection
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        if(serverSslCtx != null && serverSslCtx.isSsl()){
            // socketChannel.alloc
            pipeline.addLast(serverSslCtx.getSslContext().newHandler(socketChannel.alloc()));
        }
        pipeline.addLast(new IdleStateHandler(30 * 60, 0 , 0), new ClientHeartBeatHandler(),
                new HttpServerCodec(ServerConfig.MAX_INITIALLINE_LENGTH, ServerConfig.MAX_HEADER_SIZE, ServerConfig.MAX_CHUNK_SIZE ),
                new HttpObjectAggregator(ServerConfig.MAX_REQUEST_SIZE), new ProxyFrontendHandler());
    }

    private static String getAddress(ChannelHandlerContext ctx){
        try{
            return ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        }catch(Exception e){
            return "null";
        }
    }

    /**
     * ChannelDuplexHandler
     * IDELE state timeout action program
     */
    private static class ClientHeartBeatHandler extends ChannelDuplexHandler{
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
            if(evt instanceof IdleStateEvent){
                logger.warn("client socket idle, close it, client address is: " + getAddress(ctx));
                ctx.close();
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}


