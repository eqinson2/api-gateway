package gateway.core;

/**
 * Created by snow_young on 16/4/5.
 */
public interface Dispatcher {
    void dispatch(HttpSessionContext ctx);
}
