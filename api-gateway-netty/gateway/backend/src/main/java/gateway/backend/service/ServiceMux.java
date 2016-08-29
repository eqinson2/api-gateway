package gateway.backend.service;


import gateway.backend.server.ServerBackend;
import gateway.core.backend.Backend;
import gateway.core.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by snow_young on 16/3/26.
 */
public class ServiceMux extends AbstractMux implements Mux{
    private static final Logger logger = LoggerFactory.getLogger(ServiceMux.class);

    public ServiceMux(){}

    // support for engine
    // sould add a sync
    // question : what would iter do when change a entry in the backend
    @Override
    public void updateBackendPath(String name, String pattern){
        ServerBackend backend = null;
        Pattern key = null;
        for(Map.Entry<Pattern, ServerBackend> entry: backendMap.entrySet()){
            if(entry.getValue().getName().equals(name)){
                // dangerous operation
                backend = entry.getValue();
                key = entry.getKey();
                break;
            }
        }
        if(backend != null){
            backendMap.remove(key);
        }
        backendMap.put(Pattern.compile(pattern), backend);
    }

    /**
     * add notfound loadralance
     * @param path
     * @return
     * @throws NotFoundException
     */
    @Override
    public ServerBackend dispatcher(String path) throws NotFoundException{
        for(Map.Entry<Pattern, ServerBackend> entry : backendMap.entrySet()){
            if(entry.getKey().matcher(path).matches()){
               return entry.getValue();
            }
        }
        throw new NotFoundException(path);
    }

    @Override
    public ServerBackend getBackend(String name) {
        for(Map.Entry<Pattern, ServerBackend> entry: backendMap.entrySet()){
            if(entry.getValue().getName().equals(name)){
                // dangerous operation
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void upsertBackend(String path, ServerBackend serverBackend) {

    }

    @Override
    public void close() throws IOException {
        for(Backend backend : backendMap.values()){
            backend.close();
        }
    }
}
