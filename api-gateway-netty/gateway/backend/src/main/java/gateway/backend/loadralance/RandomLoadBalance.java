package gateway.backend.loadralance;

import gateway.backend.server.Server;

import java.util.List;
import java.util.Random;

/**
 * Created by snow_young on 16/3/25.
 * refer to the Duddo implementation of random.
 */
public class RandomLoadBalance extends AbstractLoadBalance implements LoadBalance{

    private final Random random = new Random();

    /**
     * select Listener if weight same, otherwise offset decrease to 0.
     * @param servers
     * @return
     */
    @Override
    public Server doSelect(List<Server> servers) {
        int length = servers.size();
        int totalWeight = 0 ;
        boolean sameweight = true;
        for(int i = 0; i < length; i++){
            totalWeight += servers.get(i).getWeight();
            if(sameweight && i > 0 && servers.get(i).getWeight() != servers.get(i-1).getWeight()){
                sameweight= false;
            }
        }
        if(totalWeight > 0 && !sameweight){
            int offset = random.nextInt(totalWeight);
            for(int i = 0; i < length; i++){
                offset -= servers.get(i).getWeight();
                if(offset < 0){
                    return servers.get(i);
                }
            }
        }
        return servers.get(random.nextInt(length));
    }
}
