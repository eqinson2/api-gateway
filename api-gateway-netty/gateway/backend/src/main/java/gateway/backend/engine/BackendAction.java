package gateway.backend.engine;

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

public class BackendAction implements Action {
    private static final Logger logger = LoggerFactory.getLogger(BackendAction.class);
    private static Pattern pattern = Pattern.compile("/gateway/backends/(\\w+)$");

    @Override
    public boolean Accept(String path) {
        logger.info("path :=>" + path);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()){
            logger.info("backend : " + matcher.group(1));
            return true;
        }
        return false;
    }

    @Override
    public void Update(String path, String data) {
        logger.info("update backend path : " + path + " ;path : " + path);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()) {
            String name = matcher.group();
            Service.getInstance().getMux().updateBackendPath(name, data);
        }
    }

    @Override
    public void Remove(String path, String data) {
        logger.info("backend path : " + path);
        try {
            Service.getInstance().getMux().rmBackend(data);
        } catch (NotExistException e) {
            logger.error("path");
        }
    }

    @Override
    public void Add(String path, String data) {
        logger.info("backend add " + path );
        logger.info("path :=>" + path + "; data: " + data);
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()) {
            logger.info("find backend here");
            ServerBackend backned = new ServerBackend(matcher.group(1));
            try {
                Service.getInstance().getMux().addBackend(data, backned);
            } catch (DuplicatAddException e) {
                logger.error("add duplicate : " + path, e);
            }
        }

    }
}
