package seedu.task.logic.commands;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.commons.util.FileUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.UserPrefs;
import seedu.task.storage.TaskBookStorage;
import seedu.task.storage.UserPrefsStorage;

/**
 * Changes the filepath of taskBook.
 */
public class ChangeFilePathCommand extends Command {

    public static final String COMMAND_WORD = "movefile";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the filepath of Taskbook. " + "Example: "
            + COMMAND_WORD;
    // TODO: figure out how to type example

    public static final String MESSAGE_SUCCESS = "File path changed";
    public static final String MESSAGE_DUPLICATE_FILENAME = "This file already exists in the taskBook";

    String newFilepathString;
    Logger logger = LogsCenter.getLogger(ChangeFilePathCommand.class);

    /**
     * Parameter: File Path Object
     * 
     * @throws IOException
     *
     * 
     */

    /*
     * Changes the Filepath of the file it writes to Creates a new file if it
     * does not exist File path can only be changed to within 'main' folder.
     */
    public ChangeFilePathCommand(String newFilepathString) {

        String trimmedNewFilepathString = newFilepathString;
        if (trimmedNewFilepathString == null) {
            // TODO: filepath name invalid
        }
        this.newFilepathString = trimmedNewFilepathString.concat(".xml");

        run();
    }

    private void run() {
        Config config = null;

        try {
            config = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE).orElse(null);
        } catch (DataConversionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        updateFilePath();
        moveFileData(config);
    }

    private void moveFileData(Config config) {
        try {
            File oldFile = new File(FileUtil.getPath(config.getTaskBookFilePath()));
            if (oldFile.renameTo(new File(this.newFilepathString))) {
                System.out.println("File is moved successful!");
            } else {
                System.out.println("File is failed to move!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateFilePath() {
        Config initializedConfig;
        String configFilePathUsed;
        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            initializedConfig = new Config();
        }
        // Update config file in case it was missing to begin with or there are
        // new/unused fields
        try {
            initializedConfig.setTaskBookFilePath(this.newFilepathString);
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public CommandResult execute() {
        assert model != null;
        return new CommandResult(String.format(MESSAGE_SUCCESS, newFilepathString));

    }

}
