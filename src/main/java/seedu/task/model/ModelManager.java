package seedu.task.model;

import com.google.common.eventbus.Subscribe;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.model.TaskBookChangedEvent;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.parser.TaskParser;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Status;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskPriority;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;
import seedu.task.commons.events.model.TaskBookChangedEvent;

import org.junit.Assert;

/**
 * Represents the in-memory model of the task book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskBook taskBook;
    private final FilteredList<Task> filteredTasks;
    //@@author A0138301U
    private SortedList<Task> sortedTasks;
    //@@author
    /**
     * Initializes a ModelManager with the given TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(TaskBook src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task book: " + src + " and user prefs " + userPrefs);

        taskBook = new TaskBook(src);
        filteredTasks = new FilteredList<>(taskBook.getTasks());
        registerAsAnEventHandler(this);
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredTasks = new FilteredList<>(taskBook.getTasks());
        //@@author A0138301U
        registerAsAnEventHandler(this);
        updateTaskStatus();
    }

    protected void registerAsAnEventHandler(Object handler) {
        EventsCenter.getInstance().registerHandler(handler);
    }
    //@@author
    
    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        indicateTaskBookChanged();
    }

    @Override
    public ReadOnlyTaskBook getTaskBook() {
        return taskBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskBookChanged() {
        raise(new TaskBookChangedEvent(taskBook));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskBook.removeTask(target);
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        indicateTaskBookChanged();
    }

    //@@author A0139958H
    
    @Override
    public synchronized int addTask(Task task) throws UniqueTaskList.DuplicateTaskException, DateClashTaskException {
        int position = taskBook.addTask(task);
        updateFilteredListToShowAll();
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        indicateTaskBookChanged();
        return position;
    }
    
    @Override
    public synchronized int addTask(int index, Task task) throws UniqueTaskList.DuplicateTaskException, DateClashTaskException {
        int position = taskBook.addTask(index, task);
        updateFilteredListToShowAll();
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        indicateTaskBookChanged();
        return position;
    }
    
	@Override
	public synchronized void updateTask(Task toReplace, Task toUpdate) throws DateClashTaskException {
        taskBook.updateTask(toReplace, toUpdate);
        updateFilteredListToShowAll();
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        indicateTaskBookChanged();		
	}
	
	public synchronized void updateTaskStatus() {
		taskBook.updateTaskStatus();
        updateFilteredListToShowAll();
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        indicateTaskBookChanged();	
	}	

    //@@author A0138301U
	
    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getSortedTaskList() {
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        Assert.assertNotNull("This object should not be null", sortedTasks);
        return new UnmodifiableObservableList<>(sortedTasks);
    }

    public Task getTaskByIndex(int index) {
        Task task = sortedTasks.get(index);
        return task;
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskListByKeywords(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    public void updateFilteredTaskListByHighPriority() {
        filteredTasks.setPredicate(task -> {
            if(task.getPriority().equals(TaskPriority.HIGH)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByMediumPriority() {
        filteredTasks.setPredicate(task -> {
            if(task.getPriority().equals(TaskPriority.MEDIUM)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByLowPriority() {
        filteredTasks.setPredicate(task -> {
            if(task.getPriority().equals(TaskPriority.LOW)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByActiveStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().equals(Status.ACTIVE)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByExpiredStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().equals(Status.EXPIRED)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByDoneStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().equals(Status.DONE)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByIgnoreStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().equals(Status.IGNORE)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByVenue(String venue) {
        filteredTasks.setPredicate(task -> {
            if(task.getVenue().toString().contains(venue)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByTag(Tag tag){
        filteredTasks.setPredicate(task -> {
            if(task.getTags().contains(tag)) {
                return true;
            } else {
                return false;
            }
        });
    }
    
        
    //========== Inner classes/interfaces used for sorting ==================================================

/**    
 * default comparator: arranges tasks by status (active and expired first, followed by done and ignore)
 * then pin, then priority level, then by start date or end date where applicable, then by alphabetical order*/
    public static class TaskComparator implements Comparator<ReadOnlyTask>
    {
        public int compare(ReadOnlyTask task1, ReadOnlyTask task2)
        {
            int value = compareTaskByStatus(task1.getStatus(), task2.getStatus());//by status
            if(value == 0) {
                value = task1.getPinTask().compareTo(task2.getPinTask());//by pin
                if(value == 0) {
                    value = task1.getPriority().compareTo(task2.getPriority());//by priority
                    if(value == 0) {
                        Date date1;
                        Date date2;
                        
                        //assign date if possible, if not sort by alphabetical order
                        if(hasStartDate(task1)){
                            date1 = convertStringToDateObject(task1.getStartDate().toString());
                        } else if (hasEndDate(task1)){
                            date1 = convertStringToDateObject(task1.getEndDate().toString());
                        } else {
                            return task1.getName().fullName.compareTo(task2.getName().fullName);
                        }

                        //assign date if possible, if not sort by alphabetical order
                        if(hasStartDate(task2)){
                            date2 = convertStringToDateObject(task2.getStartDate().toString());
                        } else if (hasEndDate(task2)){
                            date2 = convertStringToDateObject(task2.getEndDate().toString());
                        } else {
                            return task1.getName().fullName.compareTo(task2.getName().fullName);
                        }
                        
                        //compare by alphabetical order if same date
                        if(date1.equals(date2)) {
                            return task1.getName().fullName.compareTo(task2.getName().fullName);
                        }
                        
                        if(date1.after(date2)) {
                            return 1;
                        } else {
                            return 0;
                        }
                        
                    }
                    return value;
                }
                return value;
            }
            return value;
        }

        private int compareTaskByStatus(Status s1, Status s2) {
            int value;
            if(statusIsActiveOrExpired(s1)) {
                if(statusIsActiveOrExpired(s2)) {
                    value = 0;
                } else {
                    value = -1;
                }
            } else {
                if(statusIsActiveOrExpired(s2)) {
                    value = 1;
                } else {
                    value = 0;
                }
            }
            return value;
        }
    }

    private static boolean hasEndDate(ReadOnlyTask task) {
        return !task.getEndDate().value.isEmpty();
    }

    private static boolean hasStartDate(ReadOnlyTask task) {
        return !task.getStartDate().value.isEmpty();
    }

    private static Date convertStringToDateObject(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss zzz yyyy");
        LocalDateTime parsedDate = LocalDateTime.parse(date, formatter);
        Date dateFromParsedDate = Date.from(parsedDate.atZone(ZoneId.systemDefault()).toInstant());
        return dateFromParsedDate;
    }
    
    private static boolean statusIsActiveOrExpired(Status status) {
        if(status.equals(Status.ACTIVE) || status.equals(Status.EXPIRED)) {
            return true;
        }
        return false;
    }
    //@@author
    
    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

    @Subscribe
    public void handleTaskBookChangedEvent(TaskBookChangedEvent tbce) {
        sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());
        
    }
}
