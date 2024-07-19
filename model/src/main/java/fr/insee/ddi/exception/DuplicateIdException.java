package fr.insee.ddi.exception;

/**
 * Exception to be thrown if duplicate identifiers are detected in a DDI object.
 */
public class DuplicateIdException extends RuntimeException {

    public DuplicateIdException(String message) {
        super(message);
    }

}
