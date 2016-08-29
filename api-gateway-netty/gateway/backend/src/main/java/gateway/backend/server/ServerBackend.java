package gateway.backend.server;

import gateway.backend.loadralance.RoundRubinLoadBalance;
import gateway.core.exception.DuplicatAddException;
import gateway.core.exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by snow_young on 16/3/23.
 * ServerBackend :
 *      maintain a list of servers that provide same services.
 *      refractor a ServerBackend => AbstractServerBackend
 */
public class ServerBackend implements gateway.core.backend.Backend{
    private static final Logger logger = LoggerFactory.getLogger(ServerBackend.class);

    private String name;
    private List<Server> servers;
    private String balanceStrategy;
    private RoundRubinLoadBalance loadBalance;

    public ServerBackend(){
        this("");
    }
    public ServerBackend(String name){
        this(name, null);
    }

    public ServerBackend(String name, List<Server> sourceServers){
        this(name, sourceServers, "loadbalance");
    }

    public ServerBackend(String name, List<Server> sourceServers, String balanceStrategy){
        this.name = name;
        servers = new CopyOnWriteArrayList<Server>();
        if(sourceServers != null){
            servers.addAll(sourceServers);
        }
        loadBalance = new RoundRubinLoadBalance();
        this.balanceStrategy = balanceStrategy;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    // whether need to add Listener
    // should throws exception
    @Override
    public void addServer(gateway.core.backend.Server server) throws DuplicatAddException{
        if(!(server instanceof Server)){
            return;
        }
        Server serverobj = (Server)server;
        if(servers.contains(server)){
            throw new DuplicatAddException(serverobj.getHostAndPort().toString());
        }
        servers.add(serverobj);
    }

    @Override
    public void rmServer(gateway.core.backend.Server server) throws NotExistException{
        // illegal exception
        if(!(server instanceof Server)){
            return;
        }
        Server serverobj = (Server)server;
        if(servers.contains(serverobj)){
            servers.remove(serverobj);
            return;
        }
        throw new NotExistException(serverobj.getHostAndPort().toString());
    }

    @Override
    public void rmServer(String identify) throws NotExistException {
        String[] host_port = identify.split(":");
        int port = Integer.parseInt(host_port[1]);
        int index = 0;
        for(Server server : servers){
            if(server.getHost() == host_port[0] && server.getPort() == port){
                index++;
                break;
            }
        }
        // is there any question
        servers.remove(index);
    }

    @Override
    public boolean containsServer(gateway.core.backend.Server server) {
        return servers.contains(server);
    }

    @Override
    public void upsertServer(gateway.core.backend.Server server) {
        if(!(server instanceof Server)){
            return;
        }
        Server serverobj = (Server)server;
        servers.remove(serverobj);
        servers.add(serverobj);
    }

    public Server getServer(){
        logger.info("router Listener");
        return loadBalance.select(servers);
    }

    // whether need define unmodified
    public Collection<gateway.core.backend.Server> allServers(){
        return Collections.unmodifiableCollection(servers);
    }

    @Override
    public String toString(){
        return "balancename : " + balanceStrategy;
    }


    // for yaml
    public List<Server> getServers(){
        return this.servers;
    }
    public void setServers(List<Server> sourceServers) throws DuplicatAddException{
        logger.info("set setvers");
        if (servers != null) {
            for(Server server : sourceServers){
                if(!servers.contains(server)){
                    servers.add(server);
                }else{
                    throw new DuplicatAddException(server.toString());
                }
            }
        }
        logger.info("set setvers successfully");
    }

    public void setBalanceStrategy(String balanceStrategy){
        this.balanceStrategy = balanceStrategy;
    }

    public String getBalanceStrategy(){
        return this.balanceStrategy;
    }

    @Override
    public void close() throws IOException {
        for(Server server: servers){
            server.close();
        }
        servers.clear();
    }

    @Override
    public void start() {
        for(Server server: servers){
            server.start();
        }
    }

    @Override
    public void shutodwn() {
        try{
            close();
        }catch (IOException e){
            logger.info("shutdown serverBackend fail", e);
        }
    }
}
