package DataAccess;

/**
 * Classe che contiene l'environment CLIPS e i metodi da usare per effettuare determinate query su tale Environment.
 * @see net.sf.clipsrules.jni.Environment
 */
import net.sf.clipsrules.jni.*;

public class CLEnvironmentQuery {

    private Environment env;

    /**
     * Costruttore
     * @param e    Environment CLIPS su cui opereranno i metodi
     */
    public CLEnvironmentQuery(Environment e)
    {
        env = e;
    }

    /**
     * Restituisce un {@link MultifieldValue} di fatti che soddisfano un pattern definito dal primo parametro e delle condizioni
     * definite dal secondo parametro
     * @param template  pattern da soddisfare (generalmente nomi dei fatti da ricercare)
     * @param query     condizioni che i fatti devono soddisfare
     * @return          {@link MultifieldValue}, set dei fatti che soddisfano quanto definito dai parametri
     * @see MultifieldValue
     * @see net.sf.clipsrules.jni.Environment#eval(String)
     */
    public MultifieldValue findFactSet(String template, String query)
    {
        if(query != null)
            return (MultifieldValue) env.eval("(find-all-facts (" + template + ") (" + query + "))");
        else
            return (MultifieldValue) env.eval("(find-all-facts (" + template + ") TRUE)");
    }

    /**
     * Restituisce un {@link MultifieldValue} di fatti presenti in uno specifico modulo
     * @param moduleName    modulo di cui si vogliono ottenere i fatti
     * @return              set di fatti (set rappresentato come {@link MultifieldValue}) presenti nel modulo specificato come parametro
     * @see MultifieldValue
     */
    public MultifieldValue getAllFacts(String moduleName)
    {
        String evalStr = "(get-fact-list " + moduleName + ")";

        return (MultifieldValue) env.eval(evalStr);
    }

    /**
     * Restituisce i fatti che rappresentano il profilo utente finora ottenuto e presente nell'Environment (viene usato {@link #findFactSet(String, String)}
     * in maniera appropriata)
     * @return  fatti che rappresentano il profilo utente
     * @see #findFactSet(String, String)
     */
    public MultifieldValue retrieveUserAnswers()
    {
        //return findFactSet("(?a attribute)", "or (eq ?a:name best-genre) (eq ?a:name best-difficulty) (eq ?a:name best-learning-curve) (eq ?a:name best-plot) (eq ?a:name best-plot-feature) (eq ?a:name best-audio) (eq ?a:name best-graphics) (eq ?a:name best-AI) (eq ?a:name best-gameplay) (eq ?a:name best-world-design)");
        //return findFactSet("(?a attribute)", "neq ?a:name videogame");
        return findFactSet("(?a attribute)", "or (eq ?a:name main-game-purpose) (eq ?a:name patience) (eq ?a:name favourite-genre) (eq ?a:name user-plot-feature) (eq ?a:name plot-quality) (eq ?a:name gaming-experience) (eq ?a:name user-learning-attitude) (eq ?a:name user-audio-quality) (eq ?a:name graphics-detail-quality) (eq ?a:name ai-implementation) (eq ?a:name attitude) (eq ?a:name favourite-world-build-expert) (eq ?a:name favourite-world-build-intermediate) (eq ?a:name favourite-world-build-novice) (eq ?a:name gameplay-style)");
        //return getAllFacts("VIDEOGAMES");
        //return findFactSet("(?a attribute)", "eq ?a:name videogame");
    }
}
