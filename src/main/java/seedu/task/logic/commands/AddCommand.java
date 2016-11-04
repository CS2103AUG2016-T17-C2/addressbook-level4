package seedu.task.logic.commands;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.model.VersionControl;
import seedu.task.model.task.*;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;


//@@author A0139958H
/**
 * Adds a task to the taskBook.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the taskBook. "
            + "Example: " + COMMAND_WORD
            + " shopping with friends tomorrow evening 7pm to 10pm #high #shopping #evening /n"
            + " # - For tags, priority level (#high, #medium, #low) /n"
            + " @ - Venue";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the taskBook";

    private final Task toAdd;

    /**
     * @param: Task Object
     */
    public AddCommand(Task task) {
    	if (task == null)
    		throw new NullPointerException();
    	this.toAdd = task;
    }

    /**
     * Adds the task object to taskBook. Saves it in version control for possible undo/redo operations
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     * @throws DateClashTaskException if task happens between another task's start and end dates
     * 
     */
    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            int taskIndex = model.addTask(toAdd);
            VersionControl.getInstance().push(new TaskVersion(VersionControl.getInstance().getIndex() + 1, taskIndex, toAdd, TaskVersion.Command.ADD));
            VersionControl.getInstance().resetVersionPosition();

            //@@author A0138301U
            EventsCenter.getInstance().post(new JumpToListRequestEvent(model.getIndexOfTask(toAdd)));
            //@@author A0139958H
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        } catch (UniqueTaskList.DateClashTaskException e) {
            return new CommandResult(e.getMessage());
        } 
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}