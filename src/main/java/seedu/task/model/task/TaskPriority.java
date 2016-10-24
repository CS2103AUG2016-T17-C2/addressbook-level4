package seedu.task.model.task;

//@@author A0139958H

public enum TaskPriority {
	HIGH("HIGH"), MEDIUM("MEDIUM"), LOW("LOW");

	private final String text;

	private TaskPriority(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
