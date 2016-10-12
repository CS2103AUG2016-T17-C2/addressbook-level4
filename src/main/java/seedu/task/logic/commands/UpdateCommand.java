package seedu.task.logic.commands;

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.parser.TaskParser;
import seedu.task.logic.parser.UpdateTaskParser;
import seedu.task.model.ModelManager;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

public class UpdateCommand extends Command{
    public static final String COMMAND_WORD = "update";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 by Sunday";

    public static final String MESSAGE_SUCCESS = "Updated task %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the taskBook";

    private final int taskIndex;
    private final String updateArgs;

    
    /**
     * Parameter: Task Index
     *
     * 
     */
    public UpdateCommand(int taskIndex, String updateArgs) {
    	this.taskIndex = taskIndex;
    	this.updateArgs = updateArgs;
    }

    @Override
    public CommandResult execute() {
    	LogsCenter.getLogger(ModelManager.class).info("Task Index: " + taskIndex + " Args: " + updateArgs);
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredPersonList();

        if (lastShownList.size() < taskIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    	
        assert model != null;

        try {
        	TaskParser updateTaskParser = new UpdateTaskParser((Task)lastShownList.get(taskIndex - 1), updateArgs);
            model.deletePerson(lastShownList.get(taskIndex - 1));
        	model.addTask(updateTaskParser.parseInput());
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target person cannot be missing";
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
		} catch (IllegalValueException e) {
			new IncorrectCommand(e.getMessage());
		}

        return new CommandResult(String.format(MESSAGE_SUCCESS, taskIndex));
    }
}
