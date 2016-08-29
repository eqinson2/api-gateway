package gateway.core.backend;


import com.google.common.net.HostAndPort;
import gateway.util.HttpsSslContext;

/**
 * Created by snow_young on 16/3/25.
 */
public abstract class AbstractServerSession implements ServerSession{
    protected HttpsSslContext sslCtx;
    protected HostAndPort hostAndPort;
    // this feature for LifeControl
    // private volatile long since = System.currentTimeMillis();

    public String getAddress() { return hostAndPort.getHostText() + " : " + hostAndPort.getPort(); }
}

