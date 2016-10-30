package seedu.task.logic.commands;

//@@author A0139958H

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.parser.TaskParser;
import seedu.task.logic.parser.UpdateTaskParser;
import seedu.task.model.ModelManager;
import seedu.task.model.VersionControl;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskVersion;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;

public class UpdateCommand extends Command{
    public static final String COMMAND_WORD = "update";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 by Sunday";

    public static final String MESSAGE_SUCCESS = "Updated task %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the taskBook";

    private final int taskIndex;
    private final String updateArgs;

    
    /**
     * @param: Task Index, Arguments to be updated
     */
    public UpdateCommand(int taskIndex, String updateArgs) {
    	this.taskIndex = taskIndex;
    	this.updateArgs = updateArgs;
    }
    
    /**
     * Updates the specified task object in the taskBook. Saves it in version control for possible undo/redo operations
     * @throws DateClashTaskException if task happens between another task's start and end dates
     * @throws IllegalValueException if an invalid command is entered.
     */
    @Override
    public CommandResult execute() {
    	LogsCenter.getLogger(ModelManager.class).info("Task Index: " + taskIndex + " Args: " + updateArgs);
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getSortedTaskList();

        if (lastShownList.size() < taskIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    	
        assert model != null;
        try {
            Task toUpdate = ((Task)lastShownList.get(taskIndex - 1)).clone();
            TaskParser updateTaskParser = new UpdateTaskParser((Task)lastShownList.get(taskIndex - 1), updateArgs);
        	model.updateTask(model.getTaskByIndex(taskIndex - 1), updateTaskParser.parseInput());
            VersionControl.getInstance().push(new TaskVersion(VersionControl.getInstance().getIndex() + 1, taskIndex - 1, toUpdate, TaskVersion.Command.UPDATE));
            VersionControl.getInstance().resetVersionPosition();

        } catch (UniqueTaskList.DateClashTaskException e) {
            return new CommandResult(e.getMessage());
		} catch (IllegalValueException e) {
	        indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(e.getMessage());
		}

        return new CommandResult(String.format(MESSAGE_SUCCESS, taskIndex));
    }
}
