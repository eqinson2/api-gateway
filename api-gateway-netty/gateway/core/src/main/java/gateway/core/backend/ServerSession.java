package gateway.core.backend;

import gateway.core.HttpSessionContext;
import gateway.core.Listener;
import io.netty.handler.codec.http.HttpRequest;


/**
 * Created by snow_young on 16/3/25.
 */
public interface ServerSession extends java.io.Closeable{

    void initialConnect();

    void send(HttpSessionContext ctx, HttpRequest req, gateway.core.Listener listener);

    void reConnect(HttpSessionContext httpSessionContext, HttpRequest req, Listener listener);

//    public boolean isOverdue() { return System.currentTimeMillis() - this.since > 60 * 1000;}

//    public void resetLife(){
//        since = System.currentTimeMillis();
//    }
}
