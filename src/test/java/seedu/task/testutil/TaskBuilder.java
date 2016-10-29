package seedu.task.testutil;

import java.util.Date;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.*;

//@@author A0139958H

/** A utility class to build Task Objects easily
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new Name(name));
        return this;
    }

    public TaskBuilder withVenue(String address) throws IllegalValueException {
        this.task.setVenue(new Venue(address));
        return this;
    }

    public TaskBuilder withStartDate(Date date) throws IllegalValueException {
        this.task.setStartDate(new DateTime(date));
        return this;
    }

    public TaskBuilder withEndDate(Date date) throws IllegalValueException {
        this.task.setEndDate(new DateTime(date));
        return this;
    }
    
    public TaskBuilder withPriority(String priority) throws IllegalValueException {
		task.setPriority(TaskPriority.valueOf(priority.toUpperCase()));
        return this;
    }
    
    public TaskBuilder withStatus(String status) throws IllegalValueException {
		task.setStatus(Status.valueOf(status.toUpperCase()));
        return this;
    }
    
    public TaskBuilder withPinTask(String pinTask) throws IllegalValueException {
		task.setPinTask(PinTask.valueOf(pinTask.toUpperCase()));
        return this;
    }
    
    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TestTask build() {
        return this.task;
    }
    
    //@@author A0138301U
    public TaskBuilder withNullStartDate() throws IllegalValueException {
        this.task.setNullStartDate();
        return this;
    }

    public TaskBuilder withNullEndDate() throws IllegalValueException {
        this.task.setNullEndDate();
        return this;
    }

}
