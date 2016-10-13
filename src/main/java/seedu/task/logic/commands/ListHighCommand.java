package seedu.task.logic.commands;


/**
 * Lists all tasks with HIGH priority in the task book to the user.
 */
public class ListHighCommand extends Command {

    public static final String COMMAND_WORD = "listhigh";

    public static final String MESSAGE_SUCCESS = "Listed all tasks with HIGH priority";

    public ListHighCommand() {}

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskListByHighPriority();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
