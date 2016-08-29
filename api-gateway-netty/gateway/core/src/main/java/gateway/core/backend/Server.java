package gateway.core.backend;

import gateway.core.HttpSessionContext;
import gateway.core.Listener;
import io.netty.handler.codec.http.HttpRequest;
import java.net.SocketAddress;

/**
 * Created by snow_young on 16/3/24.
 */
public interface Server extends java.io.Closeable{

    // for future
    void send(HttpSessionContext ctx, HttpRequest req, Listener listener);

    String getHost();

    void setHost(String host);

    int getPort();

    void setPort(int port);

    boolean isDown();

    void setDown(boolean down);

    int getWeight();

    void setWeight(int weight);

    void backSession(ServerSession session);

    SocketAddress getSocketAddress();
}
