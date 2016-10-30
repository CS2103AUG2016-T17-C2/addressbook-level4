package seedu.task.ui;

//@@author A0141064U
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
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

/**
 * The TaskCard. Provides the details for the layout of details displayed for each Task,
 * and the colour and icons on each TaskCard.
 */

public class TaskCard extends UiPart {

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

    private static final String HIGH = "#0D47A1";
    private static final String MEDIUM = "#1976D2";
    private static final String LOW = "#2196F3";

    private static final String DONE = "#212121";
    private static final String IGNORE = "#616161";
    private static final String EXPIRED = "#D32F2F";
    
    private static final CornerRadii CORNERRADII = new CornerRadii(0);
    
    private static final Insets INSETS = new Insets(1,1,1,1);

    public TaskCard() {

    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex) {
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
        id.setText("   " + displayedIndex + " ");
        tags.setText(task.tagsString());
        
        setBackgroundColor();

        addIconsForStatusAndPin();
    }

/**    add icons depending on status of Task*/
    private void addIconsForStatusAndPin() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        imageContainer.getChildren().add(region); // to right align all added icons
        
        if (task.getStatus().equals(Status.DONE)) {
            ImageView done = new ImageView(new Image(DONE_IMAGE));
            imageContainer.getChildren().add(done);
        } else if (task.getStatus().equals(Status.EXPIRED)) {
            ImageView expired = new ImageView(new Image(EXPIRED_IMAGE));
            imageContainer.getChildren().add(expired);
        } else if (task.getStatus().equals(Status.IGNORE)) {
            ImageView ignore = new ImageView(new Image(IGNORE_IMAGE));
            imageContainer.getChildren().add(ignore);
        }
        if (task.getPinTask().equals(PinTask.PIN)) {
            ImageView pinimage = new ImageView(new Image(PIN_IMAGE));
            imageContainer.getChildren().add(pinimage);
        }
    }

/**    Sets the background colour based on the status or priority of the Task*/
    private void setBackgroundColor() {
        if(task.getStatus().equals(Status.DONE)) {
            gridPane.setBackground(new Background(
                    new BackgroundFill(Color.valueOf(DONE), CORNERRADII, INSETS)));
        } else if(task.getStatus().equals(Status.EXPIRED)) {
            gridPane.setBackground(new Background(
                    new BackgroundFill(Color.valueOf(EXPIRED), CORNERRADII, INSETS)));
        } else if(task.getStatus().equals(Status.IGNORE)) {
            gridPane.setBackground(new Background(
                    new BackgroundFill(Color.valueOf(IGNORE), CORNERRADII, INSETS)));
        } else if(task.getPriority().equals(TaskPriority.HIGH)) {
            gridPane.setBackground(new Background(
                    new BackgroundFill(Color.valueOf(HIGH), CORNERRADII, INSETS)));
        } else if(task.getPriority().equals(TaskPriority.MEDIUM)) {
            gridPane.setBackground(new Background(
                    new BackgroundFill(Color.valueOf(MEDIUM), CORNERRADII, INSETS)));
        } else {
            gridPane.setBackground(new Background(
                    new BackgroundFill(Color.valueOf(LOW), CORNERRADII, INSETS)));
        }
    }

    //@@author A0141064U
    private void setTextForDate() {
        if (!task.getStartDate().value.isEmpty()) {
            if (!task.getEndDate().value.isEmpty()) {
                if (toSimpleDateFormat(task.getStartDate().value)
                        .compareTo(toSimpleDateFormat(task.getEndDate().value)) != 0) {
                    dateTime.setText(toSimpleTimeFormat(task.getStartDate().value) + " - "
                            + toSimpleTimeFormat(task.getEndDate().value) + System.lineSeparator()
                            + toSimpleDateFormat(task.getStartDate().value) + " - "
                            + toSimpleDateFormat(task.getEndDate().value) + System.lineSeparator()
                            + toPrettyDate(task.getStartDate().value));
                } else {
                    dateTime.setText(toSimpleTimeFormat(task.getStartDate().value) + " - "
                            + toSimpleTimeFormat(task.getEndDate().value) + System.lineSeparator()
                            + toSimpleDateFormat(task.getStartDate().value) + System.lineSeparator()
                            + toPrettyDate(task.getStartDate().value));
                }
            } else {
                dateTime.setText(toSimpleTimeFormat(task.getStartDate().value) + " "
                        + toSimpleDateFormat(task.getStartDate().value) + System.lineSeparator()
                        + toPrettyDate(task.getStartDate().value));
            }
        } else {
            if (task.getEndDate().value.isEmpty()) {
                dateTime.setManaged(false); // remove field from layout if empty
            } else {
                dateTime.setText(toSimpleTimeFormat(task.getEndDate().value) + " "
                        + toSimpleDateFormat(task.getEndDate().value));
            }
        }
    }

    /*
     * Converts the dateTime to prettyTime format if the dateTime is less than
     * 24hr from current time
     */
    public String toPrettyDate(String date) {

        Date dateFromParsedDate = convertStringToDateObject(date);
        Date tomorrow = getTommorrow();

        if (!dateFromParsedDate.after(tomorrow)) {
            PrettyTime p = new PrettyTime();
            // List<Duration> durations =
            // p.calculatePreciseDuration(dateFromParsedDate);
            return p.format(dateFromParsedDate);
        } else {
            return null;
        }
    }

    public String toSimpleDateFormat(String date) {
        Date dateFromParsedDate = convertStringToDateObject(date);
        SimpleDateFormat sdf = new SimpleDateFormat("E dd.MM.yyyy");
        return sdf.format(dateFromParsedDate);
    }

    public String toSimpleTimeFormat(String date) {
        Date dateFromParsedDate = convertStringToDateObject(date);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(dateFromParsedDate);
    }

    private Date getTommorrow() {
        int tomorrowAsInt = 1;
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, tomorrowAsInt);
        Date tomorrow = c.getTime();
        return tomorrow;
    }

    private Date convertStringToDateObject(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d HH:mm:ss zzz yyyy");
        LocalDateTime parsedDate = LocalDateTime.parse(date, formatter);
        Date dateFromParsedDate = Date.from(parsedDate.atZone(ZoneId.systemDefault()).toInstant());
        return dateFromParsedDate;
    }
    // @@author A0138301U

    private void setTextForVenue() {
        if (task.getVenue().value.isEmpty()) {
            venue.setManaged(false); // remove field from layout if empty
        } else {
            venue.setText("Venue: " + task.getVenue().value);
        }
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
// @@author
