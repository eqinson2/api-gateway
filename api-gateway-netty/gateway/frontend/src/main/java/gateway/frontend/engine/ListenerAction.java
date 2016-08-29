package gateway.frontend.engine;

import gateway.engine.zk.Action;
import gateway.frontend.Listener.Listener;
import gateway.frontend.bootstrap.FrontendBootstrap;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snow_young on 16/4/5.
 * note : one frontend listner present
 */
public class ListenerAction implements Action {
    // listen own ip
    private static final Logger logger = LoggerFactory.getLogger(ListenerAction.class);
    private static final Pattern pattern = Pattern.compile("/gateway/frontend/listener");

    @Override
    public boolean Accept(String path) {
        Matcher matcher = pattern.matcher(path);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    @Override
    public void Update(String path, String data) {
        int port = Integer.parseInt(data);
        List<Listener> listeners = FrontendBootstrap.getListeners();
        for(Listener listener : listeners){
            if(listener.getPort() == port){
                listener.shutodwn();
                listener.setPort(port);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            listener.start();
                        }
                }).start();
            }
        }
    }

    @Override
    public void Remove(String path, String data) {
        int port = Integer.parseInt(data);
        List<Listener> listeners = FrontendBootstrap.getListeners();
        for(Listener listener : listeners){
            if(listener.getPort() == port){
                listener.shutodwn();
            }
        }
    }

    @Override
    public void Add(String path, String data) {
        int port = Integer.parseInt(data);
        List<Listener> listeners = FrontendBootstrap.getListeners();
        for(Listener listener : listeners){
            if(listener.getPort() == port){
                return;
            }
        }
        logger.info("start listen port: " + data);
        Listener listener = new Listener("localhost", port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                listener.start();
            }
        }).start();
    }
}
