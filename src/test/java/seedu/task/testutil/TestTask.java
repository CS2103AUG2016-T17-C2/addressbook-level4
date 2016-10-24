package seedu.task.testutil;

import java.util.StringJoiner;

import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.tag.UniqueTagList.DuplicateTagException;
import seedu.task.model.task.*;

//@@author A0139958H

/**
 * A mutable Task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

	private Name name;
	private DateTime startDate;
	private DateTime endDate;
	private Venue venue;
	private Status status = Status.ACTIVE;
	private TaskPriority priority = TaskPriority.MEDIUM; // Default priority is medium
	private PinTask pinTask = PinTask.UNPIN; // Default is unpin
	private UniqueTagList tags;


	/**
	 * Empty Task Constructor.
	 */
	public TestTask() {
		this.startDate = new DateTime("");
		this.endDate = new DateTime("");
		this.venue = new Venue("");
		this.tags = new UniqueTagList();
	}

	@Override
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@Override
	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	@Override
	public UniqueTagList getTags() {
		return new UniqueTagList(tags);
	}

	/**
	 * Replaces this task's tags with the tags in the argument tag list.
	 */
	public void setTags(UniqueTagList replacement) {
		tags.setTags(replacement);
	}


	@Override
	public String toString() {
		return getAsText();
	}

	@Override
	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	@Override
	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	@Override
	public PinTask getPinTask() {
		return pinTask;
	}

	public void setPinTask(PinTask pinTask) {
		this.pinTask = pinTask;
	}

	public void addTag(Tag tag) throws DuplicateTagException {
		this.tags.add(tag);
	}
	
    /** Generates the correct add command based on the task given */
    public String getAddCommand() {
    	StringJoiner cmd = new StringJoiner(" ");
    	cmd.add("add")
    	.add(this.getName().toString())
    	.add("from tomorrow 2pm")
    	.add("by tomorrow 5pm")
    	.add("@" + this.getVenue().toString().trim())
    	.add("#" + this.getPriority().name())
    	.add("#" + this.getPinTask().name());
    	
        UniqueTagList tags = this.getTags();
        for(Tag tag : tags){
            cmd.add("#" + tag.tagName);
        }
        return cmd.toString();
    }
}
