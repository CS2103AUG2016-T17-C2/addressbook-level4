package seedu.task.model.task;

import java.util.Date;

import seedu.task.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's start date/end date in the task book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class DateTime {

    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be an upcoming date";
    public static final String MESSAGE_DATE_SAME = "Start date and End date should not be same";
    public static final String MESSAGE_MULTIPLE_START_DATE = "There are multiple start dates in your input. Please enter a valid task!";
    public static final String MESSAGE_MULTIPLE_END_DATE = "There are multiple end dates in your input. Please enter a valid task!";
    public static final String MESSAGE_INVALID_START_DATE = "Start Date should be before End Date!";

    
    public final String value;
    

    /**
     * Validates given Date.
     *
     * @throws IllegalValueException if given phone string is invalid.
     */
    public DateTime(Date date) throws IllegalValueException {
        if (!isValidDate(date)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.value = date.toString();
    }

    public DateTime (String date){
        this.value = date;
    }
    
    
    /**
     * Returns true if a given Date is valid and in future
     */
    public static boolean isValidDate(Date date) {
        return date.after(new Date());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTime // instanceof handles nulls
                && this.value.equals(((DateTime) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
