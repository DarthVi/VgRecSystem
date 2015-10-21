/**
 * Created by Vito Vincenzo Covella on 16/08/2015.
 */

import VgExceptions.CannotAskException;
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
    /*final static String EXIT_LOOP = "exit-loop";
    final static String TRUE = "TRUE";
    final static String FALSE = "FALSE";*/
    int NUMBER_OF_QUESTIONS = 15;
    private boolean[] selected = new boolean[Attribute.values().length]; //default value in Java = false
    private Map<String, Question>[] questionByGenre;
    private int questionsCounter;

    VgRecSystem()
    {
        questionsCounter = 0;
        rn = new Random();
        initQuestions();

        clips = new Environment();

        clips.loadFromResource("/clp/videogamesRS.clp");
        clips.reset();
        //debug
        //System.out.println("clips reset done");
        //System.out.println(clips.eval("(facts)").toString());
        //System.out.println(clips.eval("(get-focus-stack)").toString());
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

    private boolean hFunction(MultifieldValue field)
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
        HashMap<String, String> glossaryMap;
        questionByGenre =  (HashMap<String, Question>[]) Array.newInstance(HashMap.class, Attribute.values().length);

        //in questa sezione vengono definite le varie domande e i glossari
        Question mainGamePurpose = new Question("main-game-purpose", "Per te il gioco e' fonte di... ?", null, "relax", "sfida", "divertimento");
        Question patience = new Question("patience", "Valuta il tuo grado di pazienza", null, "alto", "medio", "basso");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("gdr", "gioco di ruolo");
        glossaryMap.put("fps", "first-person shooter (sparatutto in prima persona)");
        glossaryMap.put("rts", "Real-Time Strategy (strategia in tempo reale)");
        glossaryMap.put("4X", "eXplore, eXpand, eXploit, eXterminate; uno strategico basato sulla gestione militare, economica, politica, culturale e diplomatica di un impero");
        glossaryMap.put("turn-based-strategy", "gioco di strategia a turni; i giocatori non giocano contemporaneamente ma alternando diversi turni in cui effettuano le loro mosse");
        glossaryMap.put("platformer", "videogiochi dove la meccanica di gioco implica l'attraversamento di livelli costituiti da piattaforme a volte disposte su piu' piani");
        glossaryMap.put("puzzle-game", "videogioco caratterizzato prevalentemente dalla presenza di enigmi e/o puzzle da risolvere");
        glossaryMap.put("action", "videogioco le cui meccaniche di gioco si basano prevalentemente su movimenti agili e sul combattimento");
        glossaryMap.put("picchiaduro", "videogioco le cui meccaniche sono basate prevalentemente sul combattimento fra personaggi, generalmente in una arena");
        glossaryMap.put("punta-e-clicca", "gioco in cui il giocatore deve dare ordini al personaggio cliccando su determinate parti sensibili dela visuale.\nI giochi di questo tipo hanno generalmente una forte componente narrativa");
        glossaryMap.put("interactive-fiction", "videogioco in cui il giocatore deve dare ordini digitando comandi da tastiera.\nGiochi di questo tipo hanno generalmente una forte componente narrativa");
        glossaryMap.put("avventura-grafica", "videogioco molto simile ad un film interattivo.\n L'interattivita' e' limitata rispetto agli altri generi, ma � comunque presente, a vantaggio di una forte componente narrativa e una trama elaborata");

        Question favouriteGenre = new Question("favourite-genre", "Scegliere il genere preferito in assoluto fra quelli elencati:", "gaming-experience is alto", glossaryMap,
                "gdr", "fps", "rts", "4X", "turn-based-strategy", "platformer", "puzzle-game", "action",
                "picchiaduro", "punta-e-clicca", "interactive-fiction", "avventura-grafica");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("coerenza", "trama senza contraddizioni interne");
        glossaryMap.put("linearita'", "trama che procede linearmente dal principio degli eventi fino alla loro conclusione, senza eccessivi flashback e anticipazioni");
        glossaryMap.put("non-linearita'", "trama caratterizzata da una narrazione senza uno specifico ordine fra gli eventi, con numerosi flashback, anticipazioni.\n La narrazione pu� anche cominciare da eventi che non rappresentano il principio della storia.");
        glossaryMap.put("paradossi", "trama che sfrutta vari paradossi, come i paradossi temporali (viaggi nel tempo ad esempio), paradossi sulla rappresentazione dello spazio e altro");
        glossaryMap.put("imprevedibilita'", "trama i cui eventi sono difficili da prevedere");

        Question userPlotFeature = new Question("user-plot-feature", "Quale di queste feature e' di maggiore importanza per la trama?",
                "plot-quality is importante", glossaryMap, "coerenza", "linearita'", "non-linearita'", "paradossi", "imprevedibilita'");
        Question plotQuality = new Question("plot-quality", "Il livello di qualita' della trama e'... ?", null, "importante", "irrilevante");
        Question gamingExperience = new Question("gaming-experience", "Qual e' il tuo livello di esperienza con i videogioco?", null, "alto", "medio", "basso");
        Question userLearningAttitude = new Question("user-learning-attitude", "Riesci ad apprendere velocemente nuove informazioni e nuove meccaniche di funzionamento del gioco?", null,
                "si", "no", "non-so");
        Question userAudioQuality = new Question("user-audio-quality", "All'incirca quante colonne sonore tratte da videogiochi ricordi?", null, "meno-di-10", "fra-10-e-20", "piu'-di-20");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("realismo", "grafica fotorealistica, ossia il piu' possibile simile alla realta'");
        glossaryMap.put("funzionale", "grafica non necessariamente fotorealistica, ma comunque adatta alle caratteristiche del gioco");
        Question graphicsDetailQuality = new Question("graphics-detail-quality", "Per quanto riguarda la grafica, cosa e' piu' importante, il realismo o la grafica funzionale al contesto di gioco?", null, glossaryMap, "realismo", "funzionale");

        Question aiImplementation = new Question("ai-implementation", "Dai molto peso alla reattivita' dei nemici in gioco e alle loro strategie utilizzate (\"intelligenza\" usata per ostacolarti)?", "gaming-experience is alto", "si", "no", "non-so");
        Question attitude = new Question("attitude", "Quale fra questi e' il tuo principale strumento per risolvere le situazioni critiche?", null, "intelligenza", "furbizia", "forza-bruta", "mix");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("open-world", "scenario in cui e' possibile muoversi liberamente; c'e' una unica mappa con un unico confine, i livelli non sono chiusi\ne delimitati in una specifica area");
        glossaryMap.put("closed-world", "scenario in cui i movimenti del giocatore sono limitati e i livelli sono strutturati in una specifica area di gioco");

        Question favouriteWorldBuildExpert = new Question("favourite-world-build-expert", "Per quanto riguarda l'ambiente di gioco, cosa preferisci fra le seguenti alternative?", "gaming-experience is alto", glossaryMap, "open-world", "closed-world", "indifferente");
        Question favouriteWorldBuildIntermediate = new Question("favourite-world-build-intermediate", "Ti piacciono livelli con ambientazione ben delimitata o libera da esplorare?", "gaming-experience is medio", "delimitata", "libera");
        Question favouriteWorldBuildNovice = new Question("favourite-world-build-novice", "Ti piace esplorare vaste ambientazioni?", "gaming-experience is basso", "si", "no");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("frenetico", "meccaniche di gioco che richiedono una veloce risposta e reazione da parte del giocatore, mettendone alla prova i riflessi");
        glossaryMap.put("bilanciato", "meccaniche di gioco basate su un compromesso fra una tempestiva reazione dell'utente e la possibilita' di elaborare i piani d'azione con piu' calma");
        glossaryMap.put("lento", "meccaniche di gioco basate sull'attenta pianificazione delle interazioni dell'utente, le quali devono solitamente essere strategiche.\nNe consegue che il gioco permette al giocatore di prendersi il tempo per riflettere");
        Question gameplayStyle = new Question("gameplay-style", "Selezionare lo stile di gioco preferito", null, glossaryMap, "frenetico", "bilanciato", "lento");

        //in questa sezione viene avvalorata la struttura dati che mappa gli attributi target alle varie domande che li riguardano
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put(mainGamePurpose.getKeyword(), mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put(patience.getKeyword(), patience);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put(favouriteGenre.getKeyword(), favouriteGenre);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put(userPlotFeature.getKeyword(), userPlotFeature);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put(plotQuality.getKeyword(), plotQuality);

        questionByGenre[Attribute.valueOf(Attribute.BEST_DIFFICULTY)] =  new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_DIFFICULTY)].put(gamingExperience.getKeyword(), gamingExperience);

        questionByGenre[Attribute.valueOf(Attribute.BEST_LEARNING_CURVE)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_LEARNING_CURVE)].put(patience.getKeyword(), patience);
        questionByGenre[Attribute.valueOf(Attribute.BEST_LEARNING_CURVE)].put(userLearningAttitude.getKeyword(), userLearningAttitude);

        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)].put(mainGamePurpose.getKeyword(), mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)].put(plotQuality.getKeyword(), plotQuality);
        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)].put(favouriteGenre.getKeyword(), favouriteGenre);

        questionByGenre[Attribute.valueOf(Attribute.BEST_AUDIO)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_AUDIO)].put(userAudioQuality.getKeyword(), userAudioQuality);

        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)].put(graphicsDetailQuality.getKeyword(), graphicsDetailQuality);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)].put(favouriteGenre.getKeyword(), favouriteGenre);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)].put(gamingExperience.getKeyword(), gamingExperience);

        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put(aiImplementation.getKeyword(), aiImplementation);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put(favouriteGenre.getKeyword(), favouriteGenre);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put(mainGamePurpose.getKeyword(), mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put(attitude.getKeyword(), attitude);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put(patience.getKeyword(), patience);

        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)].put(favouriteWorldBuildExpert.getKeyword(), favouriteWorldBuildExpert);
        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)].put(favouriteWorldBuildIntermediate.getKeyword(), favouriteWorldBuildIntermediate);
        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)].put(favouriteWorldBuildNovice.getKeyword(), favouriteWorldBuildNovice);

        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)].put(gameplayStyle.getKeyword(), gameplayStyle);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)].put(mainGamePurpose.getKeyword(), mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)].put(favouriteGenre.getKeyword(), favouriteGenre);
    }


    private void removeQuestion(String key)
    {
        for(Attribute a : Attribute.values())
        {
            if(questionByGenre[Attribute.valueOf(a)].containsKey(key))
              questionByGenre[Attribute.valueOf(a)].remove(key);
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

    void interact()
    {
        MutableInteger aIndex = new MutableInteger();
        Question q = null;
        boolean precursorSatisfied = false;
        MultifieldValue mv = null;
        CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
        boolean repeatCycle;
        boolean ask = false;

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

                assertAttribute(q.getKeyword(), answer);

                //debug
                //System.out.println(clips.eval("(get-focus-stack)").toString());
                //System.out.println(clips.eval("(facts CHOOSE-FEATURES)").toString());

                clips.run();
                //debug
                clips.eval(("(focus RULES VIDEOGAMES)"));

                //debug
                //System.out.println(clips.eval("(facts MAIN)").toString());
                //System.out.println(clips.eval("(facts CHOOSE-FEATURES)").toString());
                //System.out.println(clips.eval("(get-focus-stack)").toString());

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
                repeatCycle = false;

        }while(repeatCycle);

    }

    private void printSuggestions(PrintStream str)
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

    private void assertAttribute(String name, String value)
    {
        String toAssert = "(attribute (name " + name + ") (value " + value + "))";
        clips.assertString(toAssert);
    }

    public static void main(String[]  args)
    {
        VgRecSystem rec = new VgRecSystem();
        //System.out.println(rec.clips.eval("(facts)").toString());
        //System.out.println(rec.clips.eval("(get-focus)").toString());
        //System.out.println(rec.clips.eval("(get-deftemplate-list)").toString());
        rec.interact();
        rec.printSuggestions(System.out);
        //Question test line: System.out.println(rec.questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].get("patience"));
    }

}