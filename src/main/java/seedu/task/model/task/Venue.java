package seedu.task.model.task;

//@@author A0139958H

/**
 * Represents a Task's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isInValidVenue(String)}
 */
public class Venue {
    
    public static final String MESSAGE_VENUE_CONSTRAINTS = "Task venue cannot be empty!";
    public static final String VENUE_VALIDATION_REGEX = "";

    public final String value;

    /**
     * Validates given venue. Venue can be null
     */
    public Venue(String venue) {
        this.value = venue;
    }

    /**
     * Returns true if a given string is empty.
     */
    public static boolean isInValidVenue(String venue) {
        return venue.isEmpty();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Venue // instanceof handles nulls
                && this.value.equals(((Venue) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}