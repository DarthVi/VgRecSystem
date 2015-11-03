package Videogames;

import java.util.Comparator;

/**
 * Created by VitoVincenzo on 14/10/2015.
 */
public class VideogameComparator implements Comparator<Videogame> {
    public int compare(Videogame v1, Videogame v2)
    {
        return v1.compareTo(v2);
    }
}
