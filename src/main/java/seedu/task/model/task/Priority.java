package seedu.task.model.task;

public enum Priority {
	HIGH("HIGH"), MEDIUM("MEDIUM"), LOW("LOW");

	private final String text;

	private Priority(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
