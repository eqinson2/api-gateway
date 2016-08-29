package bootstrap;

import gateway.core.LifeCycle;
import gateway.engine.zk.ZKEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by snow_young on 16/3/22.
 * spi :
 *    ServerBootstrap
 */
public class Bootstrap {
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    private static LifeCycle backendBootstrap;
    private static LifeCycle frontendBootstrap;

    public static void main(String[] args){
        init();
        start();
    }

    public static void shutdown(){
        logger.info("shutdown gateway gracefully");
        // TODO: reactor by spi mechiam
        ZKEngine.getInstance().shutodwn();
//        DispatchScheduler.getInstance().shutodwn();
        // how to deal dependncy quesin
        // which is better
        backendBootstrap.shutodwn();
        frontendBootstrap.shutodwn();
//        for(gateway.frontend.Listener.Listener listener : listeners){
//            listener.shutodwn();
//        }
        logger.info("shutdown gracefully succefully");
    }

    public static void start(){
        logger.info("prepeare to star gateway");
        // TODO: reactor by spi mechiam
        try {
            backendBootstrap.start();
        } catch (Exception e) {
            logger.error("backendbootstrap start failed", e);
            throw new RuntimeException();
        }
        ZKEngine.getInstance().start();
        try {
            frontendBootstrap.start();
        } catch (Exception e) {
            logger.error("frontendbootstrap start failed", e);
            throw new RuntimeException();
        }
    }

    // react by spi
    public static void init(){
        try {
            // due to module isolation, the implemets is unfriend, should mv to others
            backendBootstrap = (LifeCycle) Class.forName("gateway.backend.bootstrap.BackendBootstrap").newInstance();
//            Class backend = Class.forName("gateway.backend.bootstrap.BackendBootstrap").getClass();
//            backendBootstrap = (LifeCycle) backend.getMethod("getInstance", null);
        } catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
            logger.error("backend bootstrap initialize failed", e);
            throw new RuntimeException();
        }
        try {
            frontendBootstrap = (LifeCycle) Class.forName("gateway.frontend.bootstrap.FrontendBootstrap").newInstance();
        } catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
            logger.error("frontend bootstrap initialize failed", e);
            throw new RuntimeException();
        }
    }
}
