package DataAccess;

import java.io.*;

/**
 * Classe contenente i dati di uno specifico utente e che serve a modellare i metodi per accedere al profilo
 * dell'utente, compreso il profilo psicologico salvato dopo il termine di una sessione d'uso del software o dopo aver
 * richiesto di uscire mentre è ancora in corso la fase di risposta alle domande.
 */
public class UserData {

    private String username;
    private File file;
    private BufferedReader bufferedReader;
    private FileInputStream fIn;
    private PrintWriter printWriter;

    /**
     * Costruttore che inizializza i dati e i riferimenti alle risorse contenenti i dati dell'utente il cui username è quello fornito come parametro
     * @param name      username dell'utente
     */
    public UserData(String name)
    {
        username = name;

        file = openUserData();

        try
        {
            fIn = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fIn));
            printWriter = new PrintWriter(new FileWriter(file, true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Costruttore da usare per l'utente Ospite (non registrato e/o non loggato): tutti i dati sono null
     */
    public UserData()
    {
        username = null;
        file = null;
        fIn = null;
        bufferedReader = null;
        printWriter = null;
    }

    /**
     * Restituisce l'username
     * @return      username dell'utente
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Apre il file contenente il profilo utente, se esso non esiste, lo crea.
     * @return      riferimento alla specifica istanza {@link File} che contiene i dati per accedere al file del profilo utente
     */
    private File openUserData()
    {
        File file = new File("Data/" + username + ".dat");

        if(!file.getParentFile().exists())
            file.getParentFile().mkdir();

        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return file;
    }

    /**
     * Chiude i buffer usati per scrivere e leggere sul file del profilo utente
     */
    public void closeUserData()
    {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printWriter.close();
    }

    /**
     * Legge una riga dal file del profilo utente
     * @return  riga corrente del file del profilo utente
     * @throws IOException
     */
    public String readLine() throws IOException
    {
        return bufferedReader.readLine();
    }


    /**
     * Scrive sul file del profilo utente una stringa passata come parametro
     * @param line      stringa da scrivere sul file del profilo utente
     */
    public void println(String line)
    {
        printWriter.println(line);
    }

    /**
     * Verifica se il file è vuoto
     * @return      true se il file è vuoto, false altrimenti
     */
    public boolean isEmpty()
    {
        return file.length() == 0;
    }

    /**
     * Se il file del profilo utente non è vuoto, ne cancella il contenuto.
     */
    public void clearFile()
    {
        if(!isEmpty())
        {
            try {
                PrintWriter pw = new PrintWriter( new FileWriter(file, false), true);
                pw.print("");
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Riposiziona il cursore di lettura all'inizio del file
     */
    public void readCursorToBegin()
    {
        try {
            fIn.getChannel().position(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedReader = new BufferedReader(new InputStreamReader(fIn));
    }
}
