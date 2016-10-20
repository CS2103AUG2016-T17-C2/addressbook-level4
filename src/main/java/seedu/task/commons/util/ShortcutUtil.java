package seedu.task.commons.util;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.DataConversionException;

/**
 * A class for accessing the shortcut File.
 */

public class ShortcutUtil {

    private static final Logger logger = LogsCenter.getLogger(ShortcutUtil.class);

    /**
     * Returns the Shortcut object from the given file or
     * {@code Optional.empty()} object if the file is not found. If any values
     * are missing from the file, default values will be used, as long as the
     * file is a valid json file.
     * 
     * @param shortcutFilePath
     *            cannot be null.
     * @throws DataConversionException
     *             if the file format is not as expected.
     */
    public static Optional<ShortcutSetting> readShortcut(String shortcutFilePath) throws DataConversionException {

        assert shortcutFilePath != null;

        File shortcutFile = new File(shortcutFilePath);

        if (!shortcutFile.exists()) {
            logger.info("shortcut file " + shortcutFile + " not found");
            return Optional.empty();
        }

        ShortcutSetting shortcut;

        try {
            shortcut = FileUtil.deserializeObjectFromJsonFile(shortcutFile, ShortcutSetting.class);
        } catch (IOException e) {
            logger.warning("Error reading from shortcut file " + shortcutFile + ": " + e);
            throw new DataConversionException(e);
        }

        return Optional.of(shortcut);
    }
    /**
     * Saves the Shortcut object to the specified file. Overwrites existing file
     * if it exists, creates a new file if it doesn't.
     * 
     * @param shortcut
     *            cannot be null
     * @param shortcutFilePath
     *            cannot be null
     * @throws IOException
     *             if there was an error during writing to the file
     */
    public static void saveShortcut(ShortcutSetting shortcut, String shortcutFilePath) throws IOException {
        assert shortcut != null;
        assert shortcutFilePath != null;

        FileUtil.serializeObjectToJsonFile(new File(shortcutFilePath), shortcut);
    }

}
