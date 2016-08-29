package gateway.core.exception;

/**
 * Created by snow_young on 16/3/26.
 */
public class NotFoundException extends Exception{
    public NotFoundException(String name){
        super(name + " not found");
    }
}
