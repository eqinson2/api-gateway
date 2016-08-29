package gateway.frontend.Listener;


import gateway.core.LifeCycle;
import gateway.util.HttpsSslContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by snow_young on 16/3/27.
 * listen port and call ProxyInitializer(which consist the call chain[end in the ProxyFrontedHandler])
 */
public class Listener implements LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    // default 127.0.0.1:8444
    private String host;
    private int port;
    private HttpsSslContext ctx;;
    private volatile boolean isrunning = true;

    private ChannelFuture cf;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public Listener(){}

    public Listener(String host, int port){
        this(host, port, null);
    }

    public Listener(String host, int port, HttpsSslContext ctx){
        this.host = host;
        this.port = port;
        this.ctx = ctx;
    }


    @Override
    public void start() {
        // 后面放到配置文件当中
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(8);

        try{
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            SslContext serverSslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true).group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ProxyInitializer(ctx)).bind(this.port).sync().channel().closeFuture().sync();
        }catch(Exception e){
            if(isrunning == true)
                logger.error("start proxy Listener failed", e);
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void shutodwn() {
        isrunning = false;
//        don't  understand which design is better
//        bossGroup.shutdownNow();
//        workerGroup.shutdown();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public boolean equals(Object obj){
        if(obj != null && obj instanceof Listener){
            Listener listener = (Listener)obj;
            if(listener.getHost() == this.host && listener.getPort() == this.port)
                return true;
        }
        return false;
    }


    public String getHost(){
        return this.host;
    }

    public void setHost(String host){
        this.host = host;
    }

    public int getPort(){
        return this.port;
    }

    public void setPort(int port){
        this.port = port;
    }
}
