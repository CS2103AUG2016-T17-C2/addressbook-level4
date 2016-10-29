package seedu.task.logic.commands;

//@@author A0139958H

import seedu.task.commons.core.LogsCenter;
import seedu.task.model.ModelManager;
import seedu.task.model.Undo;
import seedu.task.model.task.UndoTask;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoCommand extends Command {
	public static final String COMMAND_WORD = "undo";
	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo the last command ";
	public static final String MESSAGE_SUCCESS_UNDO_DELETE = "Successfully Added back last deleted tasks!";
	public static final String MESSAGE_SUCCESS_UNDO_ADD = "Successfully deleted last added task!";
	public static final String MESSAGE_SUCCESS_UNDO_UPDATE = "Successfully updated task!";
	public static final String MESSAGE_FAILURE_UNDO = "Nothing to Undo!";

	public UndoCommand() {
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			Undo.getInstance().updateVersionPosition(1);
			Undo.getInstance().setCommandCount();

			if ((Undo.getInstance().getCommandCount() - Undo.getInstance().getVersionPosition()) < 0) {
				return new CommandResult(MESSAGE_FAILURE_UNDO);
			}
			
			UndoTask undoTask = Undo.getInstance().get(Undo.getInstance().getSize() - Undo.getInstance().getVersionPosition());
			int undoIndex = undoTask.getUndoIndex();
			LogsCenter.getLogger(UndoCommand.class).info(" undoTask " + undoTask.toString());
			switch (undoTask.getCommand()) {
			case ADD:
				model.deleteTask(model.getTaskByIndex(undoTask.getTaskIndex()));
				return new CommandResult(MESSAGE_SUCCESS_UNDO_ADD);
			case DELETE:
				for (int i = Undo.getInstance().getSize() - Undo.getInstance().getVersionPosition(); i >= 0; i--) {
					undoTask = Undo.getInstance().get(i);
					if (undoTask.getUndoIndex() == undoIndex) {
						model.addTask(undoTask.getTaskIndex(), undoTask.getTask());
						Undo.getInstance().updateVersionPosition(1);
					} else {
						Undo.getInstance().updateVersionPosition(-1);
						break;						
					}
				}
				return new CommandResult(MESSAGE_SUCCESS_UNDO_DELETE);
			case UPDATE:
				model.updateTask(model.getTaskByIndex(undoTask.getTaskIndex()), undoTask.getTask());
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
		}
		return new CommandResult(MESSAGE_FAILURE_UNDO);
	}
}
