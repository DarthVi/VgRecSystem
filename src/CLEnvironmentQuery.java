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

    public MultifieldValue retrieveUserProfile()
    {
        return findFactSet("(?a attribute)", "or (eq ?a:name best-genre) (eq ?a:name best-difficulty) (eq ?a:name best-learning-curve) (eq ?a:name best-plot) (eq ?a:name best-audio) (eq ?a:name best-graphics) (eq ?a:name best-AI) (eq ?a:name best-gameplay) (eq ?a:name best-world-design)");
    }
}
