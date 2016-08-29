package gateway.backend.bootstrap;

import gateway.backend.server.ServerBackend;
import gateway.backend.server.Server;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by snow_young on 16/3/26.
 */
public class BoostrapTest {

    private static final Logger logger = LoggerFactory.getLogger(BackendBootstrap.class);

//    @Test
//    public static void testReadConfig(){
//        // 读取配置文件， 实例化 service & router &  serverBackend & Listener
//        String path = "/Users/snow_young/secrect/myopensource/gateway/backend/src/main/resources/servers.yaml";
//        Yaml yaml = new Yaml();
//        ServerBackend serverBackend = null;
//        try {
//            serverBackend = (ServerBackend) yaml.loadAs(new FileReader(path), ServerBackend.class);
//        } catch (Exception e) {
//            logger.info("read config error");
//        }
//
//        try {
//            Service.getInstance().getMux().addBackend("headername", serverBackend);
//        } catch (DuplicatAddException e) {
//            logger.info("install backend fail, ", e);
//        }
//        logger.info("read config : " + serverBackend);
//    }

    @Test
    public void testWriteConfig(){
        String path = "/Users/snow_young/secrect/myopensource/gateway/backend/src/main/resources/servers2.yam";

//        List<Server> servers = new ArrayList<>();
//        servers.add(new Server("127.0.0.1", "9090", false));
//        servers.add(new Server("127.0.0.1", "9050", false));
//        servers.add(new Server("127.0.0.1", "9010", false));
//        ServerBackend serverBackend = new ServerBackend(servers);
//
//        Yaml yaml = new Yaml();
//        try {
//            yaml.dump(serverBackend , new FileWriter(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("something error", e);
//        }
    }




}
