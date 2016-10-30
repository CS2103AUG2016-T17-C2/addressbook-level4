package seedu.task.logic.commands;

//@@author A0139958H

import seedu.task.commons.core.LogsCenter;
import seedu.task.model.ModelManager;
import seedu.task.model.VersionControl;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskVersion;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoCommand extends Command {
	public static final String COMMAND_WORD = "undo";
	public static final String MESSAGE_USAGE = COMMAND_WORD + ": VersionControl the last command ";
	public static final String MESSAGE_SUCCESS_UNDO_DELETE = "Successfully Added back last deleted tasks!";
	public static final String MESSAGE_SUCCESS_UNDO_ADD = "Successfully deleted last added task!";
	public static final String MESSAGE_SUCCESS_UNDO_UPDATE = "Successfully updated task!";
	public static final String MESSAGE_FAILURE_UNDO = "Nothing to undo!";

	VersionControl undo;
	
	public UndoCommand() {
		undo = VersionControl.getInstance();
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			LogsCenter.getLogger(UndoCommand.class).info("size: " + undo.getSize() + " versionPosition: " + undo.getVersionPosition());
			undo.logList();
			
			undo.setCommandCount();
			LogsCenter.getLogger(UndoCommand.class).info("versionPosition: " + undo.getVersionPosition() 
					+ " commandCount " + undo.getCommandCount() + " getSize " + undo.getSize());

			if ((undo.getCommandCount() - undo.getVersionPosition()) <= 0)
				return new CommandResult(MESSAGE_FAILURE_UNDO);
			
			undo.updateVersionPosition(1);
			TaskVersion undoTask = undo.get(undo.getSize() - undo.getVersionPosition());
			int undoIndex = undoTask.getVersionIndex();
			LogsCenter.getLogger(UndoCommand.class).info("versionPosition: " + undo.getVersionPosition() + " undoTask " + undoTask.toString());
			switch (undoTask.getCommand()) {
			case ADD:
				model.deleteTask(model.getTaskByIndex(undoTask.getTaskIndex()));
				return new CommandResult(MESSAGE_SUCCESS_UNDO_ADD);
			case DELETE:
				for (int i = undo.getSize() - undo.getVersionPosition(); i >= 0; i--) {
					undoTask = undo.get(i);
					LogsCenter.getLogger(UndoCommand.class).info("i: " + i + " versionIndex: " + undoTask.getVersionIndex() + " undoIndex: " + undoIndex);
					if (undoTask.getVersionIndex() == undoIndex) {
						model.addTask(undoTask.getTaskIndex(), undoTask.getTask());
						undo.updateVersionPosition(1);
					} else {
						undo.updateVersionPosition(-1);						
						break;		
					}
				}
				return new CommandResult(MESSAGE_SUCCESS_UNDO_DELETE);
			case UPDATE:
				Task temp = model.getTaskByIndex(undoTask.getTaskIndex()).clone();
				model.updateTask(model.getTaskByIndex(undoTask.getTaskIndex()), undoTask.getTask());
				TaskVersion cloneTask = new TaskVersion(undoTask.getVersionIndex(), undoTask.getTaskIndex(), temp, undoTask.getCommand());
				undo.swap((undo.getSize() - undo.getVersionPosition()), cloneTask);
				LogsCenter.getLogger(UndoCommand.class).info("cloneTask: " + cloneTask.toString() + " temp: " + temp.toString());

				return new CommandResult(MESSAGE_SUCCESS_UNDO_UPDATE);
			case DEFAULT:
				return new CommandResult(MESSAGE_FAILURE_UNDO);
			}				
				
		} catch (UniqueTaskList.DateClashTaskException e) {
			return new CommandResult(e.getMessage());
		} catch (TaskNotFoundException e) {
			assert false : "The target task cannot be missing";
			LogsCenter.getLogger(ModelManager.class).throwing(UndoCommand.class.getName(), "execute", e);
		} catch (DuplicateTaskException e) {
			return new CommandResult(AddCommand.MESSAGE_DUPLICATE_TASK);
		} catch (NullPointerException e) {
			return new CommandResult(MESSAGE_FAILURE_UNDO);
		}
		return new CommandResult(MESSAGE_FAILURE_UNDO);
	}
}
