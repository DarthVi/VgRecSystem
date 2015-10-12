import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by VitoVincenzo on 17/09/2015.
 */
public class Question {

    private String keyword;
    private String theQuestion;
    private String precursor;
    private ArrayList<String> validAnswers;
    public Glossary glossary;
    private boolean selected;

    public Question()
    {
        keyword = null;
        theQuestion = null;
        precursor = null;
        validAnswers = null;
        glossary = null;
        selected = false;
    }

    public Question(String key, String qText, String precText, ArrayList<String> valAns, Glossary gl)
    {
        keyword = key;
        theQuestion = qText;
        precursor = precText;
        validAnswers = valAns;
        glossary = gl;
        selected = false;
    }

    public Question(String key, String qText, String precText, String... valAns)
    {
        keyword = key;
        theQuestion = qText;
        precursor = precText;
        setValidAnswers(valAns);
        glossary = null;
        selected = false;
    }

    public Question(String key, String qText, String precText, HashMap<String, String> gl, String... valAns)
    {
        keyword = key;
        theQuestion = qText;
        precursor = precText;
        setValidAnswers(valAns);
        glossary = new Glossary(gl);
        selected = false;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String key)
    {
        keyword = key;
    }

    public String getQuestionText()
    {
        return theQuestion;
    }

    public String getPrecursorText()
    {
        return precursor;
    }

    public ArrayList<String> getValidAnswers()
    {
        return validAnswers;
    }

    public void setQuestionText(String text)
    {
        theQuestion = text;
    }

    public void setPrecursorText(String text)
    {
        precursor = text;
    }

    public void setValidAnswers(String... vAns)
    {
        validAnswers = new ArrayList<String>();

        for(String v : vAns)
        {
            validAnswers.add(v);
        }
    }

    public boolean getSelected()
    {
        return selected;
    }

    public void setSelected(boolean val)
    {
        selected = val;
    }

    public boolean testAnswer(String ans)
    {
        return validAnswers.contains(ans);
    }

    private void promptQuestionGlossary(PrintStream str)
    {
        if(!glossary.isEmpty())
        {
            str.println("Glossario per la domanda:\n");

            for (String s : glossary.keySet())
            {
                str.println(s + ": " + glossary.getDefinition(s) + "\n");
            }
        }
        else
            str.println("Glossario non disponibile per questa domanda\n");
    }

    public String askQuestion(PrintStream str, Scanner scn)
    {
        String userAnswer = null;
        int answerIndex = -1;

        while(answerIndex < 0)
        {
            str.println(getQuestionText());

            userAnswer = scn.nextLine();

            if (userAnswer.equals("?"))
                promptQuestionGlossary(str);

            answerIndex = getValidAnswers().indexOf(userAnswer);
        }

        return userAnswer;
    }
}

