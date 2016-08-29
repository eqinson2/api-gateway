package gateway.core;

/**
 * Created by snow_young on 16/3/24.
 */
public interface Listener {
    void onSuccess(Object obj);
    void onFail(Object obj);
    void onCreate();
}
