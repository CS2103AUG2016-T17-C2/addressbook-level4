package guitests.guihandles;

//@@author A0138301U
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.Duration;
import org.ocpsoft.prettytime.PrettyTime;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import seedu.task.model.task.ReadOnlyTask;

/**
 * Provides a handle to a person card in the person list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String NAME_FIELD_ID = "#name";
    private static final String VENUE_FIELD_ID = "#venue";
    private static final String DATETIME_FIELD_ID = "#dateTime";
    private static final String PRIORITY_FIELD_ID = "#priority";
    private static final String STATUS_FIELD_ID = "#status";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getTaskName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }

    public String getVenue() {
        return getTextFromLabel(VENUE_FIELD_ID);
    }

    public String getDateTime() {
        return getTextFromLabel(DATETIME_FIELD_ID);
    }

    public String getPriority() {
        return getTextFromLabel(PRIORITY_FIELD_ID);
    }

    public String getStatus() {
        return getTextFromLabel(STATUS_FIELD_ID);
    }


    public boolean isSameTask(ReadOnlyTask task){
        String dateTime;
        if(!task.getStartDate().value.isEmpty()){
            if(!task.getEndDate().value.isEmpty()) {
                dateTime = toPrettyDate(task.getStartDate().value) + " till " + toPrettyDate(task.getEndDate().value);        
            } else {
                dateTime = "From: " + toPrettyDate(task.getStartDate().value);
            }
        } else {
            if(task.getEndDate().value.isEmpty()){
                dateTime = ""; //remove field from layout if empty
            } else {
                dateTime = "Due by: " + toPrettyDate(task.getEndDate().value); 
            }
        }
        return getTaskName().equals(task.getName().fullName) && getDateTime().equals(dateTime)
                && getVenue().equals(task.getVenue().toString()); //cannot check priority and status as these values are no longer displayed 
                                                                  //as labels in current TaskCard
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getTaskName().equals(handle.getTaskName())
                    && getVenue().equals(handle.getVenue()); //TODO: compare the rest
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTaskName() + " " + getVenue();
    }

    //========== Functions for PrettyTime conversion of date ==================================================
    
    public String toPrettyDate(String date){
        
        Date dateFromParsedDate = convertStringToDateObject(date);
        Date tomorrow = getTommorrow();
        
        if (!dateFromParsedDate.after(tomorrow)){
           PrettyTime p = new PrettyTime();
           List<Duration> durations = p.calculatePreciseDuration(dateFromParsedDate);
        return p.format(durations);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat ("E dd.MM.yyyy 'at' hh:mm a");
            return sdf.format(dateFromParsedDate);
        }

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
}
