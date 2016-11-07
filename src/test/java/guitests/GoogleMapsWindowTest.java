package guitests;

//@@author A0138301U
import guitests.guihandles.GoogleMapsWindowHandle;
import seedu.task.commons.core.Messages;
import org.junit.Test;
import seedu.task.logic.commands.MapCommand;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class GoogleMapsWindowTest extends TaskBookGuiTest {

    @Test
    public void openGoogleMapsWindow_success() {
        assertGoogleMapsWindowOpen(commandBox.runGoogleMapCommand(1));
        assertResultMessage(String.format(MapCommand.MESSAGE_MAP_TASK_SUCCESS, 1));
    }
    
    @Test
    public void openGoogleMapsWindow_invalidIndex() {
        int targetIndex = 4;
        commandBox.runCommand("map " + targetIndex);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX,
                Arrays.toString(new int[]{targetIndex})));
    }    
    
    @Test
    public void openGoogleMapsWindow_noVenue() {
        int targetIndex = 4;
        commandBox.runCommand("add study #low");
        commandBox.runCommand("map " + targetIndex);
        assertResultMessage(String.format(MapCommand.MESSAGE_MAP_TASK_FAILURE, targetIndex));
    }


    private void assertGoogleMapsWindowOpen(GoogleMapsWindowHandle googleMapsWindowHandle) {
        assertTrue(googleMapsWindowHandle.isWindowOpen());
        googleMapsWindowHandle.closeWindow();
    }
}
