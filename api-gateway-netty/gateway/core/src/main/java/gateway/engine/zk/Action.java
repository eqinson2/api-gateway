package gateway.engine.zk;

/**
 * Created by snow_young on 16/4/3.
 */
public interface Action {
    boolean Accept(String path);
    void Update(String path, String data);
    void Remove(String path, String data);
    void Add(String path, String data);
}
