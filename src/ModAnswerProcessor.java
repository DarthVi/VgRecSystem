import DataAccess.UserData;
import TypeCheck.TypeCheckUtils;
import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.MultifieldValue;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by VitoVincenzo on 27/10/2015.
 */
public class ModAnswerProcessor {

    public static final void modifyAnswers(List<Question> aQuestion, VgRecSystem vg, UserData ud)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Riepilogo delle risposte date, è possibile scegliere se modificarle: ");

        for(int i = 0; i < aQuestion.size(); i++)
        {
            System.out.println((i + 1) + " DOMANDA: " + aQuestion.get(i).getQuestionText() + " RISPOSTA: " + aQuestion.get(i).getAnswer());
        }

        String ans = null;
        int choice = 0;
        boolean modified = false;

        do
        {
            do
            {
                System.out.println("Premere \"e\" per uscire o digitare il numero della risposta da modificare");

                ans = sc.nextLine();

                if(!ans.equals("e") && TypeCheckUtils.isInteger(ans))
                    choice = Integer.parseInt(ans);
                else
                    choice = 0;

            }while(!ans.equals("e") && (choice < 1 || choice > aQuestion.size()));

            if(!ans.equals("e"))
            {
                String questionAnswer = aQuestion.get(choice - 1).askQuestion(System.out, new Scanner(System.in));
                aQuestion.get(choice - 1).setAnswer(questionAnswer);
                modified = true;
            }

        }while(!ans.equals("e"));

        if(modified)
        {
            vg.clips.reset();

            for(int i = 0; i < aQuestion.size(); i++)
            {
                vg.assertAttribute(aQuestion.get(i).getKeyword(), aQuestion.get(i).getAnswer());
                vg.clips.run();
                vg.clips.eval("(focus MAIN RULES VIDEOGAMES)");
            }

            restoreQuestionContainer(vg);
            vg.questionsCounter = aQuestion.size();

            CLEnvironmentQuery query = new CLEnvironmentQuery(vg.clips);

            MultifieldValue mv = query.findFactSet("(?a attribute)", "eq ?a:name videogame");

            boolean printableResults = true;

            if(vg.hFunction(mv))
                printableResults = vg.interact(ud);

            if(printableResults)
                vg.printSuggestions(System.out);
        }

    }

    public static final void restoreQuestionContainer(VgRecSystem vg)
    {
        for(int i = 0; i < vg.remQuestion.size(); i++)
        {
            vg.questionByGenre[vg.removedIndex.get(i)].put(vg.remQuestion.get(i).getKeyword(), vg.remQuestion.get(i));
            vg.NUMBER_OF_QUESTIONS++;
        }

        vg.remQuestion.clear();
    }
}
