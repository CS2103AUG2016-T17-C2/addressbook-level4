package seedu.task.ui;
import javafx.geometry.Rectangle2D;
//@@author A0138301U
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
 * Controller for a Google Maps window
 */
public class GoogleMapsWindow extends UiPart {

    private static final Logger logger = LogsCenter.getLogger(GoogleMapsWindow.class);
    private static final String ICON = "/images/map.png";
    private static final String FXML = "GoogleMapsWindow.fxml";
    private static final String TITLE = "Google Maps";
    private static final String USERGUIDE_URL =
            "http://maps.google.com/?q=";
    
    private AnchorPane mainPane;

    private Stage dialogStage;

    public static GoogleMapsWindow load(Stage primaryStage, String venue) {
        logger.fine("Showing Google Maps window for task.");
        GoogleMapsWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new GoogleMapsWindow());
        helpWindow.configure(venue);
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

    private void configure(String venue){
        Scene scene = new Scene(mainPane);
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        
        //@@author A0138301U
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        dialogStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 900);//align the window to the
        dialogStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 900);//bottom right of the screen
        dialogStage.setHeight(900);//set the dimensions at 900 by 900
        dialogStage.setWidth(900);
        //@@author
        setIcon(dialogStage, ICON);

        String venueQuery = venue.replace(' ', '+');

        WebView browser = new WebView();
        browser.getEngine().load(USERGUIDE_URL+venueQuery);
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(browser);
    }

    public void show() {
        dialogStage.showAndWait();
    }
}
