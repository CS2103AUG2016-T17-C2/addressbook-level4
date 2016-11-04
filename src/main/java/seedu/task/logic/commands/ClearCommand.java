package seedu.task.logic.commands;

import seedu.task.model.TaskBook;
import seedu.task.model.VersionControl;

/**
 * Clears the task book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Task book has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(TaskBook.getEmptyTaskBook());
        VersionControl.getInstance().reset();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
