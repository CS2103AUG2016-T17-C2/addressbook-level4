package guitests;

import org.junit.Test;

import seedu.task.logic.commands.ClearCommand;
import seedu.task.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;

public class ClearCommandTest extends TaskBookGuiTest {
    //@@author A0138301U
    private final String clear = "clear";

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(TypicalTestTasks.play.getAddCommand());
        assertTrue(taskListPanel.isListMatching(TypicalTestTasks.play));
        commandBox.runCommand("delete 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand(clear);
        assertListSize(0);
        assertResultMessage(ClearCommand.MESSAGE_SUCCESS);
    }
}
