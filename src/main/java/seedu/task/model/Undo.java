package seedu.task.model;

import seedu.task.model.task.Task;

public class Undo {
	public enum UndoCommand { ADD, UPDATE, DELETE, DEFAULT };
	
	private static Undo instance;
	private int taskIndex;
	private Task task;
	private UndoCommand command;
	
	private Undo() {
		this.command = UndoCommand.DEFAULT;
	};
	
	public static Undo getInstance() {
		if (instance == null)
			instance = new Undo();
		return instance;
	}
	
	public void setUndo(int taskIndex, Task undoTask, UndoCommand undoCommand) {
		setTaskIndex(taskIndex);
		setTask(undoTask);
		setCommand(undoCommand);
	}

	public int getTaskIndex() {
		return taskIndex;
	}

	public void setTaskIndex(int taskIndex) {
		this.taskIndex = taskIndex;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task undoTask) {
		this.task = undoTask;
	}

	public UndoCommand getCommand() {
		return command;
	}

	public void setCommand(UndoCommand undoCommand) {
		this.command = undoCommand;
	}
	
}
