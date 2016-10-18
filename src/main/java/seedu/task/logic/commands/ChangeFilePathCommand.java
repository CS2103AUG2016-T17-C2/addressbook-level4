package seedu.task.logic.commands;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.model.StorageFilepathChangedEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.TaskBook;
import seedu.task.storage.XmlTaskBookStorage;

/**
 * Changes the filepath of taskBook.
 */
public class ChangeFilePathCommand extends Command {

    public static final String COMMAND_WORD = "movefile";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the filepath of Taskbook. " + "Example: "
            + COMMAND_WORD;
    // TODO: figure out how to type example

    public static final String MESSAGE_SUCCESS = "File path changed to ";
    public static final String MESSAGE_DUPLICATE_FILENAME = "This file already exists in the taskBook";

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
        
        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            initializedConfig = new Config();
        }

        moveFileData();
        updateFilePath();
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

    @Override
    public CommandResult execute() {

        
        EventsCenter.getInstance().post(new StorageFilepathChangedEvent(this.initializedConfig));

        return new CommandResult(String.format(MESSAGE_SUCCESS + newFilepathString));
        
    }

}
