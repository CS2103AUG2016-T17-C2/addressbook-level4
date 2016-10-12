package seedu.task.logic.parser;


import java.util.Date;
import java.util.List;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.UniqueTagList.DuplicateTagException;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.Name;
import seedu.task.model.task.Task;
import seedu.task.model.task.Venue;

public class UpdateTaskParser extends TaskParser{

	private Task task;
	private String input;
	private boolean isVenueUpdated;

	public UpdateTaskParser(String input) {
		this(new Task(), input);
	};
	
	public UpdateTaskParser(Task task, String input) {
		super(task, input);
		this.task = task;
		this.input = input;
		isVenueUpdated = false;
		
	};

	@Override
	protected String tagIdentification(String str) throws DuplicateTagException, IllegalValueException {
		String[] parts = str.split(SPLIT_STRING_BY_WHITESPACE);
		String inputWithoutTags = "";
		for (String part : parts) {
			if (part.startsWith(PREFIX_HASHTAG))
				matchTag(part.substring(1).trim());
			else if (part.startsWith(PREFIX_AT)) {
				if (!isVenueUpdated) {
					isVenueUpdated = true;
					task.setVenue(new Venue(""));
				}
				task.setVenue(new Venue(String.join(" ", task.getVenue().toString(), part.substring(1))));
			} else
				inputWithoutTags = String.join(" ", inputWithoutTags, part.trim());
		}
		return inputWithoutTags;
	}

	@Override
	protected void processTaskName(String str) throws IllegalValueException {
		if (!str.trim().isEmpty())
			task.setName(new Name(str));
	}
}
