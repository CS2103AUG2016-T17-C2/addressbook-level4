package seedu.address.commons.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

public class ConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Config testConfig;

    //@@author A0139958H
    
    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "App title : TaskBook App\n" +
                "Current log level : INFO\n" +
                "Preference file Location : data/preferences.json\n" +
                "Local data file location : data/taskbook.xml\n" +
                "TaskBook name : MyTaskBook";

        LogsCenter.getLogger(ConfigTest.class).warning("Config: " + new Config().toString());
        assertEquals(defaultConfigAsString, new Config().toString());
    }
    //@@author A0141064U
    public void toString_defaultAppTitle_stringReturned() {
        String defaultAppTitle = "TaskBookApp";
        assertEquals(defaultAppTitle,new Config().getAppTitle());
    }

    public void toString_defaultUserPrefLocation_stringReturned() {
        String defaultUserPreferenceFileLocation = "data/preferences.json";
        assertEquals(defaultUserPreferenceFileLocation,new Config().getUserPrefsFilePath());
    }
    public void toString_defaultShortcutLocation_stringReturned() {
        String defaultShortcutFileLocation = "data/shortcut.json";
        assertEquals(defaultShortcutFileLocation,new Config().getShortcutFilePath());
    }
    public void toString_defaultFileLocation_stringReturned() {
        String taskBookFilePath = "data/taskbook.xml";
        assertEquals(taskBookFilePath,new Config().getUserPrefsFilePath());
    }
    
    @Test
    public void toString_testConfigFileObject_stringReturned() {
        String testConfigAsString = "AppTitle : My friend App\n" +
        "logLevel : INFO\n" +
        "userPrefsFilePath : pref.json\n" +
        "taskBookFilePath : tb.xml\n" +
     //   "shortcutFilePath : shortcut.json\n" +
        "taskBookName : MyTaskBook";
        assertEquals(testConfigAsString, testConfig.toString());
    }
    
    public void testConfigFile () {
        try {
            Optional<Config> config = ConfigUtil.readConfig("src/test/data/ConfigUtilTest/configStub.json");
            this.testConfig = config.orElse(new Config());
        } catch (DataConversionException e) {
            e.printStackTrace();
        }
        
    }
    //@@author

    @Test
    public void equalsMethod(){
        Config defaultConfig = new Config();
        assertFalse(defaultConfig.equals(null));
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
