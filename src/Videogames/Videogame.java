package Videogames;

/**
 * Classe usata per modellare le caratteristiche necessarie per il ranking dei videogames (titolo e certainty value).
 * La classe implementa l'interfaccia {@link Comparable}.
 * @see VideogameComparator
 */
public class Videogame implements Comparable<Videogame> {

    private String title;
    private float certainty;

    /**
     * Costruttore di default senza parametri: il titolo è nullo e il valore di confidenza è pari a 0
     */
    public Videogame()
    {
        title = null;
        certainty = 0.0f;
    }

    /**
     * Costruttore con parametri
     * @param t     titolo da assegnare al videogame
     * @param c     valore di certezza della raccomandazione da usare per il ranking
     */
    public Videogame(String t, float c)
    {
        title = t;
        certainty = c;
    }

    /**
     * Costruttore che contiene solo il titolo del videogame
     * @param t     titolo del videogame
     */
    public Videogame(String t)
    {
        title = t;
        certainty = 0.0f;
    }

    /**
     * Restituisce il titolo del videogioco
     * @return  titolo del videogioco
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Assegna al videogioco il titolo fornito come parametro
     * @param newTitle      nuovo titolo del videogioco
     */
    public void setTitle(String newTitle)
    {
        title = newTitle;
    }

    /**
     * Assegna al videogioco il nuovo valore di certezza della raccomandazione
     * @param cert      nuovo valore di certezza della raccomandazione
     */
    public void setCertainty(float cert)
    {
        certainty = cert;
    }

    /**
     * Restituisce il valore di certezza della raccomandazione (da usare per rankare il videogame quando viene consigliato)
     * @return      valore di certezza della raccomandazione
     */
    public float getCertainty()
    {
        return certainty;
    }

    /**
     * Confronta il videogioco attuale con quello fornito come parametro.
     * @param v     videogioco con cui confrontare il videogioco attuale
     * @return      -1 se il valore di certezza del videogioco attuale è minore di quello fornito come parametro, 0 se è uguale, 1
     *              se il valore di certezza è maggiore di quello del videgioco fornito come parametro
     * @see VideogameComparator
     * @see Comparable
     */
    public int compareTo(Videogame v)
    {
        if(this.getCertainty() < v.getCertainty())
            return -1;
        else if(this.getCertainty() == v.getCertainty())
            return 0;
        else
            return 1;
    }
}
