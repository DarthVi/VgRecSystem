import DataAccess.UserData;
import Security.Authentication;
import TypeCheck.TypeCheckUtils;

import java.util.Scanner;

/**
 * Created by VitoVincenzo on 28/10/2015.
 */
public class MainMenu {

    public static int askForAuthentication()
    {
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("Come si desidare usare il sistema?");
        System.out.println("1) Registrazione: il sistema registrera' nome utente e password, per poi continuare\n" +
                "con il normale funzionamento. I dati utente verranno usati per salvere i file della profilazione\n" +
                "(dati di login, dati con i consigli prodotti).");
        System.out.println("2) Login: il sistema chieder� le credenziali dell'utente (username e password) per poi chiedere\n" +
                "se visualizzare i consigli della precedente sessione o sovrascriverli con dati provenienti da una nuova" +
                "sessione di domande fatte all'utente stesso");
        System.out.println("3) Ospite: il sistema non salver� alcun dato dell'utente");
        System.out.println("4) Esci e chiudi il programma");

        do {
            String userAnswer = sc.nextLine();

            if(TypeCheckUtils.isInteger(userAnswer))
                choice = Integer.parseInt(userAnswer);
            else
                choice = 0;

        }while(choice < 1 && choice > 4);

        return choice;
    }

    public static boolean userMenu(VgRecSystem rec, UserData ud, Scanner sc)
    {
        System.out.println("");
        System.out.println("1) Caricare i dati dei consigli prodotti nella precedente sessione");
        System.out.println("2) Eseguire una nuova interrogazione rispondendo nuovamente alle domande");
        System.out.println("3) Logout");
        int logChoice;

        do
        {
            logChoice = Integer.parseInt(sc.nextLine());
        }while(logChoice != 1 && logChoice != 2 && logChoice != 3);

        if(logChoice == 1)
        {
            rec.assertUserProfile(ud);
            rec.printSuggestions(System.out);
            return true;
        }
        else if(logChoice == 2)
        {
            rec.interact();
            rec.printSuggestions(System.out);

            ModAnswerProcessor.modifyAnswers(rec.askedQuestion, rec);

            rec.saveUserProfileToFile(ud);
            return true;
        }
        else
            return false;
    }

    public static void menu(VgRecSystem rec)
    {
        boolean exit = false;

        do {
            int choice = askForAuthentication();
            Scanner sc = new Scanner(System.in);
            String username = null;
            String password = null;

            switch (choice)
            {
                case 1:
                    System.out.println("Username: ");
                    username = sc.nextLine();
                    username.replace(" ", "");
                    System.out.println("Password: ");
                    password = sc.nextLine();
                    password.replace(" ", "");

                    if(Authentication.addUser(username, password) == 1)
                    {
                        System.out.println("Registrazione effettuata con successo!");

                        rec.interact();

                        rec.printSuggestions(System.out);

                        ModAnswerProcessor.modifyAnswers(rec.askedQuestion, rec);

                        UserData ud = new UserData(username);
                        rec.saveUserProfileToFile(ud);
                        ud.closeUserData();
                    }
                    else
                        System.out.println("Si � verificato un problema: username gi� esistente o errore di sistema.\n" +
                                "Chiudere il programma e riprovare con un altro username.");
                    break;

                case 2:
                    System.out.println("Username: ");
                    username = sc.nextLine();
                    username.replace(" ", "");
                    System.out.println("Password: ");
                    password = sc.nextLine();
                    password.replace(" ", "");

                    if(Authentication.validateLogin(username, password))
                    {
                        UserData ud = new UserData(username);
                        boolean stayInLoop;

                        do {
                            stayInLoop = userMenu(rec, ud, sc);
                        }while(stayInLoop);

                        ud.closeUserData();
                    }
                    else
                        System.out.println("Dati di login errati o errore di sistema. Chiudere l'applicazione e riprovare");

                    break;

                case 3:
                    rec.interact();
                    rec.printSuggestions(System.out);

                    ModAnswerProcessor.modifyAnswers(rec.askedQuestion, rec);
                    break;

                case 4:
                    exit = true;
                    break;

                default:
                    break;
            }
        }while(!exit);

    }
}