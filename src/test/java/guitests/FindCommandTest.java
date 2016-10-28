package guitests;

import org.junit.Test;

import seedu.task.commons.core.Messages;
import seedu.task.testutil.TestTask;
import seedu.task.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;

public class FindCommandTest extends TaskBookGuiTest {

    @Test
    public void find_nonEmptyList() {
        assertFindResult("find life"); //no results
/*        assertFindResult("find time", TypicalTestTasks.study, TypicalTestTasks.play); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find time",TypicalTestTasks.study);*/
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find work"); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findfood");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
