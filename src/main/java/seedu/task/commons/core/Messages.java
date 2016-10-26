package seedu.task.commons.core;

import seedu.task.logic.commands.SetCommand;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!";
    public static final String MESSAGE_INVALID_TASK_DISPLAYED_INDEX = "The task indexes %s are invalid";
    public static final String MESSAGE_TASKS_LISTED_OVERVIEW = "%1$d tasks listed!";
    public static final String MESSAGE_FAILURE_SET_COMMAND = MESSAGE_INVALID_COMMAND_FORMAT + "\n" + SetCommand.MESSAGE_USAGE;
}
