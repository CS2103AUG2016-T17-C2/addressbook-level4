package guitests;

import guitests.guihandles.HelpWindowHandle;
import seedu.task.logic.commands.HelpCommand;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HelpWindowTest extends TaskBookGuiTest {

    @Test
    public void openHelpWindow() {

        taskListPanel.clickOnListView();

        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());

        assertHelpWindowOpen(mainMenu.openHelpWindowUsingMenu());

        assertHelpWindowOpen(commandBox.runHelpCommand());
        //@@author A0138301U
        assertResultMessage(String.format(HelpCommand.SHOWING_HELP_MESSAGE));
        //@@author
    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }
}
