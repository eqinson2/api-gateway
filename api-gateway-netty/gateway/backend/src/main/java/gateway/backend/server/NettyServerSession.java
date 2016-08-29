package gateway.backend.server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.common.net.HostAndPort;
import gateway.core.LifeCycle;
import gateway.core.exception.QuitException;
import gateway.config.ServerConfig;
import gateway.core.HttpSessionContext;
import gateway.core.Listener;
import gateway.core.backend.AbstractServerSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gateway.util.HttpsSslContext;


/**
 * Created by snow_young on 16/3/23.
 * channel has no info about the HostAndIp.
 */
public class NettyServerSession extends AbstractServerSession implements gateway.core.backend.ServerSession, LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(NettyServerSession.class);

    // because the process rate is great, and the switch of multi-threads is heavy
    private static NioEventLoopGroup eventGroup = new NioEventLoopGroup(8);
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final ProxyBackendHandler proxyBackendHandler = new ProxyBackendHandler();
    private Channel serverChannel;

    // kill the NettyServerSession if it exists long, why?
    private volatile long since = System.currentTimeMillis();

    public NettyServerSession(HostAndPort hostAndPort, HttpsSslContext sslCtx){
        this.hostAndPort = hostAndPort;
        this.sslCtx = sslCtx;
    }

    public NettyServerSession(HostAndPort hostAndPort, HttpsSslContext sslCtx, Channel channel){
        this.hostAndPort = hostAndPort;
        this.sslCtx = sslCtx;
        this.serverChannel = channel;
    }

    public void initialConnect(){
        connect(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    NettyServerSession.this.serverChannel = future.channel();
                }
                NettyServerSession.this.countDownLatch.countDown();
            }
        });
        // ensure the channel is created
        waitChannel(3000);
    }

    public void send(HttpSessionContext ctx, HttpRequest req, gateway.core.Listener listener){
        // 抛出异常
        if(serverChannel == null) {
//            send an exception refuseException
//            无意义的操作
//                    IllegalAccessError
//            initialConnect(ctx, req, listener);
            logger.info("Listener channel is null");
        }else{
            proxyBackendHandler.setHttpSessionContext(ctx);
            logger.info("Listener channel send requeset");
            serverChannel.writeAndFlush(req).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    // proxyBackendHandler.setHttpSessionContext(null);
                    // strange : one request, but many response and the first is fail
                    if(channelFuture.isSuccess()){
                        listener.onSuccess(channelFuture);
                    }else{
                        listener.onFail(channelFuture);
                    }
                }
            });
        }
    }

    public void reConnect(HttpSessionContext httpSessionContext, HttpRequest req, Listener listener){
        logger.info("reconnect in NettyServerSession");
        proxyBackendHandler.setHttpSessionContext(httpSessionContext);
        connect(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    NettyServerSession.this.serverChannel = channelFuture.channel();
                    logger.info("async connect success, and now, send request");

                    channelFuture.channel().writeAndFlush(req).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception{
                            if (channelFuture.isSuccess()) {
                                logger.info("recreat conneciton success response");
                                NettyServerSession.this.serverChannel = channelFuture.channel();
                                httpSessionContext.setServerSession(NettyServerSession.this);
                                listener.onSuccess(channelFuture);
//                                listener.onCreate();
                            } else {
                                logger.info("recreate connection fail response");
                                listener.onFail(channelFuture);
                            }
                        }
                    });
                } else {
                    // should be a fail callback
                    logger.error("create Listener session failed, ip: ");
                    // record
                    listener.onFail(channelFuture);
                }
            }
        });
    }

    private void connect(ChannelFutureListener listener){
        Bootstrap b = new Bootstrap();
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
                .group(eventGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline p = socketChannel.pipeline();
//                if (sslCtx != null && sslCtx.isSsl()) {
//                    p.addLast(sslCtx.getSslContext().newHandler(socketChannel.alloc()));
//                }
                p.addLast(new HttpClientCodec(ServerConfig.MAX_INITIALLINE_LENGTH, ServerConfig.MAX_HEADER_SIZE, ServerConfig.MAX_CHUNK_SIZE),
                        new HttpObjectAggregator(ServerConfig.MAX_RESPONSE_SIZE), NettyServerSession.this.proxyBackendHandler);
            }
        });

        ChannelFuture f = b.connect(hostAndPort.getHostText(), hostAndPort.getPort());
        f.addListener(listener);
    }

    private void waitChannel(int loginTimeout) {
        boolean isSuccess = false;
        try {
            isSuccess = this.countDownLatch.await(loginTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("connect error : wait channel interrupted", e);
        }

        if (!isSuccess) {
            throw new QuitException(String.format("Failed to get connection on Listener in %d milliseconds ", loginTimeout));
        }
        if (this.serverChannel == null) {
            throw new QuitException("Failed to connect to Listener at " + hostAndPort.getHostText());
        }
    }

    public Channel getServerChannel(){ return this.serverChannel;}

    public void setHttpSessionContext(HttpSessionContext httpSessionContext) {
        this.proxyBackendHandler.setHttpSessionContext(httpSessionContext);
    }

    @Override public String toString(){ return hostAndPort.toString() + " to Listener session since : " + since;}

    public boolean isOverdue() { return System.currentTimeMillis() - this.since > 60 * 1000;}

    public void resetLife(){
        since = System.currentTimeMillis();
    }

    public String getAddress() { return hostAndPort.getHostText() + " : " + hostAndPort.getPort(); }

    @Override
    public void close() throws IOException {
        // should close channel ?
        if(serverChannel != null && serverChannel.isActive()){
            serverChannel.close();
        }
        serverChannel = null;
    }

    @Override
    public void start() {
        initialConnect();
    }

    @Override
    public void shutodwn() {
        eventGroup.shutdownGracefully();
    }
}
