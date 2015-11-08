package VgExceptions;

import Utils.MutableInteger;

/**
 * Modella l'eccezione usata quando non è possibile proporre la domanda selezionata all'utente perché la risposta ad una domanda
 * che funge da prerequisito è diversa da quella necessaria.
 * @see Questions.Question
 * @see VgRecMain.VgRecSystem#selectQuestion(MutableInteger)
 */
public class CannotAskException extends Exception {

    public CannotAskException() {}

    public CannotAskException(String message)
    {
        super(message);
    }
}
