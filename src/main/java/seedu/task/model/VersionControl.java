package seedu.task.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.task.TaskVersion;

//@@author A0139958H

public class VersionControl {	
	private static VersionControl instance;
	private int commandCount;
	private int versionPosition;
	private List<TaskVersion> taskList;
	
	private VersionControl() {
		this.commandCount = 0;
		this.versionPosition = 0;
		this.taskList = new ArrayList<TaskVersion>();
	};
	
	/**
	 * VersionControl Singleton class. Creates only one instance of itself
	 * @return: instance of VersionControl
	 */
	public static VersionControl getInstance() {
		if (instance == null)
			instance = new VersionControl();
		return instance;
	}
	
	/**
	 * Get the TaskVersion object in the specified index
	 * @throws NullPointerException if its an invalid index
	 * @return: Task object
	 */
	public TaskVersion get(int i) {
		if (i >= 0 && taskList.size() > i)
			return taskList.get(i);
		else
			throw new NullPointerException();
	}
	
	/**
	 * Get the TaskVersion Object from the last index of the list
	 */
	public TaskVersion pop() {
		return (taskList.isEmpty()) ? null : taskList.get(taskList.size() - 1);
	}

	/**
	 * Add the TaskVersion Object to the last index of the list
	 */
	public boolean push(TaskVersion task) {
		return taskList.add(task);
	}
	
	/**
	 * Add all TaskVersion Object to the list
	 * @param List of TaskVersion Objects
	 */
	public boolean pushAll(List<TaskVersion> u) {
		return taskList.addAll(u);
	}
	
	/**
	 * Replace the TaskVersion Object on the specified index with a new one
	 * @param index, TaskVersion object
	 */
	public TaskVersion swap(int i, TaskVersion task) {
		return taskList.set(i, task);
	}
	
	public int getIndex() {
		return (taskList.isEmpty()) ? 0 : taskList.get(taskList.size() - 1).getVersionIndex();
	}
	
	public int getSize() {
		return taskList.size();
	}
	
	public int getCommandCount() {
		return commandCount;
	}
	
	public void setCommandCount() {
		if (getVersionPosition() == 0 && (getSize() != commandCount))
			commandCount = getSize() - commandCount;
	}
	
	public void updateVersionPosition(int versionPosition) {
		
		this.versionPosition += versionPosition;
	}
	
	public int getVersionPosition() {
		return versionPosition;
	}
	
	public void resetVersionPosition() {
		this.versionPosition = 0;
	}
	
	public void logList() {
		LogsCenter.getLogger(VersionControl.class).info(Arrays.toString(taskList.toArray()));
	}
}
