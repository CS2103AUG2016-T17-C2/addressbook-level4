package seedu.task.logic.parser;

//@@author A0138301U
import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.FindCommand;
import seedu.task.logic.commands.IncorrectCommand;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.TaskPriority;
import seedu.task.model.task.Status;

/** 
 * Helper class to parse input when find is invoked, and to return the appropriate find command or other command based on input
 */
public class FindParser {
    
    private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one
    // or
    // more
    // keywords
    // separated
    // by
    // whitespace

    private static final char PREFIX_HASHTAG = '#';
    private static final char PREFIX_AT = '@';
    private static final String HIGH = "high";
    private static final String MEDIUM = "medium";
    private static final String LOW = "low";
    private static final String ACTIVE = "active";
    private static final String EXPIRED = "expired";
    private static final String DONE = "done";
    private static final String IGNORE = "ignore";
    

    
    public static Command parseInput(String args) throws IllegalValueException {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");

        if (keywords[0].charAt(0) == PREFIX_AT) { // prefix @ used to denote find
                                            // venue
            return returnFindCommandForVenue(keywords);
        }

        if (keywords[0].charAt(0) == PREFIX_HASHTAG) { // prefix # used to denote find
                                            // tag, priority or status
            return returnFindCommandForHashtagPrefix(keywords);
        }
        
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }


/** 
 * Returns the find command for priority or status, or by tagging.
 */
    private static Command returnFindCommandForHashtagPrefix(final String[] keywords) throws IllegalValueException {
        if (keywords[0].substring(1).isEmpty()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        if (keywords[0].substring(1).equalsIgnoreCase(HIGH)) {
            return new FindCommand(TaskPriority.HIGH);
        } else if (keywords[0].substring(1).equalsIgnoreCase(MEDIUM)) {
            return new FindCommand(TaskPriority.MEDIUM);
        } else if (keywords[0].substring(1).equalsIgnoreCase(LOW)) {
            return new FindCommand(TaskPriority.LOW);
        } else if (keywords[0].substring(1).equalsIgnoreCase(ACTIVE)) {
            return new FindCommand(Status.ACTIVE);
        } else if (keywords[0].substring(1).equalsIgnoreCase(DONE)) {
            return new FindCommand(Status.DONE);
        } else if (keywords[0].substring(1).equalsIgnoreCase(EXPIRED)) {
            return new FindCommand(Status.EXPIRED);
        } else if (keywords[0].substring(1).equalsIgnoreCase(IGNORE)) {
            return new FindCommand(Status.IGNORE);
        } else {
            Tag tag = new Tag(keywords[0].substring(1));
            return new FindCommand(tag);
        }
    }



    private static Command returnFindCommandForVenue(final String[] keywords) {
        if (keywords[0].substring(1).isEmpty()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return new FindCommand(keywords[0].substring(1));
    }

}
