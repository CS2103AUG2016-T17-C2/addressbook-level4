package seedu.task.model.task;

import java.util.StringJoiner;

//@@author A0139958H

public class UndoTask implements Comparable<UndoTask> {
	public enum Command { ADD, UPDATE, DELETE, DEFAULT };
	private Integer undoIndex;
	private Integer taskIndex;
	private Task task;
	private Command command;
	
	
	public UndoTask() {}
	
	public UndoTask(int undoIndex, int taskIndex, Task task, Command command) {
		this.undoIndex = undoIndex;
		this.taskIndex = taskIndex;
		this.task = task;
		this.command = command;
	}
	
	public Integer getTaskIndex() {
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

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Integer getUndoIndex() {
		return undoIndex;
	}

	public void setUndoIndex(int undoIndex) {
		this.undoIndex = undoIndex;
	}

	@Override
	public int compareTo(UndoTask u) {
		if (!this.getUndoIndex().equals(u.getUndoIndex()))
			return this.getUndoIndex() - u.getUndoIndex();
		else
			return this.getTaskIndex() - u.getTaskIndex();
	}
	
	@Override
	public String toString() {
		StringJoiner str = new StringJoiner(" ");
		return str.add(undoIndex.toString()).add(taskIndex.toString()).add(task.toString()).add(command.name()).toString();
	}
}
