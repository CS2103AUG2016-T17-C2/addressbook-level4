package seedu.task.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.DuplicateDataException;
import seedu.task.commons.util.CollectionUtil;
import seedu.task.model.ModelManager;

import java.util.*;

/**
 * A list of tasks that enforces uniqueness between its elements and does not
 * allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

	/**
	 * Signals that an operation would have violated the 'no duplicates'
	 * property of the list.
	 */
	public static class DuplicateTaskException extends DuplicateDataException {
		protected DuplicateTaskException() {
			super("Operation would result in duplicate tasks");
		}
	}

	/**
	 * Signals that the task would clash with another task.
	 */
	public static class DateClashTaskException extends DuplicateDataException {
		protected DateClashTaskException(String taskName) {
			super("The Start Date and End date clashes with another task '" + taskName + "'");
		}
	}

	/**
	 * Signals that an operation targeting a specified task in the list would
	 * fail because there is no such matching task in the list.
	 */
	public static class TaskNotFoundException extends Exception {
	}

	private final ObservableList<Task> internalList = FXCollections.observableArrayList();

	/**
	 * Constructs empty taskList.
	 */
	public UniqueTaskList() {
	}

	/**
	 * Returns true if the list contains an equivalent task as the given
	 * argument.
	 */
	public boolean contains(ReadOnlyTask toCheck) {
		assert toCheck != null;
		return internalList.contains(toCheck);
	}

	/**
	 * Returns the task if a clash in dates have been found
	 */
	public Task isDateClash(Task task) {
		if (!task.getStartDate().value.isEmpty() && !task.getEndDate().value.isEmpty()) {
			for (Task t : internalList) {
				if ((t != task) && task.checkDateClash(t))
					return t;
			}
		}
		return null;
	}

	/**
	 * Returns the task if a clash in dates have been found. It doesn't check
	 * with the task in the specified index
	 */
	public Task isDateClash(int index, Task task) {
		if (!task.getStartDate().value.isEmpty() && !task.getEndDate().value.isEmpty()) {
			for (int i = 0; i < internalList.size(); i++) {
				if ((i != index) && task.checkDateClash(internalList.get(i)))
					return internalList.get(i);
			}
		}
		return null;
	}

	/**
	 * Adds a task to the list.
	 *
	 * @throws DuplicateTaskException
	 *             if the task to add is a duplicate of an existing task in the
	 *             list.
	 * @throws DateClashTaskException
	 *             if the task dates clashes with another existing task in the
	 *             list
	 */
	public int add(Task toAdd) throws DuplicateTaskException, DateClashTaskException {
		assert toAdd != null;
		if (contains(toAdd)) {
			throw new DuplicateTaskException();
		}
		Task dateClash = isDateClash(toAdd);
		if (dateClash != null)
			throw new DateClashTaskException(dateClash.getName().toString());

		internalList.add(toAdd);
		return internalList.size() - 1;
	}

	/**
	 * Adds a task to the list in the specified index.
	 *
	 * @throws DuplicateTaskException
	 *             if the task to add is a duplicate of an existing task in the
	 *             list.
	 * @throws DateClashTaskException
	 *             if the task dates clashes with another existing task in the
	 *             list
	 */
	public int add(int index, Task toAdd) throws DuplicateTaskException, DateClashTaskException {
		assert toAdd != null;
		if (contains(toAdd)) {
			throw new DuplicateTaskException();
		}
		Task dateClash = isDateClash(toAdd);
		if (dateClash != null)
			throw new DateClashTaskException(dateClash.getName().toString());

		internalList.add(index, toAdd);
		return internalList.size() - 1;
	}

	/**
	 * Updates a task in the list.
	 *
	 * @throws DateClashTaskException
	 *             if the task dates clashes with another existing task in the
	 *             list
	 */
	public void update(int index, Task toUpdate) throws DateClashTaskException {
		assert toUpdate != null;
		Task dateClash = isDateClash(index, toUpdate);
		//if (dateClash != null)
			//throw new DateClashTaskException(dateClash.getName().toString());
		internalList.set(index, toUpdate);
	}

	/**
	 * Removes the equivalent task from the list.
	 *
	 * @throws TaskNotFoundException
	 *             if no such task could be found in the list.
	 */
	public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
		assert toRemove != null;
		final boolean taskFoundAndDeleted = internalList.remove(toRemove);
		if (!taskFoundAndDeleted) {
			throw new TaskNotFoundException();
		}
		return taskFoundAndDeleted;
	}

	public ObservableList<Task> getInternalList() {
		return internalList;
	}

	@Override
	public Iterator<Task> iterator() {
		return internalList.iterator();
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
		        || (other instanceof UniqueTaskList // instanceof handles nulls
		                && this.internalList.equals(((UniqueTaskList) other).internalList));
	}

	@Override
	public int hashCode() {
		return internalList.hashCode();
	}
}
