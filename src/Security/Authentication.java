package Security;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by VitoVincenzo on 23/10/2015.
 */
public class Authentication {

    public static final String DB_FILENAME = "db.txt";

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
