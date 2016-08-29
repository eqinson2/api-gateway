package gateway.backend.bootstrap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gateway.backend.server.ServerBackend;
import gateway.backend.server.Server;
import gateway.backend.service.Service;
import gateway.backend.service.ServiceMux;
import gateway.core.Dispatcher;
import gateway.core.LifeCycle;
import gateway.core.backend.Backend;
import gateway.core.exception.DuplicatAddException;
import gateway.scheduler.DispatchScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by snow_young on 16/3/26.
 */
public class BackendBootstrap implements LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(BackendBootstrap.class);

    private BackendEngine engine;

//    private static final Yaml yaml = new Yaml();


    // 注册到 bootstrap bus
    // 或者通过spi机制实现
    public void start(){
//        check the legal of file
//        try {
//            readConfig(Constants.BOOTSTRAP_FILE);
//        }catch(Exception e){
//            logger.error("start fail", e);
//            throw new RuntimeException();
//        }
        Service.getInstance().setMux(new ServiceMux());

        try {
            Service.getInstance().start();
        }catch(Exception e){
            logger.error("start fail", e);
            throw new RuntimeException();
        }

        DispatchScheduler.getInstance().setDispatcher(new ServerDispatcher());

        engine = new BackendEngine();
        engine.start();
    }

    @Override
    public void shutodwn() {
        engine.shutodwn();
        Service.getInstance().shutodwn();
    }

//    // read from file : incoked by bootstrap registry
//    // ? accept(file) 检查文件是否符合文件！ 默认backend.*
//    // ? 读取配置, 实例化后台的类
//    public static void readConfig(String path) throws Exception{
////        writeConfig(path);
//        // 读取配置文件， 实例化 service & router &  serverBackend & Listener
//        ServiceMux serviceMux = null;
//        serviceMux = (ServiceMux)yaml.load(new FileReader(path));
//        logger.info("serviceMux : " + serviceMux.toString());
//        Service.getInstance().setMux(serviceMux);
//    }
//
//    public static void writeConfig(String path) {
//        List<Server> servers = new ArrayList<>();
//        servers.add(new Server("127.0.0.1", 9090, false));
//        servers.add(new Server("127.0.0.1", 9050, false));
//        servers.add(new Server("127.0.0.1", 9010, false));
//        ServerBackend serverBackend = new ServerBackend("", servers);
//
//        List<Server> servers2 = new ArrayList<>();
//        servers2.add(new Server("127.0.0.1", 9090, false));
//        servers2.add(new Server("127.0.0.1", 9050, false));
//        servers2.add(new Server("127.0.0.1", 9010, false));
//        ServerBackend serverBackend2 = new ServerBackend("",servers2);
//
//        try {
//            Service.getInstance().getMux().addBackend("headername", serverBackend);
//            Service.getInstance().getMux().addBackend("tailername", serverBackend2);
//        } catch (DuplicatAddException e) {
//            logger.info("add duplicate");
//        }
//
//        logger.info("write config serverbackend");
//        ServiceMux mux = (ServiceMux) Service.getInstance().getMux();
//        String servicemux_str = yaml.dump(mux);
//        logger.info("servicemux_str : " + servicemux_str);
//        ServiceMux serviceMux = (ServiceMux)yaml.load(servicemux_str);
//        logger.info("servicemux : " + serviceMux.toString());
//
//        try {
//            yaml.dump(serviceMux, new FileWriter(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("something error", e);
//        }
//    }
//
//    public static void main(String[] args){
//        List<Server> servers = new ArrayList<>();
//        servers.add(new Server("127.0.0.1", 9090, false));
//        servers.add(new Server("127.0.0.1", 9050, false));
//        servers.add(new Server("127.0.0.1", 9010, false));
//        ServerBackend serverBackend = new ServerBackend("", servers);
//        logger.info("server to json : " + new Server("127.0.0.1", 9090, false).toString());
//        logger.info("serverBackend to json : " + serverBackend.toString());
//        String str = new Server("127.0.0.1", 9090, false).toString();
////        Server server = (Server)JSONObject.parseObject(str, Server.class);
////        logger.info("weight", server.getWeight());
//        JSONObject json = JSONObject.parseObject(str);
////        这样就可以实例化了
//        logger.info("weight : " + json.get("weight"));
//    }
}
