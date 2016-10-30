package seedu.task.logic.parser;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.SetCommand;
import seedu.task.model.ModelManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList.DuplicateTagException;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.Name;
import seedu.task.model.task.PinTask;
import seedu.task.model.task.TaskPriority;
import seedu.task.model.task.Status;
import seedu.task.model.task.Task;
import seedu.task.model.task.Venue;

//@@author A0139958H

public class UpdateTaskParser extends TaskParser{

	private Task task;
	private String input;
	private boolean isVenueUpdated;
	private boolean isStartDateUpdated;
	private boolean isEndDateUpdated;

	/**
	 * @param: input arguments from User
	 */
	public UpdateTaskParser(String input) {
		this(new Task(), input);
	};
	
	/**
	 * @param: Task object and input arguments from User
	 */
	public UpdateTaskParser(Task task, String input) {
		super(task, input);
		this.task = task;
		this.input = input;
		isVenueUpdated = false;
		isStartDateUpdated = false;
		isEndDateUpdated = false;
	};

	/**
	 * Parses the user input to identify tags and venues
	 * @param input arguments from user
	 * @throws IllegalValueException if user input contains invalid arguments
	 * @throws DuplicateTagException if duplicate tag is found
	 * @return: parsed argument
	 */
	@Override
	protected String tagIdentification(String str) throws DuplicateTagException, IllegalValueException {
		String[] parts = str.split(SPLIT_STRING_BY_WHITESPACE);
		String strWithNoTags = "";
		for (String part : parts) {
			if (part.startsWith(PREFIX_HASHTAG))
				strWithNoTags = matchTag(strWithNoTags, part.substring(1).trim());
			else if (part.startsWith(PREFIX_AT)) {
				if (!isVenueUpdated) {
					isVenueUpdated = true;
					task.setVenue(new Venue(""));
				}
				if (part.substring(1).trim().equalsIgnoreCase(NULL))
					task.setVenue(new Venue(""));
				else
					task.setVenue(new Venue(String.join(WHITE_SPACE, task.getVenue().toString(), part.substring(1))));
			} else
				strWithNoTags = String.join(WHITE_SPACE, strWithNoTags, part.trim());
		}
		return strWithNoTags;	
	}
	
	/**
	 * Parses the tags to priority/pin/date or tags
	 * @param input arguments from user, potential tags
	 * @throws IllegalValueException if user input contains invalid arguments
	 * @throws DuplicateTagException if duplicate tag is found
	 * @return: parsed argument
	 */
	protected String matchTag(String str, String tag) throws DuplicateTagException, IllegalValueException {
		if (tag.equalsIgnoreCase(NULL))
			str = dateMatch(str);
		else if (EnumUtils.isValidEnum(TaskPriority.class, tag.toUpperCase()))
			task.setPriority(TaskPriority.valueOf(tag.toUpperCase()));
		else if (EnumUtils.isValidEnum(PinTask.class, tag.toUpperCase()))
			task.setPinTask(PinTask.valueOf(tag.toUpperCase()));
		else
			task.updateTag(new Tag(tag));
		return str;
	}
	
	/**
	 * Sets the start date to the task
	 * @param list of dates
	 * @throws IllegalValueException if start date is empty or both start date and end dates are same
	 */
	protected void setStartDate(List<Date> dates) throws IllegalValueException {
		if (!task.getStartDate().value.isEmpty() && isStartDateUpdated)
			throw new IllegalValueException(DateTime.MESSAGE_MULTIPLE_START_DATE);
		
		isStartDateUpdated = true;
		if (dates.size() == 1) {
			task.setStartDate(new DateTime(dates.get(0)));
		} else if (dates.size() >= 2) {
			if (dates.get(0).compareTo(dates.get(1)) == 0)
				throw new IllegalValueException(DateTime.MESSAGE_DATE_SAME);
			task.setStartDate(new DateTime(dates.get(0)));
			task.setEndDate(new DateTime(dates.get(1)));
		}
	}
	
	/**
	 * Sets the end date to the task
	 * @param list of dates
	 * @throws IllegalValueException if end date is empty
	 */
	protected void setEndDate(List<Date> dates) throws IllegalValueException {
		if (!task.getEndDate().value.isEmpty() && isEndDateUpdated)
			throw new IllegalValueException(DateTime.MESSAGE_MULTIPLE_END_DATE);
		
		isEndDateUpdated = true;
		task.setEndDate(new DateTime(dates.get(dates.size()-1)));
	}
	
	/**
	 * Sets task name for the task
	 * @param input arguments from user
	 * @throws IllegalValueException if name is empty or invalid (contains anything other than alphanumeric values)
	 */
	@Override
	protected void processTaskName(String str) throws IllegalValueException {
		if (!str.trim().isEmpty())
			task.setName(new Name(str));
	}
	
    /**
     * Sets the status of task to done or ignore
     * @throws IllegalValueException if an invalid status is entered in the input. 
     */
	public Task setTaskStatus() throws IllegalValueException {
		
		if (!task.getStatus().equals(Status.DONE)) {
			if (input.trim().toUpperCase().equals(Status.DONE.toString()) || input.trim().toUpperCase().equals(Status.IGNORE.toString()))
				task.setStatus(Status.valueOf(input.trim().toUpperCase()));
			else
				throw new IllegalValueException(SetCommand.MESSAGE_STATUS_CONSTRAINT);
		} else if (task.getStatus().equals(Status.DONE))
			throw new IllegalValueException(SetCommand.MESSAGE_STATUS_DONE);
		else
			throw new IllegalValueException(Messages.MESSAGE_FAILURE_SET_COMMAND);
		return task;
	}
}
