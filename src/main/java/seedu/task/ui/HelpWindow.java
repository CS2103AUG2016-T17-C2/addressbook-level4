package seedu.task.ui;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.util.FxViewUtil;

import java.util.logging.Logger;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "HelpWindow.fxml";
    private static final String TITLE = "Help";
    //@@author A0138301U
    private static final String USERGUIDE_URL =
            "https://github.com/CS2103AUG2016-T17-C2/main/blob/master/docs/UserGuide.md";
    //@@author
    
    private AnchorPane mainPane;

    private Stage dialogStage;

    public static HelpWindow load(Stage primaryStage) {
        logger.fine("Showing help page about the application.");
        HelpWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new HelpWindow());
        helpWindow.configure();
        return helpWindow;
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    private void configure(){
        Scene scene = new Scene(mainPane);
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        //@@author A0138301U
        //aligns the window to the top right hand corner of screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        dialogStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 1000);
        dialogStage.setY(0);
        dialogStage.setHeight(1000);//set the dimensions at 1000 by 1000
        dialogStage.setWidth(1000);
        //@@author
        setIcon(dialogStage, ICON);

        WebView browser = new WebView();
        browser.getEngine().load(USERGUIDE_URL);
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(browser);
    }

    //@@author A0138301U
    //changed from showAndWait to show so that result message of command is shown earlier
    public void show() {
        dialogStage.show();
    }
}
