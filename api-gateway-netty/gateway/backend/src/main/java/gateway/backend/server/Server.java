package gateway.backend.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.net.HostAndPort;
import gateway.backend.bootstrap.Constants;
import gateway.core.HttpSessionContext;
import gateway.core.LifeCycle;
import gateway.core.Listener;
import gateway.core.backend.AbstractServer;
import gateway.core.backend.ServerSession;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by snow_young on 16/3/23.
 * 可以拆分成运行时属性 ＋ 配置属性
 */
public class Server extends AbstractServer implements gateway.core.backend.Server, LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private int maxSession;
    private int initialSession;
    private ServerSessionPool serverSessionPool;

    // in case the yaml
    // refractor here : the set action is ugly
    public Server(){
    }

    public Server(String ip, int port, boolean down) {
        this(ip, port, down, 0);
    }

    public Server(String ip, int port, boolean down, int weight) {
        this(ip, port, down, weight, Constants.INITIAL_SESSION, Constants.MAX_SESSION);
    }

    public Server(String ip, int port, boolean down, int weight, int initSession, int maxSession) {
        super(ip, port, down, weight);
        this.initialSession = initSession;
        this.maxSession = maxSession;
    }

    public void send(HttpSessionContext ctx, HttpRequest req, Listener listener){
        this.serverSessionPool.send(ctx, req, listener);
    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("ip", host);
        obj.put("port", port);
        obj.put("down", down);
        obj.put("initialSession", initialSession);
        obj.put("maxSession", maxSession);
        obj.put("weight", weight);
       return obj.toString();
    }

    @Override public boolean equals(Object object) {
        if(object == null){
            return false;
        }
        if(object instanceof Server){
            Server obj = (Server)object;
            return this.getHostAndPort().equals(obj.getHostAndPort());
        }
        return false;
    }

    @Override public int hashCode() { return this.getHostAndPort().hashCode();}

    @Override
    public void backSession(ServerSession session) {
        serverSessionPool.returnSession((NettyServerSession)session);
    }

    public void reConnect(){
        this.down = true;
        if(serverSessionPool != null && serverSessionPool.size() != 0){
            serverSessionPool.release();
        }
        HostAndPort identify = HostAndPort.fromParts(this.host, this.port);
        serverSessionPool = new ServerSessionPool(identify, super.ctx, initialSession);
        this.down = false;
    }

    public int getInitialSession(){
        return this.initialSession;
    }

    public void setInitialSession(int initialSession){
        this.initialSession = initialSession;
    }

    public void setMaxSession(int maxSession){
        this.maxSession = maxSession;
    }

    public int getMaxSession(){
        return this.maxSession;
    }

    @Override
    public void start() {
        HostAndPort identify = HostAndPort.fromParts(this.host, this.port);
        this.serverSessionPool = new ServerSessionPool(identify, super.ctx, initialSession);
        this.serverSessionPool.start();
    }

    @Override
    public void close() throws IOException {
        down = true;
        if(serverSessionPool != null) {
            serverSessionPool.shutodwn();
        }
    }

    @Override
    public void shutodwn() {
        try {
            close();
        }catch(IOException e){
            logger.info("close server exception");
        }
    }
}
