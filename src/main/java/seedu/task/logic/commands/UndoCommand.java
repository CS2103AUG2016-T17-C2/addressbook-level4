package seedu.task.logic.commands;

import seedu.task.commons.core.LogsCenter;
import seedu.task.model.ModelManager;
import seedu.task.model.Undo;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoCommand extends Command {
	public static final String COMMAND_WORD = "undo";
	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo the last command ";
	public static final String MESSAGE_SUCCESS_UNDO_DELETE = "Successfully Added back last deleted task!";
	public static final String MESSAGE_SUCCESS_UNDO_ADD = "Successfully deleted task last added task!";
	public static final String MESSAGE_SUCCESS_UNDO_UPDATE = "Successfully updated task!";
	public static final String MESSAGE_FAILURE_UNDO = "Nothing to Undo!";

	public UndoCommand() {
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			Undo undo = Undo.getInstance();
			switch (undo.getCommand()) {
			case ADD:
				model.deleteTask(undo.getTask());
				undo.setCommand(Undo.UndoCommand.DEFAULT);
				return new CommandResult(MESSAGE_SUCCESS_UNDO_ADD);
			case DELETE:
				model.addTask(undo.getTask());
				undo.setCommand(Undo.UndoCommand.DEFAULT);
				return new CommandResult(MESSAGE_SUCCESS_UNDO_DELETE);
			case UPDATE:
				model.updateTask(undo.getTaskIndex(), undo.getTask());
				undo.setCommand(Undo.UndoCommand.DEFAULT);
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
