package gateway.core.exception;

/**
 * Created by snow_young on 16/3/26.
 */
public class NotExistException extends Exception{

    public NotExistException(String name){
        super(name + " has been exist");
    }
}
