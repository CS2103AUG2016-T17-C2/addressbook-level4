package seedu.task.logic.commands;

//@@author A0138301U
import java.util.Arrays;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.Messages;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.ui.MapVenueRequestEvent;
import seedu.task.model.task.ReadOnlyTask;

/**
 * Format browser to display search for venue via Google Maps.
 */
public class MapCommand extends Command {

    public final int targetIndex;
    
    public static final String COMMAND_WORD = "map";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Runs a Google Maps search for the venue of a task.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 4";

    public static final String MESSAGE_MAP_TASK_SUCCESS = "Opening Google Maps window for task %1$s";

    public static final String MESSAGE_MAP_TASK_FAILURE = "Error: Task %1$s has no venue";

    public MapCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }
    
    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getSortedTaskList();

        if (lastShownList.size() < targetIndex) {//return invalid index message
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(String.format(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX,
                    Arrays.toString(new int[]{targetIndex})));
        }
        
        if (lastShownList.get(targetIndex-1).getVenue().value.isEmpty()) {//return no venue message
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(String.format(MESSAGE_MAP_TASK_FAILURE, targetIndex));
        }
        
        EventsCenter.getInstance().post(new MapVenueRequestEvent(lastShownList.get(targetIndex-1).getVenue().value));
        return new CommandResult(String.format(MESSAGE_MAP_TASK_SUCCESS, targetIndex));
    }
}
