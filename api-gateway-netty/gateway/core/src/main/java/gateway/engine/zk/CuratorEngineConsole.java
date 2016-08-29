package gateway.engine.zk;

import com.alibaba.fastjson.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * Created by snow_young on 16/4/3.
 */
public class CuratorEngineConsole {
    private static final String ZK_ADDRESS = "localhost:2181";
    private static final String ZK_PATH = "/gateway";

    static String path0 = "/gateway/backends/backend1";
    static String path1 = "/gateway/backends/backend1/127.0.0.1:9090";
    static String path2 = "/gateway/backends/backend1/127.0.0.1:9050";
    static String path3 = "/gateway/backends/backend1/127.0.0.1:9010";

    static String listenerPath = "/gateway/frontend/listener";

    public static void main(String[] args) throws Exception {
        startListner();
//        clear();
//        test();
    }

    public static void startListner() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");
        client.create().creatingParentsIfNeeded().
                forPath(listenerPath, "8444".getBytes());
    }

    public static void clearListener() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");
        client.delete().forPath(listenerPath);
    }

    public static void clear() throws Exception{
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");
        client.delete().forPath(path0);
        client.delete().forPath(path1);
        client.delete().forPath(path2);
        client.delete().forPath(path3);
    }

    public static void test() throws Exception{
        JSONObject obj1 = new JSONObject();
        obj1.put("ip", "127.0.0.1");
        obj1.put("port", "9090");
        obj1.put("down", false);
        obj1.put("initialSession", 10);
        obj1.put("maxSession", 1000);
        obj1.put("weight", 0);

        JSONObject obj2 = new JSONObject();
        obj2.put("ip", "127.0.0.1");
        obj2.put("port", "9050");
        obj2.put("down", false);
        obj2.put("initialSession", 10);
        obj2.put("maxSession", 1000);
        obj2.put("weight", 0);

        JSONObject obj3 = new JSONObject();
        obj3.put("ip", "127.0.0.1");
        obj3.put("port", "9010");
        obj3.put("down", false);
        obj3.put("initialSession", 10);
        obj3.put("maxSession", 1000);
        obj3.put("weight", 0);

        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // 2.Client API test
        // 2.1 Create node
        client.create().
                creatingParentsIfNeeded().
                forPath(path0, "headername".getBytes());
        print("create", path1, obj1.toJSONString());
        client.create().
                creatingParentsIfNeeded().
                forPath(path1, obj1.toJSONString().getBytes());

        print("create2 ", path1, obj2.toJSONString());
        client.create().
                creatingParentsIfNeeded().
                forPath(path2, obj2.toJSONString().getBytes());

        print("create3", path3, obj3.toJSONString());
        client.create().
                creatingParentsIfNeeded().
                forPath(path3, obj3.toString().getBytes());

        // 2.2 Get node and data
        print("ls", "/");
        print(client.getChildren().forPath("/"));
        print("get", ZK_PATH);
        print(client.getData().forPath(ZK_PATH));

//        // 2.3 Modify data
//        String update2 = "world";
//        print("set", ZK_PATH, update2);
//        client.setData().forPath("/gateway/backends/backend1/server1", update2.getBytes());
//        print("get", "/gateway/backends/backend1/server1");
//        print(client.getData().forPath("/gateway/backends/backend1/server1"));
//
//        // 2.4 Remove node
//        print("delete", "/gateway/backends/backend1/server2");
//        client.delete().forPath("/gateway/backends/backend1/server2");
//        print("ls", "/");
//        print(client.getChildren().forPath("/"));
    }

    private static void print(String... cmds) {
        StringBuilder text = new StringBuilder("$ ");
        for (String cmd : cmds) {
            text.append(cmd).append(" ");
        }
        System.out.println(text.toString());
    }

    private static void print(Object result) {
        System.out.println(
                result instanceof byte[]
                        ? new String((byte[]) result)
                        : result);
    }
}
