package handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;


/**
 * Created by snow_young on 16/3/24.
 */
public class RequestHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        String []uriParts = request.getUri().split("/");
        String method = uriParts[1];
        int size = Integer.valueOf(uriParts.length > 2 ? uriParts[2] : "0");
        String result = "1234dfax";

        ByteBuf requestContent = request.content();

        //print request content
        byte[] content = new byte[requestContent.readableBytes()];
        requestContent.readBytes(content);
        requestContent.release();
        String requestToPrint = "[request content]: " + new String(content, "UTF-8");
        System.out.println(requestToPrint);

        //return Listener response
        String resultFromServer = "[result from Listener]: " + result;
        System.out.println(resultFromServer);

        String backToClient = requestToPrint + "\n" + resultFromServer;
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,Unpooled.copiedBuffer(backToClient.getBytes()));
        response.headers().set(Names.CONTENT_TYPE,"text/plain");
        response.headers().set(Names.CONTENT_LENGTH,backToClient.length());
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, Values.KEEP_ALIVE);
        }

        ChannelFuture future = ctx.writeAndFlush(response);
        if (!HttpHeaders.isKeepAlive(request)) {
            future.sync();
            ctx.close();
        }
        // need here, otherwise the response will error
        request.release();
        //ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
