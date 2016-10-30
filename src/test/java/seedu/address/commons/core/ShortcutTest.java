package seedu.address.commons.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ShortcutUtil;
import seedu.task.commons.util.ShortcutUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;


public class ShortcutTest { 
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //@@author A0141064U
    
    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultShortcutAsString = "add : add\n" +
                "delete : delete\n" +
                "list : list" ;
                
        LogsCenter.getLogger(ShortcutTest.class).warning("ShortcutSetting: " + new ShortcutSetting().toString());
        assertEquals(defaultShortcutAsString, new ShortcutSetting().toString());
    }
    @Test
    public void toString_testShortkeyAdd_stringReturned() {
        String testAddShortkey = "add";
        assertEquals(testAddShortkey,new ShortcutSetting().getAdd());
    }
    @Test
    public void toString_defaultUserPrefLocation_stringReturned() {
        String testDeleteShortkey = "delete";
        assertEquals(testDeleteShortkey,new ShortcutSetting().getDelete());
    }
    @Test
    public void toString_defaultFileLocation_stringReturned() {
        String testListShortkey = "list";
        assertEquals(testListShortkey,new ShortcutSetting().getList());
    }
    
    @Test
    public void toString_testShortcutSettingFileObject_stringReturned() {
        ShortcutSetting testShortcutSetting = testShortcutSettingFile("src/test/data/ShortcutUtilTest/shortcutStub.json");
        String testShortcutSettingAsString = "add : aaa\n" +
        "delete : ddd\n" +
        "list : lll";
        assertEquals(testShortcutSettingAsString, testShortcutSetting.toString());
    }
    @Test
    public void toString_testSetShortcutSettingFileObject_stringReturned() {
        ShortcutSetting testShortcutSetting = testShortcutSettingFile("src/test/data/ShortcutUtilTest/tempStub.json");
        String testShortcutSettingAsString = "add : ana\n" +
        "delete : dnd\n" +
        "list : lnl";
        testShortcutSetting.setAdd("ana");
        testShortcutSetting.setDelete("dnd");
        testShortcutSetting.setList("lnl");
        
        assertEquals(testShortcutSettingAsString, testShortcutSetting.toString());
    }
    
    
    public ShortcutSetting testShortcutSettingFile (String filePath) {
        ShortcutSetting testShortcutSetting = new ShortcutSetting();
        try {
            Optional<ShortcutSetting> ShortcutSetting = ShortcutUtil.readShortcut(filePath);
            testShortcutSetting = ShortcutSetting.orElse(new ShortcutSetting());
        } catch (DataConversionException e) {
            e.printStackTrace();
          
        }
        return testShortcutSetting;
        
    }
    //@@author

    @Test
    public void equalsMethod(){
        ShortcutSetting defaultShortcutSetting = new ShortcutSetting();
        assertFalse(defaultShortcutSetting.equals(null));
        assertTrue(defaultShortcutSetting.equals(defaultShortcutSetting));
    }




}
