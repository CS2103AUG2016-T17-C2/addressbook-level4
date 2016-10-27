package seedu.task.commons.exceptions;
//@@author A0141064U
/**
 * Signals that some change in config values are not saved.
 */
public class WrongConfigValuesException extends Exception {

    public WrongConfigValuesException(String message) {
        super(message);
    }
} 
