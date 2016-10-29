package seedu.task.logic.commands;

import seedu.task.commons.core.LogsCenter;
import seedu.task.model.ModelManager;
import seedu.task.model.VersionControl;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskVersion;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0139958H

public class RedoCommand extends Command {
	public static final String COMMAND_WORD = "redo";
	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo the last undo command ";
	public static final String MESSAGE_SUCCESS_REDO_DELETE = "Successfully Added back last deleted tasks!";
	public static final String MESSAGE_SUCCESS_REDO_ADD = "Successfully deleted last added task!";
	public static final String MESSAGE_SUCCESS_REDO_UPDATE = "Successfully updated task!";
	public static final String MESSAGE_FAILURE_REDO = "Nothing to Redo!";

	VersionControl redo;
	
	public RedoCommand() {
		redo = VersionControl.getInstance();
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			if (redo.getVersionPosition() <= 0)
				return new CommandResult(MESSAGE_FAILURE_REDO);
			
			//LogsCenter.getLogger(UndoCommand.class).info("size: " + redo.getSize() + " versionPosition: " + redo.getVersionPosition());
			//redo.logList();
			redo.setCommandCount();
			TaskVersion redoTask = redo.get(redo.getSize() - redo.getVersionPosition());
			int redoIndex = redoTask.getVersionIndex();
			redo.updateVersionPosition(-1);
			LogsCenter.getLogger(UndoCommand.class).info("versionPosition: " + redo.getVersionPosition() + " undoTask " + redoTask.toString());
			switch (redoTask.getCommand()) {
			case DELETE:
				for (int i = redo.getSize() - (redo.getVersionPosition() + 1); i < redo.getSize(); i++) {
					redoTask = redo.get(i);
					if (redoTask.getVersionIndex() == redoIndex) {
						model.deleteTask(model.getTaskByIndex(redoTask.getTaskIndex()));
						redo.updateVersionPosition(-1);
					} else {
						redo.updateVersionPosition(1);
						break;						
					}
				}				
				return new CommandResult(MESSAGE_SUCCESS_REDO_ADD);
			case ADD:
				model.addTask(redoTask.getTaskIndex(), redoTask.getTask());
				return new CommandResult(MESSAGE_SUCCESS_REDO_DELETE);
			case UPDATE:
				TaskVersion cloneTask = new TaskVersion(redoTask.getVersionIndex(), redoTask.getTaskIndex(), model.getTaskByIndex(redoTask.getTaskIndex()), redoTask.getCommand());
				TaskVersion toUpdate = redo.swap(redoTask.getTaskIndex(), cloneTask);
				model.updateTask(model.getTaskByIndex(redoTask.getTaskIndex()), toUpdate.getTask());
				return new CommandResult(MESSAGE_SUCCESS_REDO_UPDATE);
			case DEFAULT:
				return new CommandResult(MESSAGE_FAILURE_REDO);
			}				
				
		} catch (UniqueTaskList.DateClashTaskException e) {
			return new CommandResult(e.getMessage());
		} catch (TaskNotFoundException e) {
			assert false : "The target task cannot be missing";
			LogsCenter.getLogger(ModelManager.class).throwing(UndoCommand.class.getName(), "execute", e);
		} catch (DuplicateTaskException e) {
			return new CommandResult(AddCommand.MESSAGE_DUPLICATE_TASK);
		} catch (NullPointerException e) {
			return new CommandResult(MESSAGE_FAILURE_REDO);
		}
		return new CommandResult(MESSAGE_FAILURE_REDO);
	}
}
