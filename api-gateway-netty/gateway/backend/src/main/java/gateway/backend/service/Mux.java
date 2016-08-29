package gateway.backend.service;

import java.io.Closeable;
import java.util.Collection;

import gateway.backend.server.ServerBackend;
import gateway.core.exception.DuplicatAddException;
import gateway.core.exception.NotExistException;
import gateway.core.exception.NotFoundException;


/**
 * Created by snow_young on 16/3/26.
 */
public interface Mux extends Closeable{
    ServerBackend dispatcher(String path) throws NotFoundException;
    void addBackend(String path, ServerBackend serverBackend) throws DuplicatAddException;
    void rmBackend(String path) throws NotExistException;
    ServerBackend getBackend(String path);
    void upsertBackend(String path, ServerBackend serverBackend);
    void updateBackendPath(String name,String pattern);
    void formatBackend();
    Collection<ServerBackend> getBackends();
}
