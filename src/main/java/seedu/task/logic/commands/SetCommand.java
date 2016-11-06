package seedu.task.logic.commands;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.parser.TaskParser;
import seedu.task.logic.parser.UpdateTaskParser;
import seedu.task.model.ModelManager;
import seedu.task.model.VersionControl;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskVersion;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;

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
     *@param Task Index, Arguments to be updated
     */
    public SetCommand(int taskIndex, String setArg) {
    	this.taskIndex = taskIndex;
    	this.setArg = setArg;
    }

    /**
     * Sets the status of task to done or ignore
     * @throws IllegalValueException if an invalid status is entered in the input. 
     */
    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getSortedTaskList();

        if (lastShownList.size() < taskIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    	
        assert model != null;
        try {
            Task toUpdate = model.getTaskByIndex(taskIndex - 1).clone();
        	TaskParser updateTaskParser = new UpdateTaskParser(model.getTaskByIndex(taskIndex - 1), setArg);
            int updatedTaskIndex = model.updateTask(model.getTaskByIndex(taskIndex - 1), ((UpdateTaskParser) updateTaskParser).setTaskStatus());
        	VersionControl.getInstance().push(new TaskVersion(VersionControl.getInstance().getIndex() + 1, updatedTaskIndex, toUpdate, model.getTaskByIndex(updatedTaskIndex), TaskVersion.Command.UPDATE));
            VersionControl.getInstance().resetVersionPosition();
            //@@author A0138301U
            EventsCenter.getInstance().post(new JumpToListRequestEvent(updatedTaskIndex));
            //@@author A0139958H
		} catch (IllegalValueException e) {
	        indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(e.getMessage());
		}

        return new CommandResult(String.format(MESSAGE_SUCCESS, taskIndex));
    }
}
