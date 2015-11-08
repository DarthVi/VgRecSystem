package TypeCheck;

/**
 * Classe con metodi statici usati per verificare che le stringhe fornite come parametri possano essere convertite
 * in specifici altri tipo di dato.
 */
public class TypeCheckUtils {

    /**
     * Verifica se la stringa fornita come parametro possa essere convertita nel timpo di dato int
     * @param str   stringa da convertire
     * @return      true se la stringa può essere convertita in int, false se si genere una {@link NumberFormatException}, ossia
     *              se la stringa non può essere convertita in intero
     */
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
