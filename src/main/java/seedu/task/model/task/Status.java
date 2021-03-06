package seedu.task.model.task;

//@@author A0139958H

public enum Status {
	ACTIVE("ACTIVE"), EXPIRED("EXPIRED"), DONE("DONE"), IGNORE("IGNORE");

	private final String text;

	private Status(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
