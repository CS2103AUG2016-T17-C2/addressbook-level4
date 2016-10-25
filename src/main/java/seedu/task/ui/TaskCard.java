package seedu.task.ui;

//@@author A0138301U
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import seedu.task.model.task.TaskPriority;
import seedu.task.model.task.PinTask;
import seedu.task.model.task.ReadOnlyTask;
import seedu.task.model.task.Status;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class TaskCard extends UiPart{

    private static final String PIN_IMAGE = "/images/pinicon.png";
    private static final String DONE_IMAGE = "/images/done.png";
    private static final String EXPIRED_IMAGE = "/images/expired.png";
    private static final String IGNORE_IMAGE = "/images/ignore.png";
    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox taskContainer;
    @FXML
    private HBox imageContainer;
    @FXML
    private Label name;
    @FXML
    private Label venue;
    @FXML
    private Label id;
    @FXML
    private Label dateTime;
    @FXML
    private Label priority;
    @FXML
    private Label status;
    @FXML
    private Label tags;
    
    private ReadOnlyTask task;
    private int displayedIndex;
    
    private String high = "#0288D1";
    private String medium = "#81D4FA";
    private String low = "#E1F5FE";
    
    private final int radii = 10;
    
    public TaskCard(){


    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex){
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(task.getName().fullName);
        setTextForVenue();
        setTextForDate();
        id.setText(displayedIndex + ". ");
        tags.setText(task.tagsString());
        setBackgroundColor();
        
        addIconsForStatusAndPin();
    }

    private void addIconsForStatusAndPin() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        imageContainer.getChildren().add(region); //to push either 1 or 2 icons to the right
        if(task.getStatus().equals(Status.DONE)) {
            ImageView done = new ImageView(new Image(DONE_IMAGE));
            imageContainer.getChildren().add(done);
        } else if(task.getStatus().equals(Status.EXPIRED)) {
            ImageView expired = new ImageView(new Image(EXPIRED_IMAGE));
            imageContainer.getChildren().add(expired);
        } else if(task.getStatus().equals(Status.IGNORE)) {
            ImageView ignore = new ImageView(new Image(IGNORE_IMAGE));
            imageContainer.getChildren().add(ignore);
        }
        if(task.getPinTask().equals(PinTask.PIN)) {
            ImageView pinimage = new ImageView(new Image(PIN_IMAGE));
            imageContainer.getChildren().add(pinimage);
        }
    }

    private void setBackgroundColor() {
        if(task.getPriority().equals(TaskPriority.HIGH)) {
            gridPane.setBackground(new Background(new BackgroundFill(Color.valueOf(high), new CornerRadii(radii), new Insets(3,3,3,3))));
        } else if(task.getPriority().equals(TaskPriority.MEDIUM)) {
            gridPane.setBackground(new Background(new BackgroundFill(Color.valueOf(medium), new CornerRadii(radii), new Insets(3,3,3,3))));
        } else {
            gridPane.setBackground(new Background(new BackgroundFill(Color.valueOf(low), new CornerRadii(radii), new Insets(3,3,3,3))));
        }
    }

    private void setTextForDate() {
        if(!task.getStartDate().value.isEmpty()){
            if(!task.getEndDate().value.isEmpty()) {
                dateTime.setText(task.getStartDate().value + " till " + task.getEndDate().value);        
            } else {
                dateTime.setText("From: " + task.getStartDate().value);
            }
        } else {
            if(task.getEndDate().value.isEmpty()){
                dateTime.setManaged(false); //remove field from layout if empty
            } else {
                dateTime.setText("Due by: " + task.getEndDate().value); 
            }
        }
    }

    private void setTextForVenue() {
        if(task.getVenue().value.isEmpty()){
            venue.setManaged(false); //remove field from layout if empty
        } else {
            venue.setText("Venue: " + task.getVenue().value);
        }
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
//@@author
