package VgExceptions;

import Utils.MutableInteger;

/**
 * Modella l'eccezione usata quando non è possibile usare la domanda selezionata perché non è ancora stata fatta la domanda
 * la cui risposta funge da prerequisito.
 * @see Questions.Question
 * @see VgRecMain.VgRecSystem#selectQuestion(MutableInteger)
 */
public class PrecursorException extends Exception {

    public PrecursorException() {}

    public PrecursorException(String message)
    {
        super(message);
    }
}
