package seedu.task.model;

import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;

import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskBook newData);

    /** Returns the TaskBook */
    ReadOnlyTaskBook getTaskBook();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task 
     * @throws DateClashTaskException */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException, DateClashTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskListByKeywords(Set<String> keywords);

    /** Updates the filter of the filtered task list to show tasks with HIGH priority*/
    void updateFilteredTaskListByHighPriority();
    
    /** Updates the filter of the filtered task list to show tasks with MEDIUM priority*/
    void updateFilteredTaskListByMediumPriority();
    
    /** Updates the filter of the filtered task list to show tasks with LOW priority*/
    void updateFilteredTaskListByLowPriority();
    
}
