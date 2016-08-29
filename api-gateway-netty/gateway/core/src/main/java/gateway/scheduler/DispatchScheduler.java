package gateway.scheduler;

import gateway.core.Dispatcher;
import gateway.core.LifeCycle;
import gateway.config.ServerConfig;
import gateway.core.HttpSessionContext;
import gateway.core.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * Created by snow_young on 16/3/23.
 * feature :
 *      limit the requests based the token
 *      control whether accept new request
 *
 */
public class DispatchScheduler extends Thread implements LifeCycle{

    private static final int WORKING_THREAD_COUNT = 4;
    private static final Logger logger = LoggerFactory.getLogger(DispatchScheduler.class);
    // resource limit
    private final Semaphore activeHttpSessions = new Semaphore(ServerConfig.DUMMY_SEMAPHORE_MAX_COUNT);
    private BlockingQueue<HttpSessionContext> queue = new LinkedBlockingDeque<>();
    private volatile int maxHttpSession = 0;
    private volatile int maxQueueSize = 0;
    // control the thread action.
    private volatile boolean isRunning = true;
    // control the enqueue action.
    private volatile boolean isOpen = true;
    private ExecutorService[] executors = new ExecutorService[WORKING_THREAD_COUNT] ;
    private Dispatcher dispatcher;

    public void setDispatcher(Dispatcher dispatcher){
        this.dispatcher = dispatcher;
    }

    public Dispatcher getDispatcher(){
        return this.dispatcher;
    }

    private static class Holder{
        private static DispatchScheduler instance = new DispatchScheduler();
    }
    public static DispatchScheduler getInstance(){ return Holder.instance; }

    private DispatchScheduler(){
        this.maxHttpSession = 100;
        try {
            this.activeHttpSessions.acquire(ServerConfig.DUMMY_SEMAPHORE_MAX_COUNT - maxHttpSession);
        } catch (InterruptedException e) {
            logger.error("scheduler initialize error", e);
        }
        this.maxQueueSize = 5000;
        IntStream.range(0, WORKING_THREAD_COUNT)
                .forEach(i -> this.executors[i] = Executors.newSingleThreadExecutor());
    }

    public boolean enqueue(HttpSessionContext httpSessionContext){
        // should be moved to other
        if(!isOpen) {
            return false;
        }
        try{
            if(queue.size() > maxQueueSize){
                // Listener is ?
                logger.warn("max queue size is exceed");
                // record
                httpSessionContext.closeClientChannel();
                return false;
            }
            logger.info("concurrent queue size : " + queue.size());
            return queue.add(httpSessionContext);
        }catch(Exception e){
            logger.error("enqueue scheduler failed", e);
            return false;
        }
    }


    public void run(){
        while(this.isRunning){
            HttpSessionContext httpSessionContext;
            try{
                if(activeHttpSessions.availablePermits() == 0){
                    // Listener
                    logger.warn("available == 0, Listener is : " );
                }
                logger.info("available Listener is : " + activeHttpSessions.availablePermits());
                activeHttpSessions.acquire();
                httpSessionContext = queue.take();
//                httpSessionContext.setSemaphore(activeHttpSessions);
                httpSessionContext.addCloseCallback(()->{
                    logger.info("activeHttpSession release");
                    AtomicReference<Semaphore> sem = new AtomicReference<Semaphore>();
                    sem.set(DispatchScheduler.this.activeHttpSessions);
                    sem.get().release();
                });
                // record
                dispatch(httpSessionContext);
            }catch (NullPointerException ex){
                logger.info("close succefully");
                IntStream.range(0, WORKING_THREAD_COUNT)
                        .forEach(i -> this.executors[i].shutdown());
            }catch(Exception e){
                logger.error("job schedule run failed", e);
                // record
            }
        }
    }

    // react by pull
    private void dispatch(HttpSessionContext httpSessionContext){
        int idx = Math.abs(httpSessionContext.hashCode() % WORKING_THREAD_COUNT);
        this.executors[idx].execute(() -> {
            dispatcher.dispatch(httpSessionContext);
        });
    }

    // shutdown gracefully
    // add a protection action : isclose to prevent the add action when close
    private void shutdwon0(){
        logger.warn("scheduler begin to shutdown");
        // reject new element to enqueue
        this.maxQueueSize = -1;
        // 2 seconds
        // reference to futuretask
        long MAX_AWAIT = 2000000000;
        long await = MAX_AWAIT;
        while(true){
            logger.warn("检查剩余请求gateway........ available requests : " + activeHttpSessions.availablePermits());
            logger.warn("schedule size : " + DispatchScheduler.this.queue.size());
            logger.info("maxHttpSession : " + maxHttpSession);
            if(activeHttpSessions.availablePermits() >= maxHttpSession - 1){
                logger.warn("剩余请求处理完毕");
                this.isRunning = false;
                // solve the blocking take action
                if(activeHttpSessions.availablePermits() != maxHttpSession) {
                    this.queue.add(new HttpSessionContext() {
                        @Override
                        public synchronized void execute() {
                            return;
                        }

                        @Override
                        public void addCloseCallback(CloseListener listner){
                            listner.onActive();
                            logger.info(" null available Listener is : " + DispatchScheduler.this.activeHttpSessions.availablePermits());
                            throw new NullPointerException();
                        }
                    });
                }
                break;
            }
            LockSupport.parkNanos(await);
            await = (await <= 0 ? await >> 1 : MAX_AWAIT);
        }
    }

    // flush all requests forcefully
    // cause rob action
    @Override
    public void shutodwn(){
        while(DispatchScheduler.this.queue.size() > 0){
            try{
                HttpSessionContext httpSessionContext = queue.take();
                httpSessionContext.setState(SessionState.QUIT);
                httpSessionContext.execute();
            }catch(Exception e){
                logger.error("close schedule fail", e);
                // record
            }
        }
        DispatchScheduler.this.shutdwon0();
    }

    public int getAvailableSessions() {
        return this.activeHttpSessions.availablePermits();
    }

    public int getMaxQueueSize(){
        return this.maxQueueSize;
    }

    public int getQueueSize(){
        return this.queue.size();
    }

    public void setMaxQueueSize(int maxQueueSize){
        this.maxQueueSize = maxQueueSize;
    }

    public int getMaxHttpSession(){
        return this.maxHttpSession;
    }

    public void setMaxHttpSession(int maxHttpSession){
        maxHttpSession = Math.min(maxHttpSession, ServerConfig.DUMMY_SEMAPHORE_MAX_COUNT);
        maxHttpSession = Math.max(1, maxHttpSession);

        int delta = maxHttpSession - this.maxHttpSession;
        if (delta == 0) {
            return;
        }

        if (delta > 0) {
            activeHttpSessions.release(delta);
        } else {
            IntStream.range(delta, 0).forEach(i -> {
                try {
                    activeHttpSessions.acquire();
                } catch (Exception e) {
                    logger.error("set max http session failed, cannot acquire semaphore", e);
                }
            });
        }
        this.maxHttpSession = maxHttpSession;
    }
}

