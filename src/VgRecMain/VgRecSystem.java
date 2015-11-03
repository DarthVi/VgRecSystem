package VgRecMain; /**
 * Created by Vito Vincenzo Covella on 16/08/2015.
 */

import DataAccess.CLEnvironmentQuery;
import DataAccess.UserData;
import Questions.Attribute;
import Questions.Question;
import Questions.QuestionsLoader;
import Utils.MutableInteger;
import VgExceptions.CannotAskException;
import Videogames.Videogame;
import Videogames.VideogameComparator;
import net.sf.clipsrules.jni.*;


import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import VgExceptions.PrecursorException;

public class VgRecSystem
{
    Environment clips = null;
    Random rn = null;
    int NUMBER_OF_QUESTIONS = 15;
    private boolean[] selected = new boolean[Attribute.values().length]; //default value in Java = false
    Map<String, Question>[] questionByGenre;
    int questionsCounter;
    List<Question> remQuestion, askedQuestion;
    List<Integer> removedIndex;

    VgRecSystem()
    {
        questionsCounter = 0;
        rn = new Random();
        remQuestion = new ArrayList<Question>();
        removedIndex = new ArrayList<Integer>();
        askedQuestion = new ArrayList<Question>();
        initQuestions();

        clips = new Environment();

        clips.loadFromResource("/clp/videogamesRS.clp");
        clips.reset();
    }

    private void resetSelectedArray()
    {
        for(int i = 0; i < Attribute.values().length; i++)
            selected[i] = false;
    }

    private Attribute selectAttribute()
    {
        ArrayList<Attribute> allFalse = new ArrayList<Attribute>();

        do
        {
            for(Attribute a : Attribute.values())
            {
                if(selected[Attribute.valueOf(a)] == false)
                    allFalse.add(a);
            }

            if(allFalse.isEmpty())
                resetSelectedArray();

        }while(allFalse.isEmpty());

        int sel = rn.nextInt(allFalse.size());
        selected[Attribute.valueOf(allFalse.get(sel))] = true;

        return allFalse.get(sel);
    }

    private Question selectQuestion(MutableInteger attributeIndex) throws PrecursorException, CannotAskException
    {
        Question chosenQ = null;
        List<Question> valList = null;

        do
        {
            attributeIndex.setValue(Attribute.valueOf(selectAttribute()));
            valList = new ArrayList<Question>(questionByGenre[attributeIndex.getValue()].values());

            /*
            To avoid ConcurrentModificationException, the iteration is made on a valList's copy;
            If the object has already been selected (qu.getSelected() == true), we remove it from
            the main list (valLista).
             */
            List<Question> copyL = new ArrayList<Question>(valList);

            for(Question qu : copyL)
            {
                if(qu.getSelected() == true)
                    valList.remove(qu);
            }
        }while(valList.isEmpty());

        int rndIndex = rn.nextInt(valList.size());
        chosenQ = valList.get(rndIndex);

        if(chosenQ.getPrecursorText() == null)
        {
            chosenQ.setSelected(true);
            questionsCounter++;
            return chosenQ;
        }

        CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
        String parts[] = chosenQ.getPrecursorText().split(" ");
        MultifieldValue mv;
        FactAddressValue fv;
        mv = query.findFactSet("(?a attribute)", "eq ?a:name " + parts[0]);

        if(mv.size() == 0)
        {
            throw new PrecursorException(chosenQ.getPrecursorText());
        }

        fv = (FactAddressValue) mv.get(0);
        try {
            String valueSlot = ((LexemeValue) fv.getFactSlot("value")).lexemeValue();

            if(parts[2].equals(valueSlot))
            {
                chosenQ.setSelected(true);
                questionsCounter++;
                return chosenQ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new CannotAskException(chosenQ.getKeyword());
    }

    boolean hFunction(MultifieldValue field)
    {
        if(questionsCounter == NUMBER_OF_QUESTIONS)
            return false;

        if(field.size() == 0)
            return true;

        for(int i = 0;  i < field.size(); i++)
        {
            FactAddressValue fv = (FactAddressValue) field.get(i);

            try
            {

                float certainty = ((NumberValue) fv.getFactSlot("certainty")).floatValue();
                certainty /= 100;
                float val = 1/(questionsCounter * 0.115f) * certainty;

                if(val > 0.6f)
                    return false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;

    }

    private Question chooseQuestionFromKeyword(String keyword) throws NoSuchFieldException
    {

        for(Attribute a : Attribute.values())
        {
            if(questionByGenre[Attribute.valueOf(a)].containsKey(keyword))
            {
                questionsCounter++;
                selected[Attribute.valueOf(a)] = true;
                Question qu = questionByGenre[Attribute.valueOf(a)].get(keyword);
                qu.setSelected(true);
                return qu;
            }
        }

        throw new NoSuchFieldException("String not found");
    }

    void initQuestions()
    {
        questionByGenre =  (HashMap<String, Question>[]) Array.newInstance(HashMap.class, Attribute.values().length);
        QuestionsLoader.loadQuestions(questionByGenre);
    }


    private void removeQuestion(String key)
    {
        for(Attribute a : Attribute.values())
        {
            if(questionByGenre[Attribute.valueOf(a)].containsKey(key))
            {
                remQuestion.add(questionByGenre[Attribute.valueOf(a)].get(key));
                removedIndex.add(Attribute.valueOf(a));
                questionByGenre[Attribute.valueOf(a)].remove(key);
            }
        }

        NUMBER_OF_QUESTIONS--;
    }

    private boolean askMoreQuestion(PrintStream str, Scanner scn)
    {
        String userAnswer;
        str.println("Il sistema ha già ottenuto dei consigli da visualizzare.\n Continuare con altre domande" +
                " per ottenere risultati più accurati? digitare \"si\" o \"no\". ");

        do {
            userAnswer = scn.nextLine();
        }while(!userAnswer.equals("si") && !userAnswer.equals("no"));

        if(userAnswer.equals("si"))
            return true;
        else
            return false;
    }

    public boolean interact(UserData userData)
    {
        MutableInteger aIndex = new MutableInteger();
        Question q = null;
        boolean precursorSatisfied = false;
        MultifieldValue mv = null;
        CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
        boolean repeatCycle;
        boolean ask = false;

        System.out.println("");
        System.out.println("Premere \"?\" e digitare invio per ottenere il glossario (per alcune domande non sara' disponibile).\n" +
                           "Premere \"e\" e digitare invio per uscire; se è stato effettuata la registrazione o il login, il sistema salverà i dati già ottenuti.\n" +
                           "Premere \"p\" e invio per capire perche' e' stata posta la domanda visualizata." +
                           "Digitare il numero appropriato e poi invio per rispondere alle domande\n");
        do
        {
            do
            {
                try
                {
                    q = selectQuestion(aIndex);
                    precursorSatisfied = true;
                }
                catch(PrecursorException e)
                {
                    String precursor = e.getMessage();
                    String[] parts = precursor.split(" ");
                    String keyword = parts[0];
                    selected[aIndex.getValue()] = false;

                    try {
                        q = chooseQuestionFromKeyword(keyword);
                    } catch (NoSuchFieldException e1) {
                        e1.printStackTrace();
                    }
                    precursorSatisfied = true;
                }
                catch(CannotAskException e)
                {
                    String precursor = e.getMessage();
                    String[] parts = precursor.split(" ");
                    String keyword = parts[0];
                    selected[aIndex.getValue()] = false;
                    removeQuestion(keyword);
                    precursorSatisfied = false;
                }
            }while(!precursorSatisfied && questionsCounter < NUMBER_OF_QUESTIONS);

            if(precursorSatisfied)
            {
                String answer = q.askQuestion(System.out, new Scanner(System.in));

                if(!answer.equals("e"))
                {
                    askedQuestion.add(q);
                    q.setAnswer(answer);

                    assertAttribute(q.getKeyword(), answer);

                    clips.run();
                    clips.eval(("(focus MAIN RULES VIDEOGAMES)"));

                    mv = query.findFactSet("(?a attribute)", "eq ?a:name videogame");

                    if(!hFunction(mv) && questionsCounter < NUMBER_OF_QUESTIONS && ask == false)
                    {
                        repeatCycle = askMoreQuestion(System.out, new Scanner(System.in));
                        ask = true;
                    }
                    else if(questionsCounter == NUMBER_OF_QUESTIONS)
                        repeatCycle = false;
                    else
                        repeatCycle = true;
                }
                else
                {
                    if(userData.getUsername() != null)
                        saveUserProfileToFile(userData);

                    return false;
                }
            }
            else
                repeatCycle = false;

        }while(repeatCycle);

        return true;
    }

    void printSuggestions(PrintStream str)
    {
        CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
        MultifieldValue mv = query.findFactSet("(?a attribute)", "eq ?a:name videogame");
        List<Videogame> vgList = new ArrayList<Videogame>();

        if(mv.size() != 0)
        {
            try
            {
                for (int i = 0; i < mv.size(); i++)
                {
                    FactAddressValue fv = (FactAddressValue) mv.get(i);
                    float certRankingVal = ((NumberValue) fv.getFactSlot("certainty")).floatValue();
                    String gameName = ((LexemeValue) fv.getFactSlot("value")).lexemeValue();
                    vgList.add(new Videogame(gameName, certRankingVal));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Collections.sort(vgList, new VideogameComparator());
            Collections.reverse(vgList);

            str.println("Ecco i giochi consigliati: ");

            for(int i = 0; i < vgList.size(); i++)
            {
                str.println((i+1) + ") " + vgList.get(i).getTitle() + ": " + vgList.get(i).getCertainty() + "%");
            }


        }
    }

    void assertAttribute(String name, String value)
    {
        String toAssert = "(attribute (name " + name + ") (value " + value + "))";
        clips.assertString(toAssert);
    }

    public void saveUserProfileToFile(UserData userdata)
    {
        CLEnvironmentQuery envquery = new CLEnvironmentQuery(clips);
        MultifieldValue mv = envquery.retrieveUserAnswers();
        userdata.clearFile();
        //userdata.println("(deffacts VIDEOGAMES::gamer-reccomendations");

        for (int i = 0; i < mv.size(); i++)
        {
            FactAddressValue fv = (FactAddressValue) mv.get(i);
            try {
                String atName = ((LexemeValue) fv.getFactSlot("name")).lexemeValue();
                String atValue = ((LexemeValue) fv.getFactSlot("value")).lexemeValue();
                float certRankingVal = ((NumberValue) fv.getFactSlot("certainty")).floatValue();

                String profilePiece = "(attribute (name " + atName + ") (value " + atValue + ") (certainty " + Float.toString(certRankingVal) + "))";
                userdata.println(profilePiece);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //userdata.println(")");
    }

    public void assertUserProfile(UserData userdata)
    {
        String str;

        clips.reset();
        askedQuestion.clear();
        questionsCounter = 0;

        userdata.readCursorToBegin();

        try {
            while((str = userdata.readLine()) != null)
            {
                FactAddressValue fv = clips.assertString(str);
                String key = ((LexemeValue) fv.getFactSlot("name")).lexemeValue();
                String val = ((LexemeValue) fv.getFactSlot("value")).lexemeValue();
                Question qu = chooseQuestionFromKeyword(key);
                qu.setAnswer(val);
                askedQuestion.add(qu);
                clips.run();
                clips.eval(("(focus MAIN RULES VIDEOGAMES)"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[]  args)
    {
        VgRecSystem rec = new VgRecSystem();

        MainMenu.menu(rec);
    }

}