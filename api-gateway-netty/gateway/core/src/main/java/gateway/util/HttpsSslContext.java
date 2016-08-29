package gateway.util;

import io.netty.handler.ssl.SslContext;

/**
 * Created by snow_young on 16/3/22.
 */
public class HttpsSslContext {
    private boolean ssl;
    private SslContext sslContext;

    public HttpsSslContext(){}

    public HttpsSslContext(boolean ssl, SslContext sslContext){
        this.ssl = ssl;
        this.sslContext = sslContext;
    }

    public boolean isSsl(){
        return ssl;
    }

    public void setSsl(boolean ssl){
        this.ssl = ssl;
    }

    public SslContext getSslContext(){
        return sslContext;
    }

    public void setSslContext(SslContext sslContext){
        this.sslContext = sslContext;
    }
}
