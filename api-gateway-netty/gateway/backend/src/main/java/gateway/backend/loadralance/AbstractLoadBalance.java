package gateway.backend.loadralance;

import java.util.List;
import gateway.backend.server.Server;


/**
 * Created by snow_young on 16/3/25.
 */
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public Server select(List<Server> servers) {
        if(servers == null || servers.size() == 0)
            return null;
        if(servers.size() == 1)
            return servers.get(0);
        return doSelect(servers);
    }

    public abstract Server doSelect(List<Server> servers);
}
