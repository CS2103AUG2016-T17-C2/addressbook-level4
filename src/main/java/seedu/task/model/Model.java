package seedu.task.model;

import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;
import seedu.task.model.task.UniqueTaskList.DuplicateTaskException;

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
     * @return position of the newly added task in the collection
     * @throws DuplicateTaskException
     * @throws DateClashTaskException */
    int addTask(Task task) throws UniqueTaskList.DuplicateTaskException, DateClashTaskException;

    /** Adds the given task in the specified index
     * @return position of the newly added task in the collection
     * @throws DuplicateTaskException
     * @throws DateClashTaskException */
	int addTask(int index, Task task) throws DuplicateTaskException, DateClashTaskException;

    /** Updates the given task 
     * @throws DateClashTaskException */
    void updateTask(Task toReplace, Task toUpdate) throws DateClashTaskException;
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */    
    UnmodifiableObservableList<ReadOnlyTask> getSortedTaskList();
    
    /** Returns the task identified by index number */    
    Task getTaskByIndex(int index);

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
    
    /** Updates the filter of the filtered task list to show ACTIVE tasks*/
    void updateFilteredTaskListByActiveStatus();
    
    /** Updates the filter of the filtered task list to show EXPIRED tasks*/
    void updateFilteredTaskListByExpiredStatus();
    
    /** Updates the filter of the filtered task list to show DONE tasks*/
    void updateFilteredTaskListByDoneStatus();
    
    /** Updates the filter of the filtered task list to show IGNORED tasks*/
    void updateFilteredTaskListByIgnoreStatus();
    
    /** Updates the filter of the filtered task list to show tasks at a given venue*/
    void updateFilteredTaskListByVenue(String venue);
    
    /** Updates the filter of the filtered task list to show tasks at a given venue*/
    void updateFilteredTaskListByTag(Tag tag);
    
}
