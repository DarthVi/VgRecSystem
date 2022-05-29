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

    public MultifieldValue findFactSet(String template, String query) throws CLIPSException
    {
        if(query != null)
            return (MultifieldValue) env.eval("(find-all-facts (" + template + ") (" + query + "))");
        else
            return (MultifieldValue) env.eval("(find-all-facts (" + template + ") TRUE)");
    }

    public MultifieldValue getAllFacts(String moduleName) throws CLIPSException
    {
        String evalStr = "(get-fact-list " + moduleName + ")";

        return (MultifieldValue) env.eval(evalStr);
    }


}
