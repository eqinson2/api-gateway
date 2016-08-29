package gateway.engine.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;


/**
 * Created by snow_young on 16/4/3.
 */
public class TestWatcher {
    private static final String ZK_ADDRESS = "localhost:2181";
    private static final String ZK_PATH = "/zktest";

    // 不能监控子路径中的变化
    // 只能监控直接路径的变化
    public static void PathCache() throws Exception {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // 2.Register watcher
//        TreeCache
        PathChildrenCache watcher = new PathChildrenCache(
                client,
                ZK_PATH,
                true    // if cache data
        );
        watcher.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data == null) {
                System.out.println("No data in event[" + event + "]");
            } else {
                System.out.println("Receive event: "
                        + "type=[" + event.getType() + "]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);
    }


    public static void NodeCache(){

    }


    public static void TreeCache() throws Exception {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // 2.Register watcher
//        TreeCache
        TreeCache watcher = new TreeCache(
                client,
                ZK_PATH
        );
        watcher.getListenable().addListener((client1, event) -> {
            switch (event.getType()) {
                case NODE_ADDED: {
                    System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                            + new String(event.getData().getData()));
                    break;
                }
                case NODE_UPDATED: {
                    System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                            + new String(event.getData().getData()));
                    break;
                }
                case NODE_REMOVED: {
                    System.out.println("TreeNode removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                    break;
                }
                default:
                    System.out.println("Other event: " + event.getType().name());
            }
        });
        watcher.start();
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
//        PathCache();
//        NodeCache();
        TreeCache();
    }
}
