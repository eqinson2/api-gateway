package gateway.backend.loadralance;

import gateway.backend.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by snow_young on 16/3/26.
 * in dubbo, roundrobin is base on the statictics of rpc signature(serverkey.methodName)
 * here, simple implementation.
 */
public class RoundRubinLoadBalance extends AbstractLoadBalance implements LoadBalance{
    private static final Logger logger = LoggerFactory.getLogger(RoundRubinLoadBalance.class);

    private AtomicInteger count = new AtomicInteger(0);
    /**
     * dynamic weight according to the
     * @param servers
     * @return
     */
    @Override
    public Server doSelect(List<Server> servers) {
        int value = count.incrementAndGet() % servers.size();
        count.set(value);
        logger.info("doselect size : " + servers.size() + "value : " + value);
        return servers.get(value);
    }
}
