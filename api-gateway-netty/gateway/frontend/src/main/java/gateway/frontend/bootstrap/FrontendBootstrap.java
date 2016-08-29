package gateway.frontend.bootstrap;

import gateway.core.LifeCycle;
import gateway.frontend.Listener.Listener;
import gateway.scheduler.DispatchScheduler;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snow_young on 16/3/22.
 */
public class FrontendBootstrap implements LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(FrontendBootstrap.class);
    private static List<Listener> listeners = new ArrayList<>();
    private FrontendEngine engine = new FrontendEngine();

    public static List<Listener> getListeners(){
        return listeners;
    }

    @Override
    public void shutodwn(){
        logger.info("shutdown gateway gracefully");
        // TODO: reactor by spi mechiam
        engine.shutodwn();
        DispatchScheduler.getInstance().shutodwn();

        for(Listener listener : listeners){
            listener.shutodwn();
        }
        logger.info("shutdown gracefully succefully");
    }

    @Override
    public void start(){
        logger.info("prepeare to star gateway");
        // TODO: reactor by spi mechiam
//        init();
        DispatchScheduler.getInstance().start();

        logger.info("prepare to listen the ports");
        for(Listener listener : listeners) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                     listener.start();
                }
            }).start();
        }
        engine.start();

    }


    // 这个也需要从zk engine中拿
    public static void init(){
        // default configuration
        Listener listner = new Listener("127.0.0.1", 8444);
        listeners.add(listner);
    }

}
