package seedu.task.logic.commands;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.model.StorageFilepathChangedEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.commons.util.FileUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.TaskBook;
import seedu.task.storage.XmlTaskBookStorage;

/**
 * Changes the file Path of taskBook.
 */
public class ChangeFilePathCommand extends Command {

    public static final String COMMAND_WORD = "file";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Moves the file to a new location within the same directory. " + "Example:" + COMMAND_WORD + " aaa ";
    public static final String MESSAGE_RENAME_TO_OLD_FILE = "New file name cannot be the same name as the current file name";
    public static final String MESSAGE_SUCCESS = "File is moved/renamed to ";
    public static final String MESSAGE_DUPLICATE_FILENAME = "This file already exists in the taskBook,"
            + " file will be overwritten";

    String oldFilepathString;
    String newFilepathString;
    Logger logger = LogsCenter.getLogger(ChangeFilePathCommand.class);
    ReadOnlyTaskBook readOnlyTaskBook;
    Config initializedConfig;

    /**
     * Parameter: File Path Object
     * 
     * @throws IOException
     *
     * 
     */

    /*
     * Moves the file to a new location within the same directory
     */
    public ChangeFilePathCommand(String newFilepathString) {
        assert newFilepathString != null;
        setConfig();
        this.oldFilepathString = initializedConfig.getTaskBookFilePath();
        String trimmedNewFilepathString = newFilepathString.trim();
        this.newFilepathString = trimmedNewFilepathString.concat(".xml");

        run();
    }

    private void setConfig() {
        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            initializedConfig = new Config();
        }

    }

    private void run() {

        this.oldFilepathString = initializedConfig.getTaskBookFilePath();

        if (this.oldFilepathString.equals(this.newFilepathString)) {
            new CommandResult(MESSAGE_RENAME_TO_OLD_FILE);
            this.newFilepathString.concat("new");
        } else {
            moveFileData();
            updateFilePath();
            deleteOldFile();
        }

    }

    private void moveFileData() {

        try {

            XmlTaskBookStorage taskbookStorage = new XmlTaskBookStorage(this.initializedConfig.getTaskBookFilePath());

            Optional<ReadOnlyTaskBook> readOnlyTaskbookOptional = taskbookStorage
                    .readTaskBook(initializedConfig.getTaskBookFilePath());
            this.readOnlyTaskBook = readOnlyTaskbookOptional.orElse(new TaskBook());
            taskbookStorage.saveTaskBook(readOnlyTaskBook, this.newFilepathString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateFilePath() {

        // Update config file in case it was missing to begin with or there are
        // new/unused fields
        try {
            this.initializedConfig.setTaskBookFilePath(this.newFilepathString);
            ConfigUtil.saveConfig(this.initializedConfig, Config.DEFAULT_CONFIG_FILE);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

    }

    private void deleteOldFile() {
        try {
            Files.delete(Paths.get(this.oldFilepathString));
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", this.oldFilepathString);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", this.oldFilepathString);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println("");
        }
    }

    @Override
    public CommandResult execute() {

        EventsCenter.getInstance().post(new StorageFilepathChangedEvent(this.initializedConfig));

        return new CommandResult(String.format(MESSAGE_SUCCESS + newFilepathString));

    }

}
