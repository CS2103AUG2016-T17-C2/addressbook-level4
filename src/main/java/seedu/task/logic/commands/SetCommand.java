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

//@@author A0139958H

public class SetCommand extends Command{
    public static final String COMMAND_WORD = "set";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": set the task status to marked as done or ignore.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 done";

    public static final String MESSAGE_SUCCESS = "Updated the status of task %1$s";
    public static final String MESSAGE_STATUS_CONSTRAINT = "Task status can only be updated to Done or Ignore";
    public static final String MESSAGE_STATUS_DONE = "Task status has been already set to Done";
    
    private final int taskIndex;
    private final String setArg;

    
    /**
     * Parameter: Task Index, Arguments to be updated
     *
     * 
     */
    public SetCommand(int taskIndex, String setArg) {
    	this.taskIndex = taskIndex;
    	this.setArg = setArg;
    }

    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getSortedTaskList();

        if (lastShownList.size() < taskIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    	
        assert model != null;
        try {
        	TaskParser updateTaskParser = new UpdateTaskParser(model.getTaskByIndex(taskIndex - 1), setArg);
        	LogsCenter.getLogger(ModelManager.class).info("Task Index: " + (taskIndex - 1) + " task: " + model.getTaskByIndex(taskIndex - 1));

        	model.updateTask(model.getTaskByIndex(taskIndex - 1), ((UpdateTaskParser) updateTaskParser).setTaskStatus());
		} catch (IllegalValueException e) {
	        indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(e.getMessage());
		}

        return new CommandResult(String.format(MESSAGE_SUCCESS, taskIndex));
    }
}
