package gateway.backend.engine;

import com.alibaba.fastjson.JSONObject;
import gateway.backend.server.Server;
import gateway.backend.server.ServerBackend;
import gateway.backend.service.Service;
import gateway.core.exception.DuplicatAddException;
import gateway.core.exception.NotExistException;
import gateway.engine.zk.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snow_young on 16/4/4.
 */
public class ServerAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ServerAction.class);
    private static final Pattern pattern = Pattern.compile("/gateway/backends/(\\w+)/([^/]+)$");
    @Override
    public boolean Accept(String path) {
        logger.info("path :=>" + path);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()){
            logger.info("backend : " + matcher.group(1) + " ;server : " + matcher.group(2));
            return true;
        }
        return false;
    }

    @Override
    public void Update(String path, String data) {
        logger.info("path update :=> " + path + " data: " + data);
        logger.info("path add :=> " + path + " data: " + data);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()) {
            ServerBackend backend = Service.getInstance().getMux().getBackend(matcher.group(1));
            JSONObject obj = (JSONObject) JSONObject.parse(data);
            Server server = new Server(obj.getString("ip"), obj.getInteger("port"), obj.getBoolean("down"), obj.getInteger("weight"), obj.getInteger("initSession"), obj.getInteger("maxSession"));
            server.start();
            try {
                backend.rmServer(matcher.group(1));
            } catch (NotExistException e) {
                logger.error("rm failure : ", e);
            }
            try {
                backend.addServer(server);
            } catch (DuplicatAddException e) {
                logger.error("add failure : ", e);
            }
        }
    }

    // need concurrent test
    @Override
    public void Remove(String path, String data) {
        logger.info("path remove :=> " + path);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()) {
            ServerBackend backend = Service.getInstance().getMux().getBackend(matcher.group(1));
            try {
                backend.rmServer(matcher.group(1));
            } catch (NotExistException e) {
                logger.error("remove failuer : ", e);
            }
        }
    }

    @Override
    public void Add(String path, String data) {
        logger.info("path add :=> " + path + " data: " + data);
        Matcher matcher = pattern.matcher(path);
        // something strange error if remove the if condition
        String backendName = "";
        if(matcher.find()) {
//            backendName = matcher.group(1);
//            logger.info("backend : " + matcher.group(1) + " ;server : " + matcher.group(2));

//        Matcher matcher = pattern.matcher(path);
            ServerBackend backend = Service.getInstance().getMux().getBackend(matcher.group(1));
            JSONObject obj = (JSONObject) JSONObject.parse(data);
            Server server = new Server(obj.getString("ip"), obj.getInteger("port"), obj.getBoolean("down"), obj.getInteger("weight"), obj.getInteger("initialSession"), obj.getInteger("maxSession"));
            server.start();
            try {
                backend.addServer(server);
            } catch (DuplicatAddException e) {
                logger.error("add failure : ", e);
            }
        }

    }
}

