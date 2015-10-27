/**
 * Created by VitoVincenzo on 05/10/2015.
 */
import net.sf.clipsrules.jni.*;

public class CLEnvironmentQuery {

    private Environment env;

    public CLEnvironmentQuery(Environment e)
    {
        env = e;
    }

    public MultifieldValue findFactSet(String template, String query)
    {
        if(query != null)
            return (MultifieldValue) env.eval("(find-all-facts (" + template + ") (" + query + "))");
        else
            return (MultifieldValue) env.eval("(find-all-facts (" + template + ") TRUE)");
    }

    public MultifieldValue getAllFacts(String moduleName)
    {
        String evalStr = "(get-fact-list " + moduleName + ")";

        return (MultifieldValue) env.eval(evalStr);
    }

    public MultifieldValue retrieveUserRecommendations()
    {
        //return findFactSet("(?a attribute)", "or (eq ?a:name best-genre) (eq ?a:name best-difficulty) (eq ?a:name best-learning-curve) (eq ?a:name best-plot) (eq ?a:name best-plot-feature) (eq ?a:name best-audio) (eq ?a:name best-graphics) (eq ?a:name best-AI) (eq ?a:name best-gameplay) (eq ?a:name best-world-design)");
        //return findFactSet("(?a attribute)", "neq ?a:name videogame");
        //return findFactSet("(?a attribute)", "or (eq ?a:name main-game-purpose) (eq ?a:name patience) (eq ?a:name favourite-genre) (eq ?a:name user-plot-feature) (eq ?a:name plot-quality) (eq ?a:name gaming-experience) (eq ?a:name user-learning-attitude) (eq ?a:name user-audio-quality) (eq ?a:name graphics-detail-quality) (eq ?a:name ai-implementation) (eq ?a:name attitude) (eq ?a:name favourite-world-build-expert) (eq ?a:name favourite-world-build-intermediate) (eq ?a:name favourite-world-build-novice) (eq ?a:name gaemplay-style)");
        //return getAllFacts("VIDEOGAMES");
        return findFactSet("(?a attribute)", "eq ?a:name videogame");
    }
}
