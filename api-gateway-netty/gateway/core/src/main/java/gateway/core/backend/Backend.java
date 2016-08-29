package gateway.core.backend;

import java.util.Collection;

import gateway.core.LifeCycle;
import gateway.core.exception.DuplicatAddException;
import gateway.core.exception.NotExistException;

/**
 * Created by snow_young on 16/3/24.
 */
public interface Backend extends java.io.Closeable,LifeCycle{

    void addServer(Server server) throws DuplicatAddException;

    void rmServer(Server server) throws NotExistException;

    void rmServer(String identify) throws NotExistException;

    boolean containsServer(Server server);

    void upsertServer(Server server);

    Server getServer();

    Collection<Server> allServers();

    void setName(String name);

    String getName();
}
