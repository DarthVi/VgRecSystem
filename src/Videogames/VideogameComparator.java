package Videogames;

import java.util.Comparator;
import java.util.List;

/**
 * Classe che implementa l'interfaccia {@link Comparator} e che verrà usata per
 * ordinare le istanze di {@link Videogame} tramite il metodo {@link java.util.Collections#sort(List)}
 */
public class VideogameComparator implements Comparator<Videogame> {
    public int compare(Videogame v1, Videogame v2)
    {
        return v1.compareTo(v2);
    }
}
