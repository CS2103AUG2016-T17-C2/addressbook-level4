package seedu.task.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.parser.TaskParser;
import seedu.task.model.VersionControl;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;


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
     * Parameter: Task Object
     *
     * 
     */
    public AddCommand(Task task) {
    	if (task == null)
    		throw new NullPointerException();
    	this.toAdd = task;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            int taskIndex = model.addTask(toAdd);
            VersionControl.getInstance().push(new TaskVersion(VersionControl.getInstance().getIndex() + 1, taskIndex, toAdd, TaskVersion.Command.ADD));
            VersionControl.getInstance().resetVersionPosition();
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        } catch (UniqueTaskList.DateClashTaskException e) {
            return new CommandResult(e.getMessage());
        } 
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}