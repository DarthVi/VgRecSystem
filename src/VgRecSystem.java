/**
 * Created by Vito Vincenzo Covella on 16/08/2015.
 */

import net.sf.clipsrules.jni.*;


import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;
import VgExceptions.PrecursorException;

public class VgRecSystem
{
    Environment clips = null;
    /*final static String EXIT_LOOP = "exit-loop";
    final static String TRUE = "TRUE";
    final static String FALSE = "FALSE";*/
    final static int NUMBER_OF_QUESTIONS = 15;
    private boolean[] selected = new boolean[Attribute.values().length]; //default value in Java = false
    private Map<String, Question>[] questionByGenre;
    private int questionsCounter;

    VgRecSystem()
    {
        questionsCounter = 0;
        initQuestions();

        clips = new Environment();

        clips.load("videogamesRS.clp");
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
        Random rn = new Random();

        for(Attribute a : Attribute.values())
        {
            if(selected[Attribute.valueOf(a)] == false)
                allFalse.add(a);
        }

        int sel = rn.nextInt(allFalse.size());
        selected[sel] = true;

        return allFalse.get(sel);
    }

    private Question selectQuestionsFromAttributeIndex(int attributeIndex) throws PrecursorException
    {
        Random rn = new Random();
        List<Question> valList = null;

        do
        {
            valList = new ArrayList<Question>(questionByGenre[attributeIndex].values());

            for(Question qu : valList)
            {
                if(qu.getSelected() == true)
                    valList.remove(qu);
            }

            if(valList.isEmpty())
                attributeIndex = Attribute.valueOf(selectAttribute());
        }while(valList.isEmpty());


        int rndIndex = rn.nextInt(valList.size());
        Question chosenQ = valList.get(rndIndex);
        int mvSize = 0;

        if(chosenQ.getPrecursorText() != null)
        {
            CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
            String[] parts = chosenQ.getPrecursorText().split(" ");
            MultifieldValue mv;
            mv = query.findFactSet("(?a attribute)", "and (eq ?a:name " + parts[0] + ") (eq ?a:value " + parts[2] + ")");
            mvSize = mv.size();
        }


        if(chosenQ.getPrecursorText() == null || mvSize == 0)
        {
            chosenQ.setSelected(true);
            questionsCounter++;
            return chosenQ;
        }
        else
        {
            throw new PrecursorException(chosenQ.getPrecursorText());
        }

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
                float val = 1/(questionsCounter * 0.3f) * certainty;

                if(val > 0.8f)
                    return false;
                else
                    return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    private void flagAttributes(Question q)
    {
        for(Attribute a : Attribute.values())
        {
            if(questionByGenre[Attribute.valueOf(a)].containsValue(q))
                selected[Attribute.valueOf(a)] = true;
        }
    }

    private Question chooseQuestionFromKeyword(String keyword) throws NoSuchFieldException
    {

        for(Attribute a : Attribute.values())
        {
            if(questionByGenre[Attribute.valueOf(a)].containsKey(keyword))
            {
                questionsCounter++;
                return questionByGenre[Attribute.valueOf(a)].get(keyword);
            }
        }

        throw new NoSuchFieldException("String not found");
    }

    void initQuestions()
    {
        HashMap<String, String> glossaryMap;
        questionByGenre =  (HashMap<String, Question>[]) Array.newInstance(HashMap.class, Attribute.values().length);

        //in questa sezione vengono definite le varie domande e i glossari
        Question mainGamePurpose = new Question("Per te il gioco e' fonte di relax, sfida o divertimento?", null, "relax", "sfida", "divertimento");
        Question patience = new Question("Valuta il tuo grado di pazienza: alto, medio o basso?", null, "alto", "medio", "basso");

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
        glossaryMap.put("avventura-grafica", "videogioco molto simile ad un film interattivo.\n L'interattività è limitata rispetto agli altri generi, ma è comunque presente, a vantaggio di una forte componente narrativa e una trama elaborata");

        Question favouriteGenre = new Question("Scegliere il genere preferito in assoluto fra quelli elencati: gdr, fps, rts, 4X, turn-based-strategy, platformer, puzzle-game, action, picchiaduro, punta-e-clicca, interactive-fiction, avventura-grafica", "gaming-experience is elevato", glossaryMap,
                "gdr", "fps", "rts", "4X", "turn-based-strategy", "platformer", "puzzle-game", "action",
                "picchiaduro", "punta-e-clicca", "interactive-fiction", "avventura-grafica");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("coerenza", "trama senza contraddizioni interne");
        glossaryMap.put("linearita'", "trama che procede linearmente dal principio degli eventi fino alla loro conclusione, senza eccessivi flashback e anticipazioni");
        glossaryMap.put("non-linearita'", "trama caratterizzata da una narrazione senza uno specifico ordine fra gli eventi, con numerosi flashback, anticipazioni.\n La narrazione può anche cominciare da eventi che non rappresentano il principio della storia.");
        glossaryMap.put("paradossi", "trama che sfrutta vari paradossi, come i paradossi temporali (viaggi nel tempo ad esempio), paradossi sulla rappresentazione dello spazio e altro");
        glossaryMap.put("imprevedibilita'", "trama i cui eventi sono difficili da prevedere");

        Question userPlotFeature = new Question("Quale di queste feature e' di maggiore importanza per la trama? Coerenza, linearita', non-linearita', paradossi, imprevedibilita'",
                "plot-quality is importante", glossaryMap, "coerenza", "linearita'", "non-linearita'", "paradossi", "imprevedibilita'");
        Question plotQuality = new Question("Il livello di qualita' della trama e': importante, irrilevante?", null, "importante", "irrilevante");
        Question gamingExperience = new Question("Qual e' il tuo livello di esperienza con i videogioco? alto, medio, basso?", null, "alto", "medio", "basso");
        Question userLearningAttitude = new Question("Riesci ad apprendere velocemente nuove informazioni e nuove meccaniche di funzionamento del gioco?\n si, no, non-so", null,
                "si", "no", "non-so");
        Question userAudioQuality = new Question("Quante volte sei rimasto colpito da una buona colonna sonora? Tante, poche o mai?", null, "tante", "poche", "mai");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("realismo", "grafica fotorealistica, ossia il più possibile simile alla realtà");
        glossaryMap.put("funzionale", "grafica non necessariamente fotorealistica, ma comunque adatta alle caratteristiche del gioco");
        Question graphicsDetailQuality = new Question("Per quanto riguarda la grafica, cosa e' piu' importante, il realismo o la grafica funzionale al contesto di gioco? (risposte possibili: realismo, funzionale)", null, glossaryMap, "realismo", "funzionale");

        Question aiImplementation = new Question("Dai molto peso all'implementazione dell'intelligenza artificiale? si, no, non-so", "gaming-experience is elevato", "si", "no", "non-so");
        Question attitude = new Question("Quale fra questi e' il tuo principale strumento per risolvere le situazioni critiche: intelligenza, furbizia, forza-bruta, mix fra le precedenti abilita'", null, "intelligenza", "furbizia", "forza-bruta", "mix");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("open-world", "scenario in cui e' possibile muoversi liberamente; c'è una unica mappa con un unico confine, i livelli non sono chiusi\ne delimitati in una specifica area");
        glossaryMap.put("closed-world", "scenario in cui i movimenti del giocatore sono limitati e i livelli sono strutturati in una specifica area di gioco");

        Question favouriteWorldBuildExpert = new Question("Per quanto riguarda l'ambiente di gioco, cosa preferisci fra le seguenti alternative: open-world, closed-world, indifferente?", "gaming-experience is elevato", glossaryMap, "open-world", "closed-world", "indifferente");
        Question favouriteWorldBuildIntermediate = new Question("Ti piacciono livelli con ambientazione ben delimitata o libera da esplorare? delimitata, libera", "gaming-experience is normale", "delimitata", "libera");
        Question favouriteWorldBuildNovice = new Question("Ti piace esplorare vaste ambientazioni? si, no ", "gaming-experience is basso", "si", "no");

        glossaryMap = new HashMap<String, String>();
        glossaryMap.put("frenetico", "meccaniche di gioco che richiedono una veloce risposta e reazione da parte del giocatore, mettendone alla prova i riflessi");
        glossaryMap.put("bilanciato", "meccaniche di gioco basate su un compromesso fra una tempestiva reazione dell'utente e la possibilità di elaborare i piani d'azione con più calma");
        glossaryMap.put("lento", "meccaniche di gioco basate sull'attenta pianificazione delle interazioni dell'utente, le quali devono solitamente essere strategiche.\nNe consegue che il gioco permette al giocatore di prendersi il tempo per riflettere");
        Question gameplayStyle = new Question("Selezionare lo stile di gioco preferito: frenetico, bilanciato, lento", null, glossaryMap, "frenetico", "bilanciato", "lento");

        //in questa sezione viene avvalorata la struttura dati che mappa gli attributi target alle varie domande che li riguardano
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put("main-game-purpose", mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put("patience", patience);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put("favourite-genre", favouriteGenre);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put("user-plot-feature", userPlotFeature);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].put("plot-quality", plotQuality);

        questionByGenre[Attribute.valueOf(Attribute.BEST_DIFFICULTY)] =  new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_DIFFICULTY)].put("gaming-experience", gamingExperience);

        questionByGenre[Attribute.valueOf(Attribute.BEST_LEARNING_CURVE)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_LEARNING_CURVE)].put("patience", patience);
        questionByGenre[Attribute.valueOf(Attribute.BEST_LEARNING_CURVE)].put("user-learning-attitude", userLearningAttitude);

        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)].put("main-game-purpose", mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)].put("plot-quality", plotQuality);
        questionByGenre[Attribute.valueOf(Attribute.BEST_PLOT)].put("favourite-genre", favouriteGenre);

        questionByGenre[Attribute.valueOf(Attribute.BEST_AUDIO)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_AUDIO)].put("user-audio-quality", userAudioQuality);

        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)].put("graphics-detail-quality", graphicsDetailQuality);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)].put("favourite-genre", favouriteGenre);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GRAPHICS)].put("gaming-experience", gamingExperience);

        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put("ai-implementation", aiImplementation);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put("favourite-genre", favouriteGenre);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put("main-game-purpose", mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put("attitude", attitude);
        questionByGenre[Attribute.valueOf(Attribute.BEST_AI)].put("patience", patience);

        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)].put("favourite-world-build-expert", favouriteWorldBuildExpert);
        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)].put("favourite-world-build-intermediate", favouriteWorldBuildIntermediate);
        questionByGenre[Attribute.valueOf(Attribute.BEST_WORLD_DESIGN)].put("favourite-world-build-novice", favouriteWorldBuildNovice);

        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)] = new HashMap<String, Question>();
        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)].put("gameplay-style", gameplayStyle);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)].put("main-game-purpose", mainGamePurpose);
        questionByGenre[Attribute.valueOf(Attribute.BEST_GAMEPLAY)].put("favourite-genre", favouriteGenre);
    }


    void interact()
    {
        CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
        MultifieldValue mv;
        String keyword = null;

        do
        {
            Attribute a = selectAttribute();
            Question sQuestion = null;
            try
            {
                sQuestion = selectQuestionsFromAttributeIndex(Attribute.valueOf(a));
            }
            catch(PrecursorException e)
            {
                String precursor = e.getMessage();
                String[] parts = precursor.split(" ");
                keyword = parts[0];


                try {
                    sQuestion = chooseQuestionFromKeyword(parts[0]);
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                }
            }

            String answer = sQuestion.askQuestion(System.out, new Scanner(System.in));

            if(keyword == null)
                try {
                    keyword = findKeyFromValue(questionByGenre[Attribute.valueOf(a)], sQuestion);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            assertAttribute(keyword, answer);

            clips.run();

            //old line: mv = query.findFactSet("(?a attribute)", "and (eq ?a:name videogame) (> ?a:certainty 60.0)");
            mv = query.findFactSet("(?a attribute)", "eq ?a:name videogame");

            keyword = null;
        }while(hFunction(mv));
    }

    private void printSuggestions(PrintStream str)
    {
        CLEnvironmentQuery query = new CLEnvironmentQuery(clips);
        MultifieldValue mv = query.findFactSet("(?a attribute)", "eq ?a:name videogame");

        if(mv.size() != 0)
        {
            try
            {
                str.println("Ecco i giochi consigliati: ");
                for (int i = 0; i < mv.size(); i++) {
                    FactAddressValue fv = (FactAddressValue) mv.get(i);
                    float certRankingVal = ((NumberValue) fv.getFactSlot("certainty")).floatValue();
                    String gameName = ((LexemeValue) fv.getFactSlot("name")).lexemeValue();

                    str.println((i+1) + ") " + gameName + ": " + certRankingVal + "%");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void assertAttribute(String name, String value)
    {
        String toAssert = "(attribute (name " + name + ") (value " + value + "))";
        clips.assertString(toAssert);
    }

    private String findKeyFromValue(Map<String, Question> map, Question question) throws NoSuchFieldException
    {
        for(Map.Entry<String, Question> e : map.entrySet())
        {
            if (e.getValue().equals(question))
                return e.getKey();
        }

        throw new NoSuchFieldException("Keyword Not Found");
    }

    public static void main(String[]  args)
    {
        VgRecSystem rec = new VgRecSystem();
        System.out.println(rec.clips.eval("(facts)").toString());
        System.out.println(rec.clips.eval("(get-focus)").toString());
        System.out.println(rec.clips.eval("(get-deftemplate-list)").toString());
        //rec.interact();
        //rec.printSuggestions(System.out);
        //Question test line: System.out.println(rec.questionByGenre[Attribute.valueOf(Attribute.BEST_GENRE)].get("patience"));
    }

}