package seedu.task.logic.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.ModelManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.tag.UniqueTagList.DuplicateTagException;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.Name;
import seedu.task.model.task.PinTask;
import seedu.task.model.task.Priority;
import seedu.task.model.task.Status;
import seedu.task.model.task.Task;
import seedu.task.model.task.Venue;

public class TaskParser {

	public static final String SPLIT_STRING_BY_WHITESPACE = "\\s+";
	public static final String REGEX_CASE_INSENSITIVE = "(?i)";
	public static final String WHITE_SPACE = " ";
	public static final String PREFIX_HASHTAG = "#";
	public static final String PREFIX_AT = "@";
	public static final String AT = "at ";
	public static final String FROM = "from ";
	public static final String BY = "by ";
	public static final String NULL = "NULL";

	protected static final Logger logger = LogsCenter.getLogger(TaskParser.class);
	private Task task;
	private String input;

	public TaskParser(String input) {
		this(new Task(), input);
	};

	public TaskParser(Task task, String input) {
		this.task = task;
		this.input = input;
	};

	public Task parseInput() throws IllegalValueException {
		input = tagIdentification(input);
		input = dateIdentification(input, FROM, FROM.trim().length());
		input = dateIdentification(input, BY, BY.trim().length());
		validateStartDate();
		processTaskName(input);

		logger.info("parseInput: " + task.toString());

		return task;
	}

	protected String tagIdentification(String str) throws DuplicateTagException, IllegalValueException {
		String[] parts = str.split(SPLIT_STRING_BY_WHITESPACE);
		String strWithNoTags = "";
		for (String part : parts) {
			if (part.startsWith(PREFIX_HASHTAG))
				strWithNoTags = matchTag(strWithNoTags, part.substring(1).trim());
			else if (part.startsWith(PREFIX_AT)) {
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
			task.addTag(new Tag(tag));
		return str;
	}

	protected String dateIdentification(String str, String dateType, int strLength) throws IllegalValueException {
		String[] parts = str.split("(?=" + REGEX_CASE_INSENSITIVE + dateType + ")");
		logger.info("dateIdentification: " + Arrays.toString(parts));
		StringJoiner processedString = new StringJoiner(WHITE_SPACE);

		for (int i = 0; i < parts.length; i++) {
			if (startsWithIgnoreCase(parts[i], dateType)) {
				String result = processDateNLP(parts[i], dateType);
				if (!result.equals(parts[i]))
					parts[i] = result.substring(strLength);
			}

			processedString.add(parts[i].trim());
			logger.info("dateIdentification length: " + parts.length + " strWithNoDates:" + processedString.toString() + " part: " + parts[i]);
		}
		return processedString.toString();
	}

	protected String dateMatch(String str) throws IllegalValueException {
		logger.info("dateMatch Str:" + str);
		String lastWord = StringUtils.stripEnd(str, " ").substring(str.lastIndexOf(" ") + 1);

		if (StringUtils.containsIgnoreCase(lastWord, FROM.trim()))
			task.setStartDate(new DateTime(""));
		else if (StringUtils.containsIgnoreCase(lastWord, BY.trim()))
			task.setEndDate(new DateTime(""));
		else
			throw new IllegalValueException(Tag.MESSAGE_HASHTAG_NULL_CONSTRAINTS);
		logger.info("dateMatch Str1:" + str);
		return replaceLast(str, lastWord, "");
	}

	protected String processDateNLP(String str, String dateType) throws IllegalValueException {
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(str);
		List<Date> dates = new ArrayList<>();

		if (!groups.isEmpty()) {
			dates.addAll(groups.get(0).getDates());
			Collections.sort(dates);
			String matchingValue = groups.get(0).getText();
			str = str.replace(matchingValue, WHITE_SPACE);
			logger.info("groups size:  " + groups.size() + " Str: " + str + " dates " + Arrays.toString(dates.toArray())
			        + " matchingValue: " + matchingValue);
		}

		if (!dates.isEmpty()) {
			if (dateType.equalsIgnoreCase(FROM))
				setStartDate(dates);
			else if (dateType.equalsIgnoreCase(BY))
				setEndDate(dates);
		}

		return str;
	}

	protected void setStartDate(List<Date> dates) throws IllegalValueException {
		if (!task.getStartDate().value.isEmpty())
			throw new IllegalValueException(DateTime.MESSAGE_MULTIPLE_START_DATE);

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
		if (!task.getEndDate().value.isEmpty())
			throw new IllegalValueException(DateTime.MESSAGE_MULTIPLE_END_DATE);
		task.setEndDate(new DateTime(dates.get(dates.size()-1)));
	}
	
	protected void validateStartDate() throws IllegalValueException {
		if (!task.getStartDate().value.isEmpty() && !task.getEndDate().value.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss zzz yyyy");

			LocalDateTime startDate = LocalDateTime.parse(task.getStartDate().value, formatter);
			LocalDateTime endDate = LocalDateTime.parse(task.getEndDate().value, formatter);
			if (!startDate.isBefore(endDate))
				throw new IllegalValueException(DateTime.MESSAGE_INVALID_START_DATE);
		}
	}

	protected void processTaskName(String str) throws IllegalValueException {
		task.setName(new Name(str));
	}

	protected boolean startsWithIgnoreCase(String str, String prefix) {
		return startsWith(str, prefix, true);
	}

	protected boolean startsWith(String str, String prefix, boolean ignoreCase) {
		if (str == null || prefix == null) {
			return (str == null && prefix == null);
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}
	
	protected String replaceLast(String string, String toReplace, String replacement) {
	    int pos = string.lastIndexOf(toReplace);
	    if (pos > -1) {
	        return string.substring(0, pos)
	             + replacement
	             + string.substring(pos + toReplace.length(), string.length());
	    } else {
	        return string;
	    }
	}
}
