package seedu.address.logic;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertEquals;


import seedu.address.commons.util.ConfigUtilTest;
import seedu.task.commons.core.Config;
import seedu.task.logic.commands.AddCommand;
import seedu.task.logic.commands.CommandResult;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.DateTime;
import seedu.task.model.task.Name;
import seedu.task.model.task.PinTask;
import seedu.task.model.task.Status;
import seedu.task.model.task.Task;
import seedu.task.model.task.TaskPriority;
import seedu.task.model.task.Venue;
import seedu.task.storage.StorageManager;
import seedu.task.testutil.TestTask;

public class CommandTest {
    
    Config config = ConfigUtilTest.getTypicalConfig();
    StorageManager storage = new StorageManager(config.getTaskBookFilePath(), config.getUserPrefsFilePath());
     Model model = new ModelManager();
     @Rule
     public ExpectedException thrown = ExpectedException.none();

    @Test
     public void null_task_AddCommand(){
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }
    

    
    


}
