package Security;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Classe usata per le operazioni di autenticazione dell'utente, quindi per la registrazione e il login.
 * Sfrutta quanto definito nella classe PasswordHash, in modo da memorizzare i dati delle password in salted hashing,
 * e un file chiamato db.dat (se non presente, verrà creato automaticamente).
 * @see PasswordHash
 */
public class Authentication {

    public static final String DB_FILENAME = "db.dat";

    /**
     * Controlla se esiste già un utente con un determinato username nel file di registrazione
     * @param file          classe con i riferimenti al file usato per memorizzare gli utenti registrati
     * @param username      username dell'utente
     * @return              true se l'username è presente nel file, false altrimenti
     */
    private static boolean userExist(File file, String username)
    {
        String str;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while((str = br.readLine()) != null)
            {
                String[] parts = str.split(" ");

                if(parts[0].equals(username))
                    return true;
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Aggiunge nel file delle registrazioni un utente con username e password forniti come parametri
     * @param username      username usato dall'utente
     * @param password      password usata dall'utente
     * @return              1 se l'operazione si è conclusa con successo, -1 in caso di errore
     */
    public static int addUser(String username, String password)
    {
        File file = new File("./" + DB_FILENAME);

        try {

            if(!file.exists())
                file.createNewFile();

            if(!userExist(file, username))
            {
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pw = new PrintWriter(fw, true);

                String saltedHashedPwd = PasswordHash.createHash(password);
                String userData = username + " " + saltedHashedPwd;

                pw.println(userData);
                pw.close();

                return 1; //success code
            }

            return 0; //code for "user-exist" message

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return -1; //error
    }

    /**
     * Funzione usata per le operazioni di login, ossia per verificare che i dati inseriti dall'utente siano presenti a quanto
     * già presente nel file delle registrazioni.
     * @param username      username da verificare
     * @param password      password da verificare
     * @return              true se esiste nel file delle registrazioni un utente con i dati forniti come parametri, false altrimenti
     */
    public static boolean validateLogin(String username, String password)
    {
        File file = new File("./" + DB_FILENAME);
        boolean retVal = false;

        if(file.exists())
        {
            try {

                String str;
                BufferedReader br = new BufferedReader(new FileReader(file));

                while((str = br.readLine()) != null)
                {
                    String[] parts = str.split(" ");

                    if(parts[0].equals(username))
                       retVal = PasswordHash.validatePassword(password, parts[1]);
                }

                br.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }
}
