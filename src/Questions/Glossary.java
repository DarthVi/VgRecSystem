package Questions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by VitoVincenzo on 21/09/2015.
 */
public class Glossary {

    private HashMap<String, String> dictionary;

    public Glossary()
    {
        dictionary = null;
    }

    public Glossary(HashMap<String, String> vDic)
    {
        dictionary = new HashMap<String, String>(vDic);
    }

    public void insertDefinition(String key, String definition)
    {
        if(dictionary == null)
            dictionary = new HashMap<String, String>();

        dictionary.put(key, definition);
    }

    public String getDefinition(String key)
    {
        if(!dictionary.isEmpty() && dictionary.containsKey(key))
            return dictionary.get(key);
        else
            return null;
    }

    public Set<String> keySet()
    {
        return dictionary.keySet();
    }

    public boolean isEmpty()
    {
        return dictionary.isEmpty();
    }

}
