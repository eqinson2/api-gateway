package gateway.core.backend;

import com.alibaba.fastjson.JSON;
import com.google.common.net.HostAndPort;
import gateway.util.DefaultHttpsSslContext;
import gateway.util.HttpsSslContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.cert.CertificateException;

/**
 * Created by snow_young on 16/3/24.
 */
public abstract class AbstractServer implements Server{
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    protected String host;
    protected int port;
    // atomic boolean?
    protected volatile boolean down;
    protected int weight;
    protected HttpsSslContext ctx;

    public AbstractServer(){}

    public AbstractServer(String ip, int port, boolean down) {
        this(ip, port, down, 0);
    }

    public AbstractServer(String ip, int port, boolean down, int weight) {
        // number format exception
        this.host = ip;
        this.port = port;
        this.down = down;
        try {
            ctx = DefaultHttpsSslContext.getHttpsSsslCtx();
        } catch (CertificateException |SSLException e) {
            logger.error("default sslcontext initialize error", e);
            ctx = new HttpsSslContext(false, null);
            logger.error("use null sslcontext initialize");
        }
    }

    public HostAndPort getHostAndPort(){
        return  HostAndPort.fromParts(host, Integer.valueOf(port));
    }


    public void setHostAndPorT(String url){
        String[] hostPort = url.split(":");
        host=  hostPort[0];
        port = Integer.valueOf(hostPort[1]);
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public SocketAddress getSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override public boolean equals(Object object) {
        if(object == null){
            return false;
        }
        if(object instanceof Server){
            Server obj = (Server)object;
            return this.host == obj.getHost() && this.port == obj.getPort();
        }
        return false;
    }

    @Override public int hashCode() { return this.getHostAndPort().hashCode();}
}
