package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.FileUtil;
import seedu.task.commons.util.ShortcutUtil;
//@@author A0141064U
public class ShortcutUtilTest {
    

    private static String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/ShortcutUtilTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void read_null_assertionFailure() throws DataConversionException {
        thrown.expect(AssertionError.class);
        read(null);
    }

    @Test
    public void read_missingFile_emptyResult() throws DataConversionException {
        assertFalse(read("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJasonFormat_exceptionThrown() throws DataConversionException {

        thrown.expect(DataConversionException.class);
        read("NotJasonFormatShortcut.json");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }
    @Test
    public void read_fileInOrder_successfullyRead() throws DataConversionException {

        ShortcutSetting expected = getTypicalShortcutSetting();

        ShortcutSetting actual = read("TypicalShortcut.json").get();
        assertEquals(expected, actual);
    }

    @Test
    public void read_valuesMissingFromFile_defaultValuesUsed() throws DataConversionException {
        ShortcutSetting actual = read("EmptyShortcut.json").get();
        assertEquals(new ShortcutSetting(), actual);
    }
    
    @Test
    public void read_extraValuesInFile_extraValuesIgnored() throws DataConversionException {
        ShortcutSetting expected = getTypicalShortcutSetting();
        ShortcutSetting actual = read("ExtraValuesShortcut.json").get();

        assertEquals(expected, actual);
    }

    private ShortcutSetting getTypicalShortcutSetting() {
        ShortcutSetting shortcut = new ShortcutSetting("A","B","C");
        return shortcut;
    }

    private Optional<ShortcutSetting> read(String shortcutFileInTestDataFolder) throws DataConversionException {
        String shortcutSettingFilePath = addToTestDataPathIfNotNull(shortcutFileInTestDataFolder);
        Optional<ShortcutSetting> shortcut = ShortcutUtil.readShortcut(shortcutSettingFilePath);
        return shortcut;
    }

    @Test
    public void save_nullShortcut_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        save(null, "SomeFile.json");
    }

    @Test
    public void save_nullFile_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        save(new ShortcutSetting(), null);
    }

    @Test
    public void saveShortcutSetting_allInOrder_success() throws DataConversionException, IOException {
        ShortcutSetting original = getTypicalShortcutSetting();

        String shortcutFilePath = testFolder.getRoot() + File.separator + "TempShortcut.json";
        
        //Try writing when the file doesn't exist
        ShortcutUtil.saveShortcut(original, shortcutFilePath);
        ShortcutSetting readBack = ShortcutUtil.readShortcut(shortcutFilePath).get();
        assertEquals(original, readBack);

        //Try saving when the file exists
        original.setAdd("addShortkey");
        ShortcutUtil.saveShortcut(original, shortcutFilePath);
        readBack = ShortcutUtil.readShortcut(shortcutFilePath).get();
        assertEquals(original, readBack);
    }

    private void save(ShortcutSetting shortcut, String shortcutFileInTestDataFolder) throws IOException {
        String shortcutFilePath = addToTestDataPathIfNotNull(shortcutFileInTestDataFolder);
        ShortcutUtil.saveShortcut(shortcut, shortcutFilePath);
    }

    private String addToTestDataPathIfNotNull(String shortcutFileInTestDataFolder) {
        return shortcutFileInTestDataFolder != null
                                  ? TEST_DATA_FOLDER + shortcutFileInTestDataFolder
                                  : null;
    }


}

