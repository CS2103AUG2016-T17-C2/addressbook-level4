package seedu.task.logic.parser;

import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.task.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.*;
import seedu.task.model.ModelManager;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.Priority;
import seedu.task.model.task.ReadOnlyTask;

import java.util.logging.Logger;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
	private static final Logger logger = LogsCenter.getLogger(Parser.class);

	private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
	
	
    private static final Pattern TASK_INDEX_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>\\S+)(?<arguments>.*)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    
    ShortcutSetting shortcutSetting;
    
    public Parser(ShortcutSetting shortcutSetting) {
        this.shortcutSetting = shortcutSetting;
        
    }
    
    public void setShortcutSetting (ShortcutSetting shortcutSetting){
        this.shortcutSetting = shortcutSetting;
    }
    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws IOException 
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        
        String tempCommandWord = matcher.group("commandWord");
        
        
        if ( tempCommandWord.equals(shortcutSetting.getAdd())){           
            tempCommandWord = "add";
        }
        if ( tempCommandWord.equals(shortcutSetting.getDelete())){
             tempCommandWord = "delete";
        }
        if ( tempCommandWord.equals(shortcutSetting.getList())){
            tempCommandWord = "list";
        }
        
        final String commandWord = tempCommandWord;
        final String arguments = matcher.group("arguments");
        
        
        
        switch (commandWord) {
        
        case ChangeFilePathCommand.COMMAND_WORD:
            return prepareChangeFilePathCommand(arguments);

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);
            
        case UpdateCommand.COMMAND_WORD:
        	return prepareUpdate(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        
        case ShortcutCommand.COMMAND_WORD:
            return prepareShortcut(arguments);
        
        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }
    
    

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        TaskParser taskParser = new TaskParser(args);
        try {
        	return new AddCommand(taskParser.parseInput());
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    /**
     * Parses arguments in the context of the changeShortcut task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareShortcut(String args) {
        String elements[] = args.split("\\s+");
        String field = elements[1].trim();
        String keyword = elements[2].trim();
                return new ShortcutCommand(field, keyword);
        }
    /**

     * Parses arguments in the context of the update task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareUpdate(String args){
    	logger.info("args: " + args);
    	Pair<Optional<Integer>, Optional<String>> argsPair = parseIndexWithArgs(args);
    	logger.info("left: " + argsPair.getLeft() + " right: " + argsPair.getRight());

        if(!argsPair.getLeft().isPresent() || !argsPair.getRight().isPresent())
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateCommand.MESSAGE_USAGE));
        
        return new UpdateCommand(argsPair.getLeft().get(), argsPair.getRight().get());
    }
    
    
     /* Parses arguments in the context of the changefilepath command.
     *
     * @param args new filePath args string
     * @return the prepared command 
     * @throws IOException 
     */
    private Command prepareChangeFilePathCommand(String args) {
                return new ChangeFilePathCommand(args);
        }
    /**
     * Extracts the new task's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_FORMAT.matcher(command.trim());
    	
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }
    
    /** Processes the command with an index followed by a series of arguments.
     * Returns the specified index in the {@code command} and the arguments 
     * IF a positive unsigned integer is given as the index and if arguments are present.
     *   Returns an {@code Optional.empty()} otherwise
     */
    private Pair<Optional<Integer>, Optional<String>> parseIndexWithArgs(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
    	//logger.info("left: " + argsPair.getLeft() + " right: " + argsPair.getRight());

        if (!matcher.matches()) {
            return new ImmutablePair<Optional<Integer>, Optional<String>>(Optional.empty(), Optional.empty());
        }
        
        String index = matcher.group("targetIndex");
        String args = matcher.group("arguments");
        if(!StringUtil.isUnsignedInteger(index) || args.isEmpty()){
            return new ImmutablePair<Optional<Integer>, Optional<String>>(Optional.empty(), Optional.empty());
        }
        return new ImmutablePair<Optional<Integer>, Optional<String>>(Optional.of(Integer.parseInt(index)), Optional.of(args));
    }
    
    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        try {
            logger.info("find: " + args);
            final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
            if (!matcher.matches()) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }

            // keywords delimited by whitespace
            final String[] keywords = matcher.group("keywords").split("\\s+");

            if (keywords[0].charAt(0) == '@') { // prefix @ used to denote find
                                                // venue
                if (keywords[0].substring(1).isEmpty()) {
                    return new IncorrectCommand(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
                return new FindCommand(keywords[0].substring(1));
            }

            if (keywords[0].charAt(0) == '#') { // prefix # used to denote find
                                                // tag or priority
                if (keywords[0].substring(1).isEmpty()) {
                    return new IncorrectCommand(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
                }
                if (keywords[0].substring(1).equalsIgnoreCase("high")) {
                    return new FindCommand(Priority.HIGH);
                } else if (keywords[0].substring(1).equalsIgnoreCase("medium")) {
                    return new FindCommand(Priority.MEDIUM);
                } else if (keywords[0].substring(1).equalsIgnoreCase("low")) {
                    return new FindCommand(Priority.LOW);
                } else {
                    Tag tag = new Tag(keywords[0].substring(1));
                    return new FindCommand(tag);
                }
            }

            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
            return new FindCommand(keywordSet);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
}