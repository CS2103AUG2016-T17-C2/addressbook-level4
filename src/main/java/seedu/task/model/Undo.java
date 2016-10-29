package seedu.task.model;

import java.util.ArrayList;
import java.util.List;

import seedu.task.model.task.UndoTask;

//@@author A0139958H

public class Undo {	
	private static Undo instance;
	private int commandCount;
	private int versionPosition;
	private List<UndoTask> taskList;
	
	private Undo() {
		this.commandCount = 0;
		this.versionPosition = 0;
		this.taskList = new ArrayList<UndoTask>();
	};
	
	public static Undo getInstance() {
		if (instance == null)
			instance = new Undo();
		return instance;
	}
	
	public UndoTask get(int i) {
		return (i >= 0 && taskList.size() > i) ? taskList.get(i) : null;
	}
	
	public UndoTask pop() {
		return (taskList.isEmpty()) ? null : taskList.get(taskList.size() - 1);
	}
	
	public boolean push(UndoTask task) {
		return taskList.add(task);
	}
	
	public boolean pushAll(List<UndoTask> u) {
		return taskList.addAll(u);
	}
	
	public int getIndex() {
		return (taskList.isEmpty()) ? 0 : taskList.get(taskList.size() - 1).getUndoIndex();
	}
	
	public int getSize() {
		return taskList.size();
	}
	
	public int getCommandCount() {
		return commandCount;
	}
	
	public void setCommandCount() {
		if (getVersionPosition() == 1)
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
}
