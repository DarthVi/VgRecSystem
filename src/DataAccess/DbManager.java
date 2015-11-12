package DataAccess;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.*;

/**
 * Classe usata per gestire la connessione con il database locale contenente alcune informazioni sui videogiochi non inerenti
 * le operazioni di inferenza.
 */
public class DbManager {

    /**
     * Crea la connessione al database usato per memorizzare e leggere le informazioni secondarie sui videogiochi
     */
    public static Connection createDbConnection(String dbFileName)
    {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return c;
    }

    /**
     * Crea la tabella e inserisce i valori qualora essi non siano gi� presenti
     * @param conn      connessione al database
     */
    public static void createDbVgInfo(Connection conn)
    {
        Statement stmt = null;
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='VgInfoTable'";
        String plotTxt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if(!rs.isBeforeFirst())
            {
                sql =   "CREATE TABLE VgInfoTable " +
                        "(vgName VARCHAR(20) PRIMARY KEY NOT NULL, " +
                        "genre VARCHAR(60) NOT NULL, " +
                        "publisher VARCHAR(120), " +
                        "developer VARCHAR(60), " +
                        "plot TEXT)";

                stmt.executeUpdate(sql);

                plotTxt = "'Sei Booker DeWitt, assoldato per ritrovare Elizabeth, una ragazza con strani poteri\n " +
                        "che vive in una distopica città sulle nuvole governata da un dittatore.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Bioshock-Infinite', 'fps, action', '2K Games', 'Irrational Games', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Il tuo nome è Nilin, abile agente del movimento Errorista, che opera nella futuristica Neo Parigi del 2084.\n " +
                        "In questa città la potentissima industria Memorize controlla i ricordi delle persone. Dopo essere stata imprigionata,\n" +
                        "stanno per cancellarti la memoria, ma il leader degli Erroristi ti contatta e...'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Remember-Me', 'action, platformer', 'Capcom', 'DONTNOD', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Strategico in tempo reale che consiste nel guidare una civilt� attraverso le epoche classiche e medievali'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Age-of-Empires-II', 'rts', 'Microsoft', 'Ensemble Studios', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Strategico a turni che consiste nel guidare una civilt� attraversio le varie epoche.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Civilization-V', '4X, strategico a turni', '2K Games', 'Firaxis Games', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Strategico in tempo reale con componente tattica a turni, il tutto ambientato in una epoca in cui esplorare\n" +
                        "lo Spazio è possibile.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Sins-Of-Solar-Empire', '4X, rts, strategico a turni', 'Stardock Corporation, Kalypso Media', 'Ironclad Games Corporation', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Strategico a turni e sim game in cui gestisci la politica di famiglie reali medievali'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Crusader-Kings-2', '4X, strategico a turni', 'Paradox Interactive AB', 'Paradox Interactive AB', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Strategico in tempo reale su una guerra intergalattica fra umani, Protoss e Zerg...'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Starcraft-II', 'rts', 'Blizzard', 'Blizzard', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Sei in vacanza con degli amici su un arcipelago, ma qualcosa va storto, dei locali guidati da un folle fanatico\n" +
                        "rapiscono i tuoi amici e per ritrovarli devi diventare un guerriero.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Far-Cry-3', 'fps', 'Ubisoft', 'Ubisoft', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Sei un pistolero del far west, rivivi le tue avventure mentre le racconti in una taverna'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Call-of-Juarez-Gunslinger', 'fps', 'Ubisoft', 'Techland', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Sei Geralt of Rivia, un witcher, ossia un professionista nella caccia ai mostri.\n" +
                        "Ti viene affidato il compito di ritrovare Cirilla, braccata dalla Caccia Selvaggia,\n" +
                        "guerrieri proveniente da un altro mondo.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('The-Witcher-3-Wild-Hunt', 'gdr', 'CD Projekt Red', 'CD Projekt Red', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Sei un non-morto, vaghi nella medievaleggiante Lordran. Non sai altro, dovrai esplorare le ambientazioni\n" +
                        "per scoprire cosa fare.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Dark-Souls', 'gdr', 'Bandai Namco', 'From Software', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Osservi te stesso mentre commetti un omicidio, ma sai di non essere cosciente e di essere manipolato da altre entit�.\n" +
                        "Dovrai indagare per comprendere il tutto'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Fahrenheit', 'avventura grafica', 'Atari, Aspyr Media', 'Quantic Dream', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Il mondo in cui vivi viene distrutto, dovrai ricostruirlo, ma prima devi recuperare dei core.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Bastion', 'platformer', 'WB Games', 'Supergiant Games', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Sei alla guida di un manipolo di sopravvissuti durante una apocalisse zombie. Dovrai fare delle dure\n" +
                        "scelte durante le tue avventure.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('The-Walking-Dead', 'avventura grafica', 'Telltale Games', 'Telltale Games', " + plotTxt +")";
                stmt.executeUpdate(sql);

                plotTxt = "'Sei Jodie, una ragazza che fin dalla sua nascita è legata ad una entità. Rivivi 15 anni della vita di Jodie\n" +
                        "e le sue ultime vicende.'";
                sql =   "INSERT INTO VgInfoTable (vgName,genre,publisher,developer,plot) " +
                        "VALUES ('Beyond-Two-Souls', 'avventura grafica', 'Sony', 'Quantic Dream', " + plotTxt +")";
                stmt.executeUpdate(sql);

                stmt.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ritorna un'unica stringa contenente le informazioni su un videogioco specifico specificato dal secondo parametro.
     * Sfrutta la connessione al database specificata come primo parametro.
     * @param c         {@link Connection}
     * @param vgName    nome del gioco di cui si vogliono le informazioni
     * @return          informazioni del gioco richiesto
     */
    public static String getVgInfos(Connection c, String vgName)
    {
        Statement stmt = null;
        String retStr = null;

        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM VgInfoTable WHERE vgName=" + "'" + vgName + "'");

            String s =        "Titolo del gioco: " + rs.getString("vgName").replace("-"," ") + "\n" +
                            "Trama/informazioni: " + rs.getString("plot") + "\n" +
                            "Genere: " + rs.getString("genre") + "\n" +
                            "Publisher: " + rs.getString("publisher") + "\n" +
                            "Casa sviluppatrice: " + rs.getString("developer");

            byte[] array = s.getBytes("UTF-8");

            retStr = new String(array, Charset.forName("UTF-8"));

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return retStr;
    }
}
