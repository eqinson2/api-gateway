package gateway.backend.bootstrap;

import gateway.backend.engine.BackendAction;
import gateway.backend.engine.ServerAction;
import gateway.core.LifeCycle;
import gateway.engine.zk.ZKEngine;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 * Created by snow_young on 16/4/4.
 */
public class BackendEngine implements LifeCycle {
    private static final Logger logger = LoggerFactory.getLogger(BackendEngine.class);


    @Override
    public void start() {
        // add serverChange Listener
        ZKEngine.getInstance().addAction(new ServerAction());
        // add backend listener
        ZKEngine.getInstance().addAction(new BackendAction());
    }

    @Override
    public void shutodwn() {

    }

}
