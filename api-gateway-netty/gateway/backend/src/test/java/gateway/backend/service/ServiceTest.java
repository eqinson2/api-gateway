package gateway.backend.service;

import gateway.backend.bootstrap.BackendBootstrap;

/**
 * Created by snow_young on 16/3/26.
 */
public class ServiceTest {
    //        test for 1
//        List<Server> servers = new ArrayList<>();
//        servers.add(new Server("127.0.0.1", "9090", false));
//        servers.add(new Server("127.0.0.1", "9050", false));
//        servers.add(new Server("127.0.0.1", "9010", false));
//        ServerBackend serverBackend = new ServerBackend(servers)
//
// ;
//        test for 2
//        ServerBackend serverBackend = new ServerBackend();
//        try {
//            serverBackend.addServer(new Server("127.0.0.1", "9090", false));
//            serverBackend.addServer(new Server("127.0.0.1", "9050", false));
//            serverBackend.addServer(new Server("127.0.0.1", "9010", false));
//        }catch(DuplicatAddException e){
//
//        }



    //        Listener test
//        logger.info("write config ");
//        Server Listener = new Server("127.0.0.1", "9090", false);
//        String server_str = yaml.dump(Listener);
//        logger.info("szerver_Str : " + server_str);
//        Server server2 = (Server)yaml.load(server_str);
//        logger.info("Listener : " + Listener.toString());



    //      serverBackend test
//        logger.info("write config serverbackend");
//        String serverbackend_str = yaml.dump(serverBackend);
//        logger.info("szerverbackend_Str : " + serverbackend_str);
//        ServerBackend backend2 = (ServerBackend)yaml.load(serverbackend_str);
//        logger.info("serverbackend : " + backend2.toString());


    //        ServerBackend serverBackend = (ServerBackend) yaml.loadAs(fis, ServerBackend.class);
//        ServerBackend serverBackend = null;
//        try {
//            serverBackend = (ServerBackend)yaml.load(new FileReader(path));
//            logger.info("Listener info : " + serverBackend.toString());
//        } catch (Exception e) {
//            logger.info("read config error");
//            return;
//        }
//
//        try {
//            Service.getInstance().getMux().addBackend("headername", serverBackend);
//        } catch (DuplicatAddException e) {
//            logger.info("install backend fail, ", e);
//        }
//        logger.info("read config : " + serverBackend);



//    try {
//        (new BackendBootstrap()).start();
//    } catch (Exception e) {
//        logger.error("backendbootstrap start failed", e);
//    }
//        try {
//            if (Service.getInstance().getMux().dispatcher("headername") == null) {
//                logger.info("error headername");
//            }
//            logger.info("finish here");
//        }catch(NotFoundException e){
//            logger.info("not found exception");
//        }

}
