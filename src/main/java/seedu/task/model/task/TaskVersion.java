package seedu.task.model.task;

import java.util.StringJoiner;

//@@author A0139958H

public class TaskVersion implements Comparable<TaskVersion> {
	public enum Command { ADD, UPDATE, DELETE, DEFAULT };
	private Integer versionIndex;
	private Integer taskIndex;
	private Task task;
	private Task originalTask;
	private Command command;
	
	
	public TaskVersion() {}
	
	public TaskVersion(int versionIndex, int taskIndex, Task task, Task originalTask, Command command) {
		this.versionIndex = versionIndex;
		this.taskIndex = taskIndex;
		this.task = task;
		this.setOriginalTask(originalTask);
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

	public Task getOriginalTask() {
		return originalTask;
	}

	public void setOriginalTask(Task originalTask) {
		this.originalTask = originalTask;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Integer getVersionIndex() {
		return versionIndex;
	}

	public void setVersionIndex(int versionIndex) {
		this.versionIndex = versionIndex;
	}

	@Override
	public int compareTo(TaskVersion u) {
		if (!this.getVersionIndex().equals(u.getVersionIndex()))
			return this.getVersionIndex() - u.getVersionIndex();
		else
			return this.getTaskIndex() - u.getTaskIndex();
	}
	
	@Override
	public String toString() {
		StringJoiner str = new StringJoiner(" ");
		return str.add(versionIndex.toString()).add(taskIndex.toString()).add(task.toString()).add(command.name()).toString();
	}
}
