package DataAccess;

import java.io.*;

/**
 * Created by VitoVincenzo on 24/10/2015.
 */
public class UserData {

    private String username;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    public UserData(String name)
    {
        username = name;

        File file = openUserData();

        try
        {
            bufferedReader = new BufferedReader(new FileReader(file));
            printWriter = new PrintWriter(new FileWriter(file, false), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File openUserData()
    {
        File file = new File("Data/" + username + ".dat");

        if(!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return file;
    }

    public void closeUserData()
    {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printWriter.close();
    }

    public String readLine() throws IOException
    {
        return bufferedReader.readLine();
    }

    public void println(String line)
    {
        printWriter.println(line);
    }
}
