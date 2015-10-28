package TypeCheck;

/**
 * Created by VitoVincenzo on 27/10/2015.
 */
public class TypeCheckUtils {

    public static boolean isInteger(String str)
    {
        try
        {
            int d = Integer.parseInt(str);
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        return true;
    }
}
