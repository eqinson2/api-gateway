package handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Created by snow_young on 16/3/24.
 */
public class JsonRequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(JsonRequestHandler.class);

    public JsonRequestHandler(){
        logger.debug("开始饿了");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("收到信息");
        FullHttpRequest request = (FullHttpRequest) msg;

        JSONObject jobj = new JSONObject();
        jobj.put("SID", "bvqbruvotqciansf");
        jobj.put("UID", "ioqwrixkfksafjxo");


        String backToClient = jobj.toJSONString();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(backToClient.getBytes()));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/plain");
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,backToClient.length());
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        ChannelFuture future = ctx.writeAndFlush(response);
        if (!HttpHeaders.isKeepAlive(request)) {
            future.sync();
            ctx.close();
        }
        logger.debug("输出信息");
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
