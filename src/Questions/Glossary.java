package Questions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe usata per modellare il glossario che va associato ad una specifica domanda (qualora necessario).
 * È strutturata usando un dizionario (HashMap) che ha come contenuto stringhe e come chiave stringhe.
 */
public class Glossary {

    private HashMap<String, String> dictionary;

    /**
     * Costruttore di default senza parametri
     */
    public Glossary()
    {
        dictionary = null;
    }

    /**
     * Costruttore che sfrutta come dizionario da associare all'istanza quello fornito come parametro
     * @param vDic dizionario da associare all'istanza
     */
    public Glossary(HashMap<String, String> vDic)
    {
        dictionary = new HashMap<String, String>(vDic);
    }

    /**
     * Inserisce una definizione nel dizionario e la associa al termine a cui essa si riferisce
     * @param key termine di cui viene fornita la definizione
     * @param definition definizione
     */
    public void insertDefinition(String key, String definition)
    {
        if(dictionary == null)
            dictionary = new HashMap<String, String>();

        dictionary.put(key, definition);
    }

    /**
     * Restituisce la definizione di un termine fornito come parametro
     * @param key termine di cui si vuole conoscere la definizione
     * @return definizione del termine fornito come parametro
     */
    public String getDefinition(String key)
    {
        if(!dictionary.isEmpty() && dictionary.containsKey(key))
            return dictionary.get(key);
        else
            return null;
    }

    /**
     * Restituisce il set di chiavi contenute nel dizionario
     * @return insieme di chiavi contenute nel dizionario
     */
    public Set<String> keySet()
    {
        return dictionary.keySet();
    }

    /**
     * Verifica se il dizionario è vuoto
     * @return  true se il dizionario è vuoto, false altrimenti
     */
    public boolean isEmpty()
    {
        return dictionary.isEmpty();
    }

}
