package gateway.util;

import gateway.core.HttpSessionContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;


import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * Created by snow_young on 16/3/24.
 */
public class DefaultHttpsSslContext {

    public DefaultHttpsSslContext() {
    }

    public static HttpsSslContext getHttpsSsslCtx() throws CertificateException, SSLException {
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SslContext serverSslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        return new HttpsSslContext(false, serverSslCtx);
    }
}
