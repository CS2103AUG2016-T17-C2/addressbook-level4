package seedu.task.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.task.commons.core.LogsCenter;
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
	
	public static VersionControl getInstance() {
		if (instance == null)
			instance = new VersionControl();
		return instance;
	}
	
	public TaskVersion get(int i) {
		if (i >= 0 && taskList.size() > i)
			return taskList.get(i);
		else
			throw new NullPointerException();
		//return (i >= 0 && taskList.size() > i) ? taskList.get(i) : null;
	}
	
	public TaskVersion pop() {
		return (taskList.isEmpty()) ? null : taskList.get(taskList.size() - 1);
	}
	
	public boolean push(TaskVersion task) {
		return taskList.add(task);
	}
	
	public boolean pushAll(List<TaskVersion> u) {
		return taskList.addAll(u);
	}
	
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
		if (getVersionPosition() == 1 && (getSize() != commandCount))
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
