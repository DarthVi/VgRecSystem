/**
 * Created by VitoVincenzo on 14/10/2015.
 */
public class Videogame implements Comparable<Videogame> {

    private String title;
    private float certainty;

    public Videogame()
    {
        title = null;
        certainty = 0.0f;
    }

    public Videogame(String t, float c)
    {
        title = t;
        certainty = c;
    }

    public Videogame(String t)
    {
        title = t;
        certainty = 0.0f;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }

    public void setCertainty(float cert)
    {
        certainty = cert;
    }

    public float getCertainty()
    {
        return certainty;
    }

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
