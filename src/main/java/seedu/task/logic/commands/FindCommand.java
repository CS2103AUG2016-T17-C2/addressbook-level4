package seedu.task.logic.commands;

import java.util.Set;

import org.junit.Assert;

import seedu.task.model.tag.Tag;
import seedu.task.model.task.Priority;
import seedu.task.model.task.Status;

//@@author A0138301U
/**
 * Finds and lists all tasks in task book whose name contains any of the argument keywords.
 * Can also be used to find tasks with the associated priority or status level, or with the associated tagging, or tasks at a certain venue.
 * Keyword matching is not case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
            + "the specified keywords (not case-sensitive) and displays them as a list with index numbers.\n"
            + "When prefix hashtag (#) is used, will find all tasks with the associated priority level, or status, or with the tag.\n"
            + "When prefix at sign (@) is used, will find all tasks with the same venue.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " soccer dota basketball\n"
            + "OR\n"
            + "Parameters: #KEYWORD\n"
            + "Examples: " + COMMAND_WORD + " #high" + " OR " + COMMAND_WORD + " #active" + " OR " + COMMAND_WORD + " #healthy\n"
            + "OR\n"
            + "Parameters: @KEYWORD\n"
            + "Example: " + COMMAND_WORD + " @starbucks";

    private final Set<String> keywords;
    
    private final String venue;
    
    private final Tag tag;
    
    private final Priority priority;
    
    private final Status status;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
        this.venue = null;
        this.tag = null;
        this.priority = null;
        this.status = null;
    }

    public FindCommand(String venue) {
        this.keywords = null;
        this.venue = venue;
        this.tag = null;
        this.priority = null;
        this.status = null;
    }

    public FindCommand(Tag tag) {
        this.keywords = null;
        this.venue = null;
        this.tag = tag;
        this.priority = null;
        this.status = null;
    }

    public FindCommand(Priority priority) {
        this.keywords = null;
        this.venue = null;
        this.tag = null;
        this.priority = priority;
        this.status = null;
    }
    
    public FindCommand(Status status) {
        this.keywords = null;
        this.venue = null;
        this.tag = null;
        this.priority = null;
        this.status = status;
    }

    @Override
    public CommandResult execute() {
        if(this.venue != null) {
            model.updateFilteredTaskListByVenue(venue);
        } else if(this.tag != null) {
            model.updateFilteredTaskListByTag(tag);
        } else if(this.priority != null) {
            updateByPriorityLevel();
        } else if(this.keywords != null) {
            model.updateFilteredTaskListByKeywords(keywords);
        } else if(this.status != null){
            updateByStatus();
        } else {
            Assert.fail("unable to execute FindCommand due to incorrect attributes");
        }
        return new CommandResult(getMessageForTaskListShownSummary(model.getSortedTaskList().size()));
    }

    private void updateByStatus() {
        if(this.status == Status.ACTIVE){
            model.updateFilteredTaskListByActiveStatus();
        } else if(this.status == Status.DONE) {
            model.updateFilteredTaskListByDoneStatus();
        } else if(this.status == Status.EXPIRED) {
            model.updateFilteredTaskListByExpiredStatus();
        } else if(this.status == Status.IGNORE) {
            model.updateFilteredTaskListByIgnoreStatus();
        } else {
            Assert.fail("unable to execute FindCommand due to incorrect Status");
        }
        
    }

    private void updateByPriorityLevel() {
        if(this.priority == Priority.HIGH) {
            model.updateFilteredTaskListByHighPriority();
        } else if(this.priority == Priority.MEDIUM) {
            model.updateFilteredTaskListByMediumPriority();
        } else if(this.priority == Priority.LOW) {
            model.updateFilteredTaskListByLowPriority();
        } else {
            Assert.fail("unable to execute FindCommand due to incorrect Priority");
        }
    }


}
