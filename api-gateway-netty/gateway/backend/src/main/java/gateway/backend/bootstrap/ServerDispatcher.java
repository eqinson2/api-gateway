package gateway.backend.bootstrap;

import gateway.backend.service.Service;
import gateway.core.Dispatcher;
import gateway.core.HttpSessionContext;
import gateway.core.SessionState;
import gateway.core.exception.NotFoundException;
import org.omg.CORBA.DataInputStream;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 * Created by snow_young on 16/4/5.
 */
public class ServerDispatcher implements Dispatcher{
    private static final Logger logger = LoggerFactory.getLogger(ServerDispatcher.class);

    @Override
    public void dispatch(HttpSessionContext httpSessionContext) {
        logger.info("dispatch message");
        try {
            // router function
            httpSessionContext.setBackend(Service.getInstance().getMux().dispatcher("headername"));
        }catch(NotFoundException e){
            logger.info("not found exception : ");
        }
        httpSessionContext.setState(SessionState.SEND_REQUEST);
        httpSessionContext.execute();
    }
}
