package VgRecMain;

import DataAccess.CLEnvironmentQuery;
import DataAccess.UserData;
import Questions.Question;
import TypeCheck.TypeCheckUtils;
import net.sf.clipsrules.jni.MultifieldValue;

import java.util.List;
import java.util.Scanner;

/**
 * Contiene metodi statici per la gestione delle operazione di modifica delle risposte alle domande già poste all'utente
 */
public class ModAnswerProcessor {

    /**
     * Visualizza una lista di domande a cui è stata data una risposta e chiede se l'utente desidera effettuare delle modifiche.
     * Qualora qualche risposta venga modificata, viene resettato l'{@link net.sf.clipsrules.jni.Environment}, riasseriti i giusti
     * fatti/risposte e viene nuovamente fatta partire l'inferenza. Dato che a seguito delle modifiche il numero di risposte potrebbe
     * non essere sufficiente a produrre delle raccomandazione, la funzione in tal caso richiamerà il metodo {@link VgRecSystem#interact(UserData)}
     * per fare altre domande.
     *
     * @param aQuestion     domande a cui è stata data una risposta
     * @param vg            classe principale che contiene i metodi di interazione e altre strutture dati riguardanti le domande
     * @param ud            contiene i dati dell'utente (username, password, riferimento al file del profilo utente)
     * @see UserData
     * @see VgRecSystem
     */
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

    /**
     * Ripristina le strutture dati usate per fare domande, memorizzare le domande già poste e quelle che non possono essere
     * presentate all'utente. Nello specifico ripristina il numero di domande disponibili e svuota le liste di domande già poste
     * e di domande non disponibili.
     * @param vg classe che contiene le strutture relative alla gestione delle domande
     */
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
