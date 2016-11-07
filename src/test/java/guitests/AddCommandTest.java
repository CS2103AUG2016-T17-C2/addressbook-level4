package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import seedu.task.commons.core.Messages;
import seedu.task.logic.commands.AddCommand;
import seedu.task.model.task.Task;
import seedu.task.model.task.UniqueTaskList;
import seedu.task.testutil.TestTask;
import seedu.task.testutil.TestUtil;
import seedu.task.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends TaskBookGuiTest {
    //@@author A0138301U
    
    @Test
    public void add() {
        //add one task
        TestTask taskToAdd = TypicalTestTasks.play;
        TestTask[] currentList = td.getTypicalTasks();
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add task with dates clashing with other tasks
        taskToAdd = TypicalTestTasks.coding;
        commandBox.runCommand(taskToAdd.getAddCommand());
        
        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.play); 

        //invalid command
        commandBox.runCommand("adds get a massage");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        //@@author
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getName().fullName);
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
