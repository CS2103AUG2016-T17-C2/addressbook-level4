package seedu.task.model;

import javafx.collections.transformation.FilteredList;
import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.model.TaskBookChangedEvent;
import seedu.task.commons.util.StringUtil;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;
import seedu.task.model.task.UniqueTaskList.TaskNotFoundException;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the task book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskBook taskBook;
    private final FilteredList<Task> filteredTasks;

    /**
     * Initializes a ModelManager with the given TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(TaskBook src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        taskBook = new TaskBook(src);
        filteredTasks = new FilteredList<>(taskBook.getTasks());
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredTasks = new FilteredList<>(taskBook.getTasks());
    }

    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyTaskBook getTaskBook() {
        return taskBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new TaskBookChangedEvent(taskBook));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskBook.removeTask(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException, DateClashTaskException {
        taskBook.addTask(task);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
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
            if(task.getPriority().toString().equals("HIGH")) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByMediumPriority() {
        filteredTasks.setPredicate(task -> {
            if(task.getPriority().toString().equals("MEDIUM")) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByLowPriority() {
        filteredTasks.setPredicate(task -> {
            if(task.getPriority().toString().equals("LOW")) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByActiveStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().toString().equals("ACTIVE")) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByExpiredStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().toString().equals("EXPIRED")) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByDoneStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().toString().equals("DONE")) {
                return true;
            } else {
                return false;
            }
        });
    }
    
    public void updateFilteredTaskListByIgnoredStatus() {
        filteredTasks.setPredicate(task -> {
            if(task.getStatus().toString().equals("IGNORE")) {
                return true;
            } else {
                return false;
            }
        });
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask person);
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

}
