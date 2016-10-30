package seedu.address.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.task.commons.core.Config;
import seedu.task.commons.events.model.StorageFilepathChangedEvent;
import seedu.task.commons.events.model.TaskBookChangedEvent;
import seedu.task.commons.events.storage.DataSavingExceptionEvent;
import seedu.task.model.TaskBook;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.UserPrefs;
import seedu.task.storage.JsonUserPrefsStorage;
import seedu.task.storage.Storage;
import seedu.task.storage.StorageManager;
import seedu.task.storage.XmlTaskBookStorage;
import seedu.task.testutil.EventsCollector;
import seedu.task.testutil.TypicalTestTasks;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void taskBookReadSave() throws Exception {
        TaskBook original = new TypicalTestTasks().getTypicalTaskBook();
        storageManager.saveTaskBook(original);
        ReadOnlyTaskBook retrieved = storageManager.readTaskBook().get();
        //@@author A0141064U
        assertEquals(original.getTagList(), new TaskBook(retrieved).getTagList());
        //@@author
        //More extensive testing of TaskBook saving/reading is done in XmlAddressBookStorageTest
    }

    @Test
    public void getTaskBookFilePath(){
        assertNotNull(storageManager.getTaskBookFilePath());
    }

    @Test
    public void handleTaskBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskBookStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleAddressBookChangedEvent(new TaskBookChangedEvent(new TaskBook()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }
    
    //@@author A0141064U
    @Test
    public void StorageFilepathChangedEventExceptionThrowingStub () {
        Config config = new Config();
        String expected = "File Path changed to ";
        assertEquals(new StorageFilepathChangedEvent(config).toString(), expected + config.getTaskBookFilePath());
        
    }

    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskBookStorageExceptionThrowingStub extends XmlTaskBookStorage{

        public XmlTaskBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskBook(ReadOnlyTaskBook taskBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
