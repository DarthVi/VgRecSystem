import java.util.HashMap;
import java.util.Map;

/**
 * Created by VitoVincenzo on 25/09/2015.
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

    public static int valueOf(Attribute attr)
    {
        return map.get(attr);
    }
}
