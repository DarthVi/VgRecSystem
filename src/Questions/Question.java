package Questions;

import TypeCheck.TypeCheckUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
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
    private String answer;

    public Question()
    {
        keyword = null;
        theQuestion = null;
        precursor = null;
        validAnswers = null;
        glossary = null;
        answer = null;
        selected = false;
    }

    public Question(String key, String qText, String precText, ArrayList<String> valAns, Glossary gl)
    {
        keyword = key;
        theQuestion = qText;
        precursor = precText;
        validAnswers = valAns;
        glossary = gl;
        answer = null;
        selected = false;
    }

    public Question(String key, String qText, String precText, String... valAns)
    {
        keyword = key;
        theQuestion = qText;
        precursor = precText;
        setValidAnswers(valAns);
        glossary = null;
        answer = null;
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
        answer = null;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setAnswer(String newAnswer)
    {
        answer = newAnswer;
    }

    public String getAnswer()
    {
        return answer;
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
        if(glossary != null)
        {
            str.println("Glossario per la domanda:\n");

            for (String s : glossary.keySet())
            {
                str.println(s.replace("-", " ") + ": " + glossary.getDefinition(s) + "\n");
            }
        }
        else
            str.println("Glossario non disponibile per questa domanda\n");
    }

    private void promptQuestionExplanation(PrintStream str)
    {
        str.println(QuestionExplanation.get(this.getKeyword()));
    }

    public String askQuestion(PrintStream str, Scanner scn)
    {
        String userAnswer = null;

        str.println(getQuestionText());

        for(int i = 0; i < getValidAnswers().size(); i++)
        {
            str.println(i + 1 + ")" + getValidAnswers().get(i).replace("-", " "));
        }

        while(userAnswer == null)
        {
            userAnswer = scn.nextLine();

            if (userAnswer.equals("?"))
            {
                promptQuestionGlossary(str);
                userAnswer = null;
            }
            else if (userAnswer.equals(""))
                userAnswer = null;
            else if (userAnswer.equals("p"))
            {
                promptQuestionExplanation(str);
                userAnswer = null;
            }
            else if (TypeCheckUtils.isInteger(userAnswer))
            {
                int ansInt = Integer.parseInt(userAnswer);

                if(ansInt >= 1 && ansInt <= getValidAnswers().size())
                    userAnswer = getValidAnswers().get(ansInt - 1);
                else
                    userAnswer = null;
            }
            else if(!userAnswer.equals("e"))
                userAnswer = null;
        }

        return userAnswer;
    }
}

