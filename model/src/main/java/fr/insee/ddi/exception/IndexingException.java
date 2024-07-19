package fr.insee.ddi.exception;

/**
 * Exception to be thrown if an unexpected exception occurs during DDI indexing.
 */
public class IndexingException extends RuntimeException {

    public IndexingException(String message) {
        super(message);
    }

    public IndexingException(String message, Exception e) {
        super(message, e);
    }

}
