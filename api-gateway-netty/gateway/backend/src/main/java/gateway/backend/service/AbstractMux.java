package gateway.backend.service;

import gateway.backend.server.ServerBackend;
import gateway.core.backend.Backend;
import gateway.core.exception.DuplicatAddException;
import gateway.core.exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by snow_young on 16/3/26.
 */
public abstract class AbstractMux implements Mux{
    private static final Logger logger = LoggerFactory.getLogger(AbstractMux.class);
    protected Map<Pattern, ServerBackend> backendMap = new ConcurrentHashMap<>();

    public AbstractMux(){

    }

    /**
     * refer to  the difference of putifabsent and put
     * @param path
     * @param serverBackend
     * @throws DuplicatAddException
     */
    @Override
    public synchronized void addBackend(String path, ServerBackend serverBackend) throws DuplicatAddException{
        Pattern pattern = Pattern.compile(path);
        if(backendMap.containsKey(pattern)){
            // 抛出异常
            throw new DuplicatAddException("regex for " + path);
        }else{
            backendMap.put(pattern, serverBackend);
        }
        logger.info("add serverBackend : " + path + " regex : " + pattern);
    }

    @Override
    public synchronized void rmBackend(String path) throws NotExistException{
        Pattern pattern = Pattern.compile(path);
        if(!backendMap.containsKey(pattern)){
            throw new NotExistException("regex for " + path);
        }else{
            backendMap.remove(pattern);
        }
    }

    @Override
    public void formatBackend(){
        this.backendMap.clear();
    }

    public void setBackendMap(Map<String, ServerBackend> map){
        if(map != null && map.size() >= 1){
            for(ServerBackend backend : backendMap.values()){
                try {
                    backend.close();
                }catch(IOException e){
                    logger.error("close backend occurs error", e);
                }
            }
        }
        for(Map.Entry<String, ServerBackend> entry : map.entrySet()){
            backendMap.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
    }

    public Map<String, ServerBackend> getBackendMap(){
        Map<String, ServerBackend> out = new HashMap<>();
        for(Map.Entry<Pattern, ServerBackend> entry : backendMap.entrySet()){
            out.put(entry.getKey().toString(), entry.getValue());
        }
        return out;
    }

    public Collection<ServerBackend> getBackends(){
        return  backendMap.values() ;
    }
}
