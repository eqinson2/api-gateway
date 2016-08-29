package gateway.backend.loadralance;

import java.util.List;
import gateway.backend.server.Server;


/**
 * Created by snow_young on 16/3/25.
 */
public interface LoadBalance {

    public Server select(List<Server> servers);
}
