package gateway.engine.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

//import common.toolkit.java.util.ObjectUtil;
/**
 * Created by snow_young on 16/4/3.
 */
public class TestServerEngine {
    private static final int SESSION_TIMEOUT = 1000;
    private static final String CONNECTION_STRING = "localhost:2181";
    private static final String ZK_PATH = "/javaexample";

    public static void main(String[] args){
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        final Logger loggerx = LoggerFactory.getLogger(TestServerEngine.class);

        WatcherExample example = new WatcherExample();
        example.creatConnection(CONNECTION_STRING, SESSION_TIMEOUT);
        if(example.createPath(ZK_PATH, "徐建海")){
            loggerx.info("read data : " + example.getData(ZK_PATH));
            example.writeData(ZK_PATH, "updated 徐建海");
            loggerx.info("read data again: " + example.getData(ZK_PATH));
            example.deleteData(ZK_PATH);
        }

        try{
            Thread.sleep(5000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }


    }

    private static class WatcherExample implements Watcher{
        private static final Logger logger = LoggerFactory.getLogger(TestServerEngine.class);
        private ZooKeeper zk = null;

        public void creatConnection(String connectionString, int sessiontTimeout){
            this.releaseConnection();
            try{
                zk = new ZooKeeper(connectionString, sessiontTimeout, this);
            }catch(IOException e){
                logger.error("zk create connection error", e);
                e.printStackTrace();
            }
        }

        public void releaseConnection(){
            if (zk!=null && zk.getState()!= ZooKeeper.States.CLOSED){
                try{
                    zk.close();
                }catch (InterruptedException e){
                    logger.error("close zk failed", e);
                    e.printStackTrace();
                }
            }
        }

        public boolean createPath(String path, String data){
            try{
                this.zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                return true;
            }catch (KeeperException|InterruptedException e){
                logger.info("create path failed : " + path, e);
            }
            return false;
        }

        public String getData(String path){
            try{
                return new String(this.zk.getData(path, false, null));
            }catch(KeeperException | InterruptedException e){
                logger.error("get data failed", e);
            }
            return null;
        }

        public boolean writeData(String path, String data){
            try{
                this.zk.setData(path, data.getBytes(), -1);
                return true;
            }catch(InterruptedException| KeeperException ex){
                logger.error("write data failed", ex);
            }
            return false;
        }

        public void deleteData(String path){
            try{
                this.zk.delete(path, -1);
            }catch(KeeperException | InterruptedException ex){
                logger.error("delete path failed ", ex);
            }
        }

        @Override
        public void process(WatchedEvent event) {
            logger.info("change from receive nodification  :" + event.getState());
            logger.info("change from path : " + event.getPath());
            logger.info("change type :" + event.getType());
        }
    }
}
