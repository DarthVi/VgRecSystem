package Utils;

/**
 * Classe usata per creare un tipo di dato intero che possa conservare le modifiche apportate ad esso da procedure e funzioni qualora
 * venga passato come parametro.
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

    /**
     * @return      valore intero corrente
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Setta il valore intero con quello fornito come parametro
     * @param i     valore intero da usare
     */
    public void setValue(int i)
    {
        value = i;
    }
}
