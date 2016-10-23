package seedu.task.model.task;

public enum PinTask {
	PIN("PIN"), UNPIN("UNPIN");

	private final String text;

	private PinTask(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
	
	public boolean isPinned() {
	    if(text.equals("PIN")) {
	        return true;
	    }
	    return false;
	}
}
