package seedu.task.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.model.Undo;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0139958H

/**
 * Deletes a task identified using it's last displayed index from the task book.
 */
public class DeleteCommand extends Command {

	public static final String COMMAND_WORD = "delete";

	public static final String MESSAGE_USAGE = COMMAND_WORD
	        + ": Deletes the tasks identified by their index number used in the last task listing.\n"
	        + "Parameters: INDEXES (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " 1 2";

	public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %s";

	public final int[] targetIndexes;

	public DeleteCommand(int[] targetIndexes) {
		this.targetIndexes = targetIndexes;
	}

	@Override
	public CommandResult execute() {

		UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getSortedTaskList();
		List<Integer> invalidIndexes = new ArrayList<>();

		for (int i = 0; i < targetIndexes.length; i++) {
			if (lastShownList.size() < targetIndexes[i])
				invalidIndexes.add(targetIndexes[i]);
		}

		if (!invalidIndexes.isEmpty()) {
			indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(String.format(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX,
			        Arrays.toString(invalidIndexes.toArray())));
		}

		try {
			for (int i = 0; i < targetIndexes.length; i++) {
				LogsCenter.getLogger(DeleteCommand.class).info("Delete Task: " + (targetIndexes[i] - i - 1));
				ReadOnlyTask taskToDelete = model.getTaskByIndex(targetIndexes[i] - i - 1);
				model.deleteTask(taskToDelete);
				Undo.getInstance().setUndo(targetIndexes[i] - i - 1, (Task) taskToDelete, Undo.UndoCommand.DELETE);
			}
		} catch (TaskNotFoundException tnfe) {
			assert false : "The target task cannot be missing";
		}

		return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, Arrays.toString(targetIndexes)));
	}

}
