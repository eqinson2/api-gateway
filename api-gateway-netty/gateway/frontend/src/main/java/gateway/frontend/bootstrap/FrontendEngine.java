package gateway.frontend.bootstrap;

import gateway.core.LifeCycle;
import gateway.engine.zk.ZKEngine;
import gateway.frontend.engine.ListenerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by snow_young on 16/4/5.
 */
public class FrontendEngine implements LifeCycle {
    private static final Logger logger = LoggerFactory.getLogger(FrontendEngine.class);

    @Override
    public void start() {
        // add  Listener
        ZKEngine.getInstance().addAction(new ListenerAction());
    }

    @Override
    public void shutodwn() {

    }
}
