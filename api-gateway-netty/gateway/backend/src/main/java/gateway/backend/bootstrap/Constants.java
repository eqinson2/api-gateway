package gateway.backend.bootstrap;

/**
 * Created by snow_young on 16/3/26.
 */
public class Constants {
    public static String BOOTSTRAP_FILE = System.getProperty("user.dir") + "/backend/src/main/resources/servers.yaml";
    public static volatile int MAX_SESSION = 1000;
    public static volatile int INITIAL_SESSION = 10;
}
