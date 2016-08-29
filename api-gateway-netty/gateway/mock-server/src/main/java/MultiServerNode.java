import handler.JsonRequestHandler;
import handler.RequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.List;

/**
 * Created by snow_young on 16/3/26.
 */
public class MultiServerNode {

    public MultiServerNode(){

    }

    public void binds(List<String> ports) throws Exception {
        for(String port : ports){
            bind(Integer.valueOf(port));
        }
    }


    public void bind(int port) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 30 : value setting according to the machine. otehrwise
        // error increase because the switch cost of thread context
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        JsonRequestHandler handler = new JsonRequestHandler();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChInitializer(
                            (ch) ->{
                                ch.pipeline().addLast(new HttpServerCodec());
                                ch.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                                ch.pipeline().addLast(new RequestHandler());
                            }
                    ));
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public static void main(String args[]) throws Exception{
        String[] ports_str = new String[]{"9090", "9095", "9100"};
        List<String> ports = Arrays.asList(ports_str);
        new MultiServerNode().binds(ports);
    }




    private class ChInitializer extends ChannelInitializer<Channel> {
        private final IChInitializer initializer;

        protected void initChannel(Channel ch) throws Exception {
            this.initializer.initChannel(ch);
        }

        @ConstructorProperties({"initializer"})
        public ChInitializer(IChInitializer initializer) {
            this.initializer = initializer;
        }
    }

    public interface IChInitializer {
        void initChannel(Channel var1) throws Exception;
    }
}
