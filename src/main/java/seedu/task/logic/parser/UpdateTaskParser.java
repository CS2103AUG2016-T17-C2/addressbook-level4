package seedu.task.logic.parser;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList.DuplicateTagException;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.Name;
import seedu.task.model.task.PinTask;
import seedu.task.model.task.Priority;
import seedu.task.model.task.Task;
import seedu.task.model.task.Venue;

public class UpdateTaskParser extends TaskParser{

	private Task task;
	private boolean isVenueUpdated;
	private boolean isStartDateUpdated;
	private boolean isEndDateUpdated;


	public UpdateTaskParser(String input) {
		this(new Task(), input);
	};
	
	public UpdateTaskParser(Task task, String input) {
		super(task, input);
		this.task = task;
		isVenueUpdated = false;
		isStartDateUpdated = false;
		isEndDateUpdated = false;
	};

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
					task.setVenue(new Venue(String.join(" ", task.getVenue().toString(), part.substring(1))));
			} else
				strWithNoTags = String.join(" ", strWithNoTags, part.trim());
		}
		return strWithNoTags;	
	}
	
	protected String matchTag(String str, String tag) throws DuplicateTagException, IllegalValueException {
		if (tag.equalsIgnoreCase(NULL))
			str = dateMatch(str);
		else if (EnumUtils.isValidEnum(Priority.class, tag.toUpperCase()))
			task.setPriority(Priority.valueOf(tag.toUpperCase()));
		else if (EnumUtils.isValidEnum(PinTask.class, tag.toUpperCase()))
			task.setPinTask(PinTask.valueOf(tag.toUpperCase()));
		else
			task.updateTag(new Tag(tag));
		return str;
	}
	
	
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
	
	protected void setEndDate(List<Date> dates) throws IllegalValueException {
		if (!task.getEndDate().value.isEmpty() && isEndDateUpdated)
			throw new IllegalValueException(DateTime.MESSAGE_MULTIPLE_END_DATE);
		
		isEndDateUpdated = true;
		task.setEndDate(new DateTime(dates.get(dates.size()-1)));
	}
	
	@Override
	protected void processTaskName(String str) throws IllegalValueException {
		if (!str.trim().isEmpty())
			task.setName(new Name(str));
	}
}
