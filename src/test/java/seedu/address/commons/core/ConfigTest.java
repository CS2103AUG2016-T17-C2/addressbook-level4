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

    //@@author A0139958H
    
    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "App title : TaskBook App\n" +
                "Current log level : INFO\n" +
                "Preference file Location : data/preferences.json\n" +
                "Local data file location : data/taskbook.xml\n" +
                "shortcutFilePath : data/shortcut.json\n" +
                "TaskBook Name : MyTaskBook";

        LogsCenter.getLogger(ConfigTest.class).warning("Config: " + new Config().toString());
        assertEquals(defaultConfigAsString, new Config().toString());
    }
    //@@author A0141064U
    @Test
    public void toString_defaultAppTitle_stringReturned() {
        String defaultAppTitle = "TaskBook App";
        assertEquals(defaultAppTitle,new Config().getAppTitle());
    }
    @Test
    public void toString_defaultUserPrefLocation_stringReturned() {
        String defaultUserPreferenceFileLocation = "data/preferences.json";
        assertEquals(defaultUserPreferenceFileLocation,new Config().getUserPrefsFilePath());
    }
    @Test
    public void toString_defaultShortcutLocation_stringReturned() {
        String defaultShortcutFileLocation = "data/shortcut.json";
        assertEquals(defaultShortcutFileLocation,new Config().getShortcutFilePath());
    }
    @Test
    public void toString_defaultFileLocation_stringReturned() {
        String taskBookFilePath = "data/taskbook.xml";
        assertEquals(taskBookFilePath,new Config().getTaskBookFilePath());
    }
    
    @Test
    public void toString_testConfigFileObject_stringReturned() {
        Config testConfig = testConfigFile("src/test/data/ConfigUtilTest/configStub.json");
        String testConfigAsString = "App title : My friend App\n" +
        "Current log level : INFO\n" +
        "Preference file Location : pref.json\n" +
        "Local data file location : tb.xml\n" +
        "shortcutFilePath : shortcut.json\n" +
        "TaskBook Name : MyTaskBook";
        assertEquals(testConfigAsString, testConfig.toString());
    }
    
    @Test
    public void toString_testSetConfigFileObject_stringReturned() {
        Config testConfig = testConfigFile("src/test/data/ConfigUtilTest/tempConfig.json");
        testConfig.setShortcutFilePath("Shortcut");
        testConfig.setTaskBookFilePath("TBP");
        testConfig.setAppTitle("APP");
        testConfig.setUserPrefsFilePath("user");
        String testConfigAsString = "App title : APP\n" +
                "Current log level : INFO\n" +
                "Preference file Location : user\n" +
                "Local data file location : TBP\n" +
                "shortcutFilePath : Shortcut\n" +
                "TaskBook Name : MyTaskBook";
                
        assertEquals(testConfigAsString, testConfig.toString());
    }
    
    public Config testConfigFile (String filePath) {
        Config testConfig = new Config();
        try {
            Optional<Config> config = ConfigUtil.readConfig(filePath);
            testConfig = config.orElse(new Config());
        } catch (DataConversionException e) {
            e.printStackTrace();
          
        }
        return testConfig;
        
    }
    //@@author

    @Test
    public void equalsMethod(){
        Config defaultConfig = new Config();
        assertFalse(defaultConfig.equals(null));
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
