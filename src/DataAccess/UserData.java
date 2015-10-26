package DataAccess;

import java.io.*;

/**
 * Created by VitoVincenzo on 24/10/2015.
 */
public class UserData {

    private String username;
    private File file;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public UserData(String name)
    {
        username = name;

        file = openUserData();

        try
        {
            bufferedReader = new BufferedReader(new FileReader(file));
            printWriter = new PrintWriter(new FileWriter(file, true), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername()
    {
        return username;
    }

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

    public boolean isEmpty()
    {
        return file.length() == 0;
    }

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
}
