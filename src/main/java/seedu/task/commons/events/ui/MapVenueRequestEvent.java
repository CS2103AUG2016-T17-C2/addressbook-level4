package seedu.task.commons.events.ui;
//@@author A0138301U
import seedu.task.commons.events.BaseEvent;

/**
 * An event find venue of a task via Google Maps.
 */
public class MapVenueRequestEvent extends BaseEvent {

    public final String venue;

    public MapVenueRequestEvent(String venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
