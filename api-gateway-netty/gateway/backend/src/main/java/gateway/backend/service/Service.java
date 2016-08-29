package gateway.backend.service;

import gateway.core.LifeCycle;
import gateway.core.backend.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by snow_young on 16/3/26.
 * faceade pattern :
 *      设计真的很好吗？
 *
 */
public class Service implements LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    private AbstractMux mux = new ServiceMux();

    private Service(){}

    @Override
    public void start() {
        for(Backend backend : mux.getBackends()){
            backend.start();
        }
    }

    @Override
    public void shutodwn() {
        for(Backend backend : mux.getBackends()){
            backend.shutodwn();
        }
    }

    private static class Holder{
        public static Service instance = new Service();
    }

    public static Service getInstance(){
        return Holder.instance;
    }

    public Mux getMux(){
        return this.mux;
    }

    public void formatMux(){
        this.mux.formatBackend();
    }

    public void setMux(ServiceMux mux){
        if(this.mux != null){
            try {
                this.mux.close();
            } catch (IOException e) {
                logger.error("close resource mux occurs error", e);
            }
        }
        this.mux = mux;
    }

}
