package seedu.task.logic.commands;


/**
 * Lists all tasks with MEDIUM priority in the task book to the user.
 */
public class ListMediumCommand extends Command {

    public static final String COMMAND_WORD = "listmedium";

    public static final String MESSAGE_SUCCESS = "Listed all tasks with MEDIUM priority";

    public ListMediumCommand() {}

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskListByMediumPriority();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
