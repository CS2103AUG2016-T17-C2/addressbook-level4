package guitests;

import org.junit.Test;

import seedu.task.commons.core.Messages;
import seedu.task.logic.commands.FindCommand;
import seedu.task.testutil.TestTask;
import seedu.task.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;
import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class FindCommandTest extends TaskBookGuiTest {

    //@@author A0138301U
    @Test
    public void find_nonEmptyList() {
        assertFindResult("find life"); //no results
        assertFindResult("find time", TypicalTestTasks.study, TypicalTestTasks.play); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find time", TypicalTestTasks.play);
    }
    //@@author
    
    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find work"); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findfood");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void find_Priority() {
        assertFindResult("find #medium");
        assertFindResult("find #low", TypicalTestTasks.play);       
        assertFindResult("find #high", TypicalTestTasks.study, TypicalTestTasks.coding);      
    }

    @Test
    public void find_Status() {
        assertFindResult("find #done");
        assertFindResult("find #expired");
        assertFindResult("find #ignore");
        assertFindResult("find #active", TypicalTestTasks.study, TypicalTestTasks.play, TypicalTestTasks.coding);  
        commandBox.runCommand("find #");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));    
    }

    @Test
    public void find_Venue() {
        assertFindResult("find @life");
        commandBox.runCommand("find @");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE)); 
        assertFindResult("find @Playground", TypicalTestTasks.play);        
    }

/*    @Test
    public void find_Tagging() {
        assertFindResult("find #fitness"); 
        assertFindResult("find #healthy", TypicalTestTasks.play);     
        assertFindResult("find #study", TypicalTestTasks.study, TypicalTestTasks.coding);        
    }*/

    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
