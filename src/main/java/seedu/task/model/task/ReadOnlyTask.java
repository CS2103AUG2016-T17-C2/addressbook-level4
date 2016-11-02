package seedu.task.model.task;

import seedu.task.model.tag.UniqueTagList;

//@@author A0139958H

/**
 * A read-only immutable interface for a Task in the taskBook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Name getName();
    DateTime getStartDate();
    DateTime getEndDate();
    Venue getVenue();
    TaskPriority getPriority();
    Status getStatus();
    PinTask getPinTask();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this; 
    }
    
    //@@author A0138301U
    /**
     * Formats the Task showing all details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        constructBuilderString(builder);
        
        getTags().forEach(builder::append);
        return builder.toString();
    }
    default void constructBuilderString(final StringBuilder builder) {
        builder.append(getName());
        if(!getStartDate().value.isEmpty()) {
            builder.append(" Start Date: ")
            .append(getStartDate());
        }
        if(!getEndDate().value.isEmpty()) {
            builder.append(" End Date: ")
            .append(getEndDate());
        }
        if(!getVenue().value.isEmpty()) {
            builder.append(" Venue: ")
            .append(getVenue());
        }
        builder.append(" Priority: ")
        .append(getPriority())
        .append(" Status: ")
        .append(getStatus())
        .append(" Tags: ")
        .append(" Pin: ")
        .append(getPinTask());
    }
    
    //@@author
    
    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
