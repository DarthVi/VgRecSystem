package VgExceptions;

/**
 * Created by VitoVincenzo on 07/10/2015.
 */
public class MutableInteger {

    private int value;

    public MutableInteger()
    {
        value = 0;
    }

    public MutableInteger(int i)
    {
        value = i;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int i)
    {
        value = i;
    }
}
