package gateway.engine.zk;

import gateway.core.LifeCycle;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snow_young on 16/4/3.
 */
public class ZKEngine implements LifeCycle{
    private static final Logger logger = LoggerFactory.getLogger(ZKEngine.class);

    private static final String ZK_ADDRESS = "localhost:2181";
    private static final String ZK_PATH = "/gateway";
    private List<Action> actions;
    CuratorFramework client;

    private static class Holder{
        static final ZKEngine instance = new ZKEngine();
    }

    private ZKEngine(){
        client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        actions = new ArrayList<>();
    }

    public static ZKEngine getInstance(){
        return Holder.instance;
    }

    @Override
    public void start() {
        // retry should be test
        client.start();
        logger.info("zk client start successfully!");

        TreeCache watcher = new TreeCache(
                client,
                ZK_PATH
        );
        watcher.getListenable().addListener((client1, event) -> {
            switch (event.getType()) {
                case NODE_ADDED: {
                    System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                            + new String(event.getData().getData()));
                    for(Action action : actions){
                        if(action.Accept(event.getData().getPath())){
                            action.Add(event.getData().getPath(), new String(event.getData().getData()));
                        }
                    }
                    break;
                }
                case NODE_UPDATED: {
                    System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                            + new String(event.getData().getData()));
                    for(Action action : actions){
                        if(action.Accept(event.getData().getPath())){
                            action.Update(event.getData().getPath(), new String(event.getData().getData()));
                        }
                    }
                    break;
                }
                case NODE_REMOVED: {
                    System.out.println("TreeNode removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                    for(Action action : actions){
                        if(action.Accept(event.getData().getPath())){
                            action.Remove(event.getData().getPath(), new String(event.getData().getData()));
                        }
                    }
                    break;
                }
                default:
                    System.out.println("Other event: " + event.getType().name());
            }
        });
        try {
            watcher.start();
           logger.info("Register zk watcher successfully!");
        }catch(Exception ex){
            logger.info("listen exception ", ex);
        }
    }

    /**
     *   想一个办法将节点已有的数据download下来
     *   TreeNode added: zktest, value:
         TreeNode added: 22, value: hello
         TreeNode added: 23, value: hello
         TreeNode added: tt, value: hello
         TreeNode added: test, value: hello
     */
    @Override
    public void shutodwn() {
        logger.info("zk engine close");
        client.close();
    }


    public void addAction(Action action){
        this.actions.add(action);
    }

    // for test
    public List<Action> getList(){
        return this.actions;
    }

    public static void main(String[] args) throws InterruptedException {
        // 会有事件回放的
        // 是按 目录层次进行回放的
        // server监听
        ZKEngine.getInstance().addAction(new Action() {
            @Override
            public boolean Accept(String path) {
                logger.info("path :=>" + path);
                Pattern pattern = Pattern.compile("/gateway/backends/(\\w+)/(\\w+)");
                Matcher matcher = pattern.matcher(path);
                if(matcher.find()){
                    logger.info("backend : " + matcher.group(1) + " ;server : " + matcher.group(2));
                    return true;
                }else{
                    logger.info("mismatch here");
                }
                return false;
            }

            @Override
            public void Update(String path, String data) {
                logger.info("path update :=> " + path + " data: " + data);
            }

            @Override
            public void Remove(String path, String data) {
                logger.info("path remove :=> " + path);
            }

            @Override
            public void Add(String path, String data) {
                logger.info("path add :=> " + path + " data: " + data);
            }
        });
        ZKEngine.getInstance().addAction(new Action(){

            @Override
            public boolean Accept(String path) {
                logger.info("path :=>" + path);
                Pattern pattern = Pattern.compile("/gateway/backends/(\\w+)$");
                Matcher matcher = pattern.matcher(path);
                if(matcher.find()){
                    logger.info("backend : " + matcher.group(1));
                    return true;
                }else{
                    logger.info("mismatch here");
                }
                return false;
            }

            @Override
            public void Update(String path, String data) {
                logger.info("update path : " + path);
            }

            @Override
            public void Remove(String path, String data) {
                logger.info("backend path : " + path);
            }

            @Override
            public void Add(String path, String data) {
                logger.info("backend add " + path + " ,data: " + data);
            }
        });
        ZKEngine.getInstance().start();
        Thread.sleep(Integer.MAX_VALUE);
    }


}
