package seedu.task.testutil;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.TaskBook;
import seedu.task.model.tag.Tag;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.model.task.UniqueTaskList.DateClashTaskException;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code TaskBook ab = new TaskBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class TaskBookBuilder {

    private TaskBook taskBook;

    public TaskBookBuilder(TaskBook taskBook){
        this.taskBook = taskBook;
    }

    public TaskBookBuilder withPerson(Task task) throws UniqueTaskList.DuplicateTaskException, DateClashTaskException {
        taskBook.addTask(task);
        return this;
    }

    public TaskBookBuilder withTag(String tagName) throws IllegalValueException {
        taskBook.addTag(new Tag(tagName));
        return this;
    }

    public TaskBook build(){
        return taskBook;
    }
}
