package seedu.address.commons.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.LogsCenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void equalsMethod(){
        Config defaultConfig = new Config();
        assertFalse(defaultConfig.equals(null));
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
