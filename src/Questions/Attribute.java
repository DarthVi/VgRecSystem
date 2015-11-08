package Questions;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe enumerativa che associa dei valori simbolici agli interi che vanno da 0 a 8. Tale classe viene usata
 * per facilitare l'accesso a determinate feature di un gioco senza dover ricordare a quale numero essa è associata.
 */
public enum Attribute {
    BEST_GENRE(0), BEST_DIFFICULTY(1), BEST_LEARNING_CURVE(2), BEST_PLOT(3), BEST_AUDIO(4), BEST_GRAPHICS(5), BEST_AI(6), BEST_WORLD_DESIGN(7),
    BEST_GAMEPLAY(8);

    private int attributeValue;

    private static Map<Attribute, Integer> map = new HashMap<Attribute, Integer>();

    static
    {
        for(Attribute attributeEnum : Attribute.values())
        {
            map.put(attributeEnum, attributeEnum.attributeValue);
        }
    }

    private Attribute(final int a) { attributeValue = a; }

    /**
     * Ottiene l'intero a cui è associato l'attributo fornito come parametro
     * @param attr  attributo
     * @return      intero a cui è associato l'attributo
     */
    public static int valueOf(Attribute attr)
    {
        return map.get(attr);
    }
}
