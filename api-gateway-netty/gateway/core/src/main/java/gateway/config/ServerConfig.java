package gateway.config;

/**
 * Created by snow_young on 16/3/22.
 */
public class ServerConfig {
    public static final int DUMMY_SEMAPHORE_MAX_COUNT = 5000;
    public static final int MAX_REQUEST_SIZE = 10 * 1024 * 1024;
    public static final int MAX_RESPONSE_SIZE = 10 * 1024 * 1024;
    public static final int MAX_INITIALLINE_LENGTH = 4096;
    public static final int MAX_HEADER_SIZE = 16 * 1024;
    public static final int MAX_CHUNK_SIZE = 16 * 1024;
    //project config items specialization
    public static final String PRE_FILTER = "pre";
    public static final String POST_FILTER = "post";
    public static final String FILTER_SPECIAL = "FILTER";
    public static final String MAX_HTTP_SESSION = "maxHttpSession";
    public static final String MAX_QUEUE_SIZE = "maxQueueSize";
    public static final String URI_PARAMETERKEY = "uri_parameterkey";
//    public static final String CLUSTER = Config.getString("cluster");
    public static final String Fallback_SPECIAL = "FALLBACK";
//    public static final String APPID = Config.getString("appid");
//    public static final String ETRACE_URL = Config.getString("etraceUrl");
    public static volatile String GROUP = "";
    public static String URL_ROUTE_CONFIG_PATH = System.getProperty("user.dir") + "/conf/location.yml";
//    public static String GATEWAY_NAME = NetworkInterfaceHelper.INSTANCE.getLocalHostAddress();
    //listener
    public static volatile long SERVER_TIMEOUT = 1500000000l;//nanosecond
    public static volatile long GATEWAY_TIMEOUT = 3000000000l;//nanosecond
    public static volatile long DELAY = 500l;
    public static volatile long SLEEP_INTERVAL = 500l;

    // ServerBackend Listener



    public static String LOCAL_CONFIG = System.getProperty("user.dir") + "/conf/localConfig.json";
}
