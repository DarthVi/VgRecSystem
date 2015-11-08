package Questions;

import TypeCheck.TypeCheckUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe usata per modellare una domanda. Ogni domanda è caratterizzata da una parola chiave che la identifica,
 * dal testo che verrà mostrato quando verrà posta la domanda all'utente, da un eventuale prerequisito che deve essere soddisfatto
 * (ossia una specifica risposta ad un'altra domanda; se non presente, il campo sarà nullo), dalla lista di risposte valide,
 * da un eventuale glossario (anche in questo caso, se non necessario, il glossario sarà nullo), da un flag che indica se la
 * domanda è già stata posta o meno e da un campo usato per memorizzare la risposta.
 */
public class Question {

    private String keyword;
    private String theQuestion;
    private String precursor;
    private ArrayList<String> validAnswers;
    public Glossary glossary;
    private boolean selected;
    private String answer;

    /**
     * Costruttore di default
     */
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

    /**
     * Costruttore con parola chiave, testo, precursore, ArrayList di risposte valide e glossario
     *
     * @param key       parola chiave
     * @param qText     testo che verrà visualizzato dall'utente a cui viene posta la domanda
     * @param precText  prerequisito da soddisfare prima di porre la domanda
     * @param valAns    sequenza di risposte valide
     * @param gl        glossario
     */
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

    /**
     * Costruttore con parola chiave, testo, precursore e array di risposte valide fornite usando variable args
     *
     * @param key       parola chiave
     * @param qText     testo che verrà visualizzato dall'utente a cui viene posta la domanda
     * @param precText  prerequisito da soddisfare prima di porre la domanda
     * @param valAns    sequenza di risposte valide
     */
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

    /**
     * Costruttore con parola chiave, testo, precursore, glossario passato come HashMap e variable args di rispote valide
     *
     * @param key       parola chiave
     * @param qText     testo che verrà visualizzato dall'utente a cui viene posta la domanda
     * @param precText  prerequisito da soddisfare prima di porre la domanda
     * @param gl        glossario
     * @param valAns    sequenza di risposte valide
     */
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

    /**
     * @return parola chiave che identifica la domanda
     */
    public String getKeyword()
    {
        return keyword;
    }

    /**
     * Setta la risposta assegnandole il valore newAnswer
     *
     * @param newAnswer risposta data alla domanda
     */
    public void setAnswer(String newAnswer)
    {
        answer = newAnswer;
    }

    /**
     * @return risposta data alla domanda
     */
    public String getAnswer()
    {
        return answer;
    }

    /**
     * Setta la parola chiave
     * @param key
     */
    public void setKeyword(String key)
    {
        keyword = key;
    }

    /**
     * @return testo che viene visualizzato dall'utente quando gli viene posta la domanda
     */
    public String getQuestionText()
    {
        return theQuestion;
    }

    /**
     * @return prerequisito da soddisfare: specifica risposta ad una specifica domanda identificata da una parola chiave
     */
    public String getPrecursorText()
    {
        return precursor;
    }

    /**
     * @return lista di risposte valide
     */
    public ArrayList<String> getValidAnswers()
    {
        return validAnswers;
    }

    /**
     * Imposta il testo da mostrare all'utente quando viene posta la domanda
     * @param text  testo che verrà visualizzato dall'utente quando gli verrà mostrata la domanda
     */
    public void setQuestionText(String text)
    {
        theQuestion = text;
    }

    /**
     * Imposta il prerequisito da soddisfare
     * @param text  prerequisito da soddisfare
     */
    public void setPrecursorText(String text)
    {
        precursor = text;
    }

    /**
     * Imposta la lista di risposte valide
     * @param vAns variable args di risposte valide
     */
    public void setValidAnswers(String... vAns)
    {
        validAnswers = new ArrayList<String>();

        for(String v : vAns)
        {
            validAnswers.add(v);
        }
    }

    /**
     * @return true se la domanda è già stata posta, false in caso contrario
     */
    public boolean getSelected()
    {
        return selected;
    }

    /**
     * Imposta il flag che indica se la domanda è già stata posta o meno
     * @param val flag booleano che indica se la domanda è già stata posta o meno
     */
    public void setSelected(boolean val)
    {
        selected = val;
    }

    /**
     * Verifica che la risposta considerata sia presente fra quelle considerate
     *
     * @param ans risposta da verificare
     * @return true se ans è presente fra le risposte valide, false altrimenti
     */
    public boolean testAnswer(String ans)
    {
        return validAnswers.contains(ans);
    }

    /**
     * Stampa su PrintStream il glossario
     * @param str stream su cui stampare il glossario
     * @see PrintStream
     */
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

    /**
     * Stampa su PrintStream una spiegazione del motivo per cui è stata posta la domanda
     * @param str stream su cui stampare
     * @see PrintStream
     */
    private void promptQuestionExplanation(PrintStream str)
    {
        str.println(QuestionExplanation.get(this.getKeyword()));
    }

    /**
     * Stampa su PrintStream il testo relativo alla domanda e cattura da Scanner la risposta dell'utente.
     * Se l'utente ha digitato "?", allora mostra il glossario.
     * Se l'utente ha digitato "p", allora stampa su PrintStream una spiegazione relativa al motivo per cui è stata posta la specifica domanda.
     * In tutti gli altri casi la risposta data viene sottoposta a controllo: se è "vuota" (l'utente preme "invio" senza digitare caratteri),
     * il sistema chiederà nuovamente la risposta; se la risposta data è valida, la funziona termina ritornando il valore della risposta data.
     *
     * @param str PrintStream su cui stampare i messaggi
     * @param scn Scanner da cui acquisire l'input
     * @return risposta data dall'utente e presente fra le risposte valide
     */
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

