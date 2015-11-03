package Questions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VitoVincenzo on 02/11/2015.
 */
public class QuestionExplanation {

    private static Map<String, String> explanationMap = new HashMap<String, String>();

    static
    {
        explanationMap.put("main-game-purpose", "Le risposte a questa domanda sono utili per capire quali potrebbero essere i generi di gioco ideali\n" +
                           "per il giocatore, i tipi di trama che possono interessargli maggiormente, il livello di sfida che i giochi devono avere\n" +
                           "e le meccaniche di gioco piu' appropriate.");
        explanationMap.put("patience", "Il livello di pazienza dell'utente verra' tenuto in conto per determinare i generi di gioco appropriati (ad esempio generi come gli rts\n" +
                           "richiedono un livello di pazienza maggiore rispetto agli fps), per capire quali curve di apprendimento prendere in considerazione (giochi che possono essere godibili sin da subito,\n" +
                           "giochi che richiedono di apprendere gradualmente le meccaniche prima di poter essere compresi fino in fondo, ecc.), per determinare il livello di sfida appropriato e\n" +
                           "per individuare le migliori meccaniche di gioco adatte per l'utente.");
        explanationMap.put("favourite-genre", "Sapere qual è il genere di giochi preferito dall'utente è importante per determinare quasi tutte le caratteristiche dei videogiochi, oltre al genere stesso\n:" +
                           "il livello di difficoltà e la curva di apprendimento (ad esempio gli appassionati di rts sono solitamente più pazienti e cercano giochi con elevato grado di sfida, mentre gli appassionati di avventure grafiche\n" +
                           "sono interessati alla trama e meno propensi a dare peso al livello di sfida offerto),\n" +
                           "le caratteristiche che deve avere la trama (gli appassionati di giochi strategici ad esempio danno poco peso alla trama, diversamente da chi ama avventure grafiche e giochi di ruolo),\n" +
                           "la qualità grafica che devono avere i giochi consigliati, il modo in cui reagiscono i nemici e/o il modo in cui sono disposti gli ostacoli che vanno superati per raggiungere l'obiettivo di gioco.\n" +
                           "Inoltre il genere preferito dal giocatore può essere utile per individuare quali sono le meccaniche di gioco e le ambientazioni da lui preferite.");
        explanationMap.put("user-plot-feature", "La risposta verrà utilizzata per individuare le caratteristiche piu' appropriate per la trama e per capire quali generi possono interessare l'utente.");
        explanationMap.put("plot-quality", "Sapere quanto conta per l'utente la qualità della trama e' utile per capire i generi videoludici che piu' possono interessargli e per cercare di selezionare\n" +
                           "i giochi con il livello di qualita' appropriato.");
        explanationMap.put("gaming-experience", "Conoscere il livello di esperienza nel mondo videoludico serve per avere informazioni sui generi videoludici da considerare\n" +
                           "(un giocatore con piu' esperienza molto probabilmente conosce e aprezza anche generi meno popolari), sul livello di difficoltà desiderato (i giocatori con piu' esperienza\n" +
                           "tendenzialmente cercano un grado di sfida elevato, pur gradendo anche giochi a bassa difficoltà; i giocatori con meno esperienza potrebbero invece ritenere troppo frustranti\n" +
                           "i giochi il cui livello di difficolta' e' elevato) e sulla grafica.");
        explanationMap.put("user-learning-attitude", "La velocita' e facilita' con cui l'utente apprende le informazioni nuove serviranno a determinare le curve di apprendimento che piu' si addicono al suo stile di gioco.");
        explanationMap.put("user-audio-quality", "La risposta data a questa domanda serve per capire quale peso dare alla componente sonora di un videogioco.");
        explanationMap.put("ai-implementation", "La risposta data a questa domanda serve per capire quanto debbano essere impegnative le sfide che il giocatore deve superare per raggiungere gli obiettivi di gioco.");
        explanationMap.put("attitude", "Conoscere il modo in cui il giocatore affronta gli ostacoli incontrati e' utile per determinare il livello di sfida piu' appropriato ai suoi gusti e alcuni generi\nche potrebbe ritenere interessanti.");
        explanationMap.put("favourite-world-build-expert", "Questa domanda viene posta per comprendere come debba essere l'ambientazione ideale dei giochi consigliati. Le ambientazioni possono essere\n" +
                           "apparentemente illimitate e completamente esplorabili o organizzate in livelli dai limiti chiari e invalicabili.");
        explanationMap.put("favourite-world-build-intermediate", "Questa domanda viene posta per comprendere come debba essere l'ambientazione ideale dei giochi consigliati. Le ambientazioni possono essere\n" +
                           "apparentemente illimitate e completamente esplorabili o organizzate in livelli dai limiti chiari e invalicabili.");
        explanationMap.put("favourite-world-build-novice", "Questa domanda viene posta per comprendere come debba essere l'ambientazione ideale dei giochi consigliati. Le ambientazioni possono essere\n" +
                           "apparentemente illimitate e completamente esplorabili o organizzate in livelli dai limiti chiari e invalicabili.");
        explanationMap.put("gameplay-style", "Sapere qual e' la velocita' di gioco preferita dall'utente e' importante per determinare quali sono le meccaniche di gioco e i generi che potrebbero risultargli interessanti.");
    }

    public static String get(String key)
    {
        return explanationMap.get(key);
    }
}
