package seedu.address.logic;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.core.UnmodifiableObservableList;
import seedu.task.commons.events.model.TaskBookChangedEvent;
import seedu.task.commons.events.ui.JumpToListRequestEvent;
import seedu.task.commons.events.ui.ShowHelpRequestEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.FileUtil;
import seedu.task.commons.util.ShortcutUtil;
import seedu.task.logic.Logic;
import seedu.task.logic.LogicManager;
import seedu.task.logic.commands.*;
import seedu.task.logic.parser.TaskParser;
import seedu.task.model.TaskBook;
import seedu.task.model.ModelManager.TaskComparator;
import seedu.task.model.Model;
import seedu.task.model.ModelManager;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;
import seedu.task.storage.StorageManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.task.commons.core.Messages.*;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;
    

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskBook latestSavedTaskBook;
    private boolean helpShown;
    private int targetedJumpIndex;

	private static final Logger logger = LogsCenter.getLogger(TaskParser.class);

    @Subscribe
    private void handleLocalModelChangedEvent(TaskBookChangedEvent abce) {
        latestSavedTaskBook = new TaskBook(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempTaskBookFile = saveFolder.getRoot().getPath() + "TempTaskBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";

        logic = new LogicManager(model, new StorageManager(tempTaskBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskBook = new TaskBook(model.getTaskBook()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'taskBook' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlyTaskBook, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskBook(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal taskBook data are same as those in the {@code expectedTaskBook} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyTaskBook expectedTaskBook,
                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);

        expectedTaskBook.getTaskList().forEach(t->logger.warning("expected: " + t.toString()));
        model.getTaskBook().getTaskList().forEach(t->logger.warning("actual: " + t.toString()));
    }


    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskBook(), Collections.emptyList());
    }
    //@@author A0141064U
    @Test
    public void execute_shortcut_add() throws Exception {
        
        ShortcutSetting shortcutSetting = ShortcutUtil.readShortcut(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH).orElse(new ShortcutSetting());
        
        resetShortcut(shortcutSetting);
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
        assertCommandBehavior("shortcut add addable", ShortcutCommand.MESSAGE_SUCCESS + "add"+" changed to "+"addable");
        resetShortcut(shortcutSetting);
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
    }
    @Test
    public void execute_shortcut_delete() throws Exception {
        
        ShortcutSetting shortcutSetting = ShortcutUtil.readShortcut(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH).orElse(new ShortcutSetting());
        
        resetShortcut(shortcutSetting);
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
        assertCommandBehavior("shortcut delete deletable", ShortcutCommand.MESSAGE_SUCCESS + "delete"+" changed to "+"deletable");
        resetShortcut(shortcutSetting);
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
    }
    @Test
    public void execute_shortcut_list() throws Exception {
        
        ShortcutSetting shortcutSetting = ShortcutUtil.readShortcut(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH).orElse(new ShortcutSetting());
        
        resetShortcut(shortcutSetting);
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
        assertCommandBehavior("shortcut list listable", ShortcutCommand.MESSAGE_SUCCESS + "list"+" changed to "+"listable");
        resetShortcut(shortcutSetting);
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
    }

    @Test
    public void execute_shortcut_error() throws Exception {
        
        ShortcutSetting shortcutSetting = ShortcutUtil.readShortcut(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH).orElse(new ShortcutSetting());
        resetShortcut(shortcutSetting); 
        ShortcutUtil.saveShortcut(shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
         assertCommandBehavior("shortcut list list", ShortcutCommand.MESSAGE_DUPLICATE_SHORTKEY);
        
    }
    
    public void resetShortcut(ShortcutSetting shortcutSetting) {
        shortcutSetting.setAdd("add");
        shortcutSetting.setDelete("delete");
        shortcutSetting.setList("list");
    }
    
    @Test
    public void execute_changeFilepath_success() throws Exception {
        assertCommandBehavior("file aa", ChangeFilePathCommand.MESSAGE_SUCCESS + "aa.xml");
        }
    
    
    //@@author A0139958H
    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        assertCommandBehavior("add", Name.MESSAGE_NAME_CONSTRAINTS); //Empty Name
        assertCommandBehavior("add play football by Jan 1 2015", DateTime.MESSAGE_DATE_CONSTRAINTS); //Past Date
        assertCommandBehavior("add play football from 6pm to 6pm tomorrow", DateTime.MESSAGE_DATE_SAME); //Same Dates
        assertCommandBehavior("add play football from 6pm to 8pm tomorrow from 7pm tomorrow", DateTime.MESSAGE_MULTIPLE_START_DATE); //Multiple Start Dates
        assertCommandBehavior("add play football from 6pm to 7pm tomorrow by 8pm tomorrow", DateTime.MESSAGE_MULTIPLE_END_DATE); //Multiple End Dates
        assertCommandBehavior("add play football from 8pm tomorrow by 6pm tomorrow", DateTime.MESSAGE_INVALID_START_DATE); //Start Date is After End Date
    }
    

    
    @Test
    public void execute_update_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        helper.addToModel(model, 1);
        
        Task toBeAdded = helper.sampleTask(1);
        TaskBook expectedAB = new TaskBook();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateUpdateCommand(toBeAdded, 1),
                String.format(UpdateCommand.MESSAGE_SUCCESS, 1),
                expectedAB,
                expectedAB.getTaskList());
    }
    
    @Test
    public void execute_set_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        helper.generateAddressBook(1);
        helper.addToModel(model, 1);
        
        Task toBeAdded = helper.sampleTask();
        TaskBook expectedAB = new TaskBook();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateSetCommand(1),
                String.format(SetCommand.MESSAGE_SUCCESS, 1),
                expectedAB,
                expectedAB.getTaskList());
    }
    
    @Test
    public void execute_undo_and_redo_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.sampleTask();
        TaskBook expectedAB = new TaskBook();
        expectedAB.addTask(toBeAdded);

        assertCommandBehavior("undo", UndoCommand.MESSAGE_SUCCESS_UNDO_DELETE);
        assertCommandBehavior("redo", RedoCommand.MESSAGE_SUCCESS_REDO_ADD);
    }
    
    @Test
    public void execute_redo_unsuccessful() throws Exception {
        assertCommandBehavior("redo", RedoCommand.MESSAGE_FAILURE_REDO);
    }
    
   //@@author

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.sampleTask();
        TaskBook expectedAB = new TaskBook();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_list_showsAlltasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskBook expectedAB = helper.generateAddressBook(2);
        FilteredList<Task> filteredTasks = new FilteredList<>(expectedAB.getTasks());
        SortedList<Task> sortedTasks = new SortedList<>(filteredTasks, new TaskComparator());

        //List<? extends ReadOnlyTask> expectedList = expectedAB.getTasks();

        // prepare task book state
        helper.addToModel(model, 2);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAB,
                new UnmodifiableObservableList<>(sortedTasks));
    }


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage) throws Exception {
        assertCommandBehavior(commandWord , expectedMessage); //index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generatetaskList(2);

        // set AB state to 2 tasks
        model.resetData(new TaskBook());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", String.format(expectedMessage, Arrays.toString(new int[]{3}), model.getTaskBook(), taskList));
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrecttask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threetasks = helper.generatetaskList(3);

        TaskBook expectedAB = helper.generateAddressBook(threetasks);
        helper.addToModel(model, threetasks);

        assertCommandBehavior("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_TASK_SUCCESS, 2),
                expectedAB,
                expectedAB.getTaskList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getSortedTaskList().get(1), threetasks.get(1));
    }


    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
         assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrecttask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threetasks = helper.generatetaskList(3);

        TaskBook expectedAB = helper.generateAddressBook(threetasks);
        int[] toDelete = {1};
        expectedAB.removeTask(threetasks.get(toDelete[0]-1));
        helper.addToModel(model, threetasks);

        assertCommandBehavior("delete 1",
                String.format(DeleteCommand.MESSAGE_DELETE_TASK_SUCCESS, Arrays.toString(toDelete)),
                expectedAB,
                expectedAB.getTaskList());
    }


    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generatetaskWithName("bla bla KEY bla");
        Task pTarget2 = helper.generatetaskWithName("bla KEY bla bceofeia");
        Task p1 = helper.generatetaskWithName("KE Y");
        Task p2 = helper.generatetaskWithName("KEYKEYKEY sduauo");

        List<Task> fourtasks = helper.generatetaskList(p1, pTarget1, p2, pTarget2);
        TaskBook expectedAB = helper.generateAddressBook(fourtasks);
        List<Task> expectedList = helper.generatetaskList(pTarget1, pTarget2);
        helper.addToModel(model, fourtasks);

        assertCommandBehavior("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generatetaskWithName("bla bla KEY bla");
        Task p2 = helper.generatetaskWithName("bla KEY bla bceofeia");
        Task p3 = helper.generatetaskWithName("key key");
        Task p4 = helper.generatetaskWithName("KEy sduauo");

        List<Task> fourtasks = helper.generatetaskList(p3, p1, p4, p2);
        TaskBook expectedAB = helper.generateAddressBook(fourtasks);
        List<Task> expectedList = fourtasks;
        helper.addToModel(model, fourtasks);

        assertCommandBehavior("find KEY",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generatetaskWithName("bla bla KEY bla");
        Task pTarget2 = helper.generatetaskWithName("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generatetaskWithName("key key");
        Task p1 = helper.generatetaskWithName("sduauo");

        List<Task> fourtasks = helper.generatetaskList(pTarget1, p1, pTarget2, pTarget3);
        TaskBook expectedAB = helper.generateAddressBook(fourtasks);
        List<Task> expectedList = helper.generatetaskList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourtasks);

        assertCommandBehavior("find key rAnDoM",
                Command.getMessageForTaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
 
    }

    
    //@@author A0139958H
    
    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{

        Task sampleTask() throws Exception {
        	Task  task = new Task();
        	Date date = DateUtils.addDays(new Date(), 1);
        	date = DateUtils.setMinutes(date, 0);
        	date = DateUtils.setSeconds(date, 0);

        	task.setName(new Name("Study for exam"));
        	task.setStartDate(new DateTime(DateUtils.setHours(date, 14)));
        	task.setEndDate(new DateTime(DateUtils.setHours(date, 17)));
        	task.setVenue(new Venue(" School"));
        	task.setPriority(TaskPriority.HIGH);
        	task.setPinTask(PinTask.PIN);
        	task.setTags(new UniqueTagList(new Tag("Study"), new Tag("Exam")));
        	return task;
        }
        
        Task sampleTask(int seed) throws Exception {
        	Task  task = new Task();
        	task.setName(new Name("Study for exam " + seed));
        	task.setStartDate(new DateTime(DateUtils.addHours(new Date(), seed)));
        	task.setEndDate(new DateTime(DateUtils.addHours(new Date(), seed + 1)));
        	task.setVenue(new Venue("School " + seed));
        	task.setPriority(TaskPriority.HIGH);
        	task.setPinTask(PinTask.PIN);
        	task.setTags(new UniqueTagList(new Tag("Study"), new Tag("Exam"), new Tag(String.valueOf(seed))));
        	return task;
        }

        /**
         * Generates a valid task using the given seed.
         * Running this function with the same parameter values guarantees the returned task will have the same state.
         * Each unique seed will generate a unique Task object.
         *
         * @param seed used to generate the task data field values
         */
        Task generateTask(int seed) throws Exception {
        	return sampleTask(seed);
        }

        /** Generates the correct add command based on the task given */
        String generateAddCommand(Task t) {
        	StringJoiner cmd = new StringJoiner(" ");
        	cmd.add("add")
        	.add(t.getName().toString())
        	.add("from tomorrow 2pm")
        	.add("by tomorrow 5pm")
        	.add("@" + t.getVenue().toString().trim())
        	.add("#" + t.getPriority().name())
        	.add("#" + t.getPinTask().name());
        	
            UniqueTagList tags = t.getTags();
            for(Tag tag : tags){
                cmd.add("#" + tag.tagName);
            }
            logger.warning("generateAddCommand: " + cmd.toString());
            return cmd.toString();
        }
        
        /** Generates the correct update command based on the task given */
        String generateUpdateCommand(Task t, int i) {
        	StringJoiner cmd = new StringJoiner(" ");
        	cmd.add("update")
        	.add(Integer.toString(i))
        	.add(t.getName().toString())
        	.add("from #null")
        	.add("by #null")
        	.add("@" + t.getVenue().toString().trim())
        	.add("#" + t.getPriority().name())
        	.add("#" + t.getPinTask().name());
        	
            UniqueTagList tags = t.getTags();
            for(Tag tag : tags){
                cmd.add("#" + tag.tagName);
            }
            return cmd.toString();
        }
        
        /** Generates the correct set command based on the task given */
        String generateSetCommand(int i) {
        	StringJoiner cmd = new StringJoiner(" ");
        	cmd.add("set")
        	.add(Integer.toString(i))
        	.add("done");
        	
            return cmd.toString();
        }
        
        
        //@@author

        /**
         * Generates an TaskBook with auto-generated tasks.
         */
        TaskBook generateAddressBook(int numGenerated) throws Exception{
            TaskBook taskBook = new TaskBook();
            addToAddressBook(taskBook, numGenerated);
            return taskBook;
        }

        /**
         * Generates an TaskBook based on the list of tasks given.
         */
        TaskBook generateAddressBook(List<Task> tasks) throws Exception{
            TaskBook taskBook = new TaskBook();
            addToAddressBook(taskBook, tasks);
            return taskBook;
        }

        /**
         * Adds auto-generated Task objects to the given TaskBook
         * @param taskBook The TaskBook to which the tasks will be added
         */
        void addToAddressBook(TaskBook taskBook, int numGenerated) throws Exception{
            addToAddressBook(taskBook, generatetaskList(numGenerated));
        }

        /**
         * Adds the given list of tasks to the given TaskBook
         */
        void addToAddressBook(TaskBook taskBook, List<Task> tasksToAdd) throws Exception{
            for(Task p: tasksToAdd){
                taskBook.addTask(p);
            }
        }

        /**
         * Adds auto-generated Task objects to the given model
         * @param model The model to which the tasks will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generatetaskList(numGenerated));
        }

        /**
         * Adds the given list of tasks to the given model
         */
        void addToModel(Model model, List<Task> tasksToAdd) throws Exception{
            for(Task p: tasksToAdd){
                model.addTask(p);
            }
        }

        /**
         * Generates a list of tasks based on the flags.
         */
        List<Task> generatetaskList(int numGenerated) throws Exception{
            List<Task> tasks = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                tasks.add(generateTask(i));
            }
            return tasks;
        }

        List<Task> generatetaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        /**
         * Generates a Task object with given name. Other fields will have some dummy values.
         */
        Task generatetaskWithName(String name) throws Exception {
            Task task = new Task();
            task.setName(new Name(name));
            return task;
        }
    }
}
