package seedu.task.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;

import java.util.ArrayList;
import java.util.List;

//@@author A0139958H

/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private String venue;
    @XmlElement(required = true)
    private String priority;
    @XmlElement(required = true)
    private String status;
    @XmlElement(required = true)
    private String pinTask;
    
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        name = source.getName().fullName;
        startDate = source.getStartDate().value;
        endDate = source.getEndDate().value;
        venue = source.getVenue().value;
        priority = source.getPriority().toString();
        status = source.getStatus().toString();
        pinTask = source.getPinTask().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Task toModelType() throws IllegalValueException {
        final List<Tag> taskTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }
        final Name name = new Name(this.name);
        final DateTime startDate = new DateTime(this.startDate);
        final DateTime endDate = new DateTime(this.endDate);
        final Venue venue = new Venue(this.venue);
        final TaskPriority priority = TaskPriority.valueOf(this.priority.toUpperCase());
        final Status status = Status.valueOf(this.status.toUpperCase());
        final PinTask pinTask = PinTask.valueOf(this.pinTask.toUpperCase());
        final UniqueTagList tags = new UniqueTagList(taskTags);
        return new Task(name, startDate, endDate, venue, priority, status, pinTask, tags);
    }
}
