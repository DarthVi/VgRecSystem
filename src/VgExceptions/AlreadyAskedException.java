package VgExceptions;

/**
 * Created by VitoVincenzo on 06/10/2015.
 */
public class AlreadyAskedException extends Exception {

    public AlreadyAskedException() {}

    public AlreadyAskedException(String message)
    {
        super(message);
    }
}
