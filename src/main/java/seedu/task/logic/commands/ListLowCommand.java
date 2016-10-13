package seedu.task.logic.commands;


/**
 * Lists all tasks with LOW priority in the task book to the user.
 */
public class ListLowCommand extends Command {

    public static final String COMMAND_WORD = "listlow";

    public static final String MESSAGE_SUCCESS = "Listed all tasks with LOW priority";

    public ListLowCommand() {}

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskListByLowPriority();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
