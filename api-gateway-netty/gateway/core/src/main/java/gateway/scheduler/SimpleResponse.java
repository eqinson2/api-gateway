package gateway.scheduler;

import gateway.core.HttpSessionContext;
import gateway.core.SessionState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by snow_young on 16/3/23.
 */
public class SimpleResponse {

    private static Object FORBIDDEN() {
        String body = "<!DOCTYPE HTML \"-//IETF//DTD HTML 2.0//EN\">\n" + "<html><head>\n" + "<title>Breaker circuit problem</title>\n" + "</head><body>\n"
                + "<h1>Error Fallback error</h1>\n" + "<p>This Filter could not accept what you\n" + "are requested to access the document\n"
                + "requested. </body></html>\n";
        DefaultFullHttpResponse response = responseFor(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS, body);
        HttpHeaders.setDate(response, new Date());
        response.headers().set("Proxy-Authenticate", "Basic realm=\"Restricted Files\"");
        return response;
    }

    private static DefaultFullHttpResponse responseFor(HttpVersion httpVersion, HttpResponseStatus status, String body) {
        byte[] bytes = body.getBytes(Charset.forName("UTF-8"));
        ByteBuf content = Unpooled.copiedBuffer(bytes);
        return responseFor(httpVersion, status, content, bytes.length);
    }

    private static DefaultFullHttpResponse responseFor(HttpVersion httpVersion, HttpResponseStatus status, ByteBuf body, int contentLength) {
        DefaultFullHttpResponse response = body != null ?
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, body) :
                new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        if (body != null) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, contentLength);
            response.headers().set("Content-Type", "text/html; charset=UTF-8");
        }
        return response;
    }

    public static void endResRes(HttpSessionContext httpSessionContext){
        httpSessionContext.setHttpResponse(FORBIDDEN());
        httpSessionContext.setState(SessionState.SEND_RESPONSE);
        httpSessionContext.execute();
    }

}
