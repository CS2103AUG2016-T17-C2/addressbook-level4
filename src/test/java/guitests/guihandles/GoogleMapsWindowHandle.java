package guitests.guihandles;

//@@author A0138301U
import guitests.GuiRobot;
import javafx.stage.Stage;

/**
 * Provides a handle to the Google Maps window of the app.
 */
public class GoogleMapsWindowHandle extends GuiHandle {

    private static final String HELP_WINDOW_TITLE = "Google Maps";
    private static final String HELP_WINDOW_ROOT_FIELD_ID = "#googleMapsRoot";

    public GoogleMapsWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, HELP_WINDOW_TITLE);
        guiRobot.sleep(1000);
    }

    public boolean isWindowOpen() {
        return getNode(HELP_WINDOW_ROOT_FIELD_ID) != null;
    }

    public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
