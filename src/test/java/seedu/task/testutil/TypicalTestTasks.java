package seedu.task.testutil;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.model.TaskBook;
import seedu.task.model.task.*;

/**
 *
 */
public class TypicalTestTasks {

	public static TestTask study, play, coding, watchMovie;

	public TypicalTestTasks() {
		try {
			Date date = DateUtils.addDays(new Date(), 1);
			date = DateUtils.setMinutes(date, 0);
			date = DateUtils.setSeconds(date, 0);

			study = new TaskBuilder().withName("Study time").withVenue("School")
			        .withStartDate(DateUtils.setHours(date, 10)).withEndDate(DateUtils.setHours(date, 11))
			        .withPriority("High").withStatus("Active").withPinTask("pin").withTags("study", "exam").build();

			play = new TaskBuilder().withName("Play time").withVenue("Playground")
			        .withStartDate(DateUtils.setHours(date, 12)).withEndDate(DateUtils.setHours(date, 13))
			        .withPriority("Low").withStatus("Active").withPinTask("pin").withTags("play", "healthy").build();
			
			coding = new TaskBuilder().withName("code cs2103").withVenue("School")
			        .withStartDate(DateUtils.setHours(date, 14)).withEndDate(DateUtils.setHours(date, 16))
			        .withPriority("High").withStatus("Active").withPinTask("unpin").withTags("study", "school").build();

			
		} catch (IllegalValueException e) {
			e.printStackTrace();
			assert false : "not possible";
		}
	}

	public static void loadTaskBookWithSampleData(TaskBook tb) {

		try {
			tb.addTask(new Task(study));
			tb.addTask(new Task(play));
			tb.addTask(new Task(coding));

		} catch (UniqueTaskList.DuplicateTaskException e) {
			assert false : "not possible";
		} catch (UniqueTaskList.DateClashTaskException e) {
			assert false : "not possible";
		}
	}

	public TestTask[] getTypicalTasks() {
		return new TestTask[] { study, play, coding };
	}

	public TaskBook getTypicalTaskBook() {
		TaskBook tb = new TaskBook();
		loadTaskBookWithSampleData(tb);
		return tb;
	}
}
