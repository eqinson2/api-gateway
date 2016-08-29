package gateway.core.exception;


/**
 * Created by snow_young on 16/3/26.
 */
public class DuplicatAddException extends Exception{
    public DuplicatAddException(String object){
        super(object + " has add");
    }
}
