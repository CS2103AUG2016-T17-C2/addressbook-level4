package seedu.task.logic.commands;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.events.model.ShortcutChangedEvent;
import seedu.task.commons.events.model.StorageFilepathChangedEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ShortcutUtil;
import seedu.task.commons.util.StringUtil;

/**
 * Changes the Command names of taskBook.
 */

public class ShortcutCommand extends Command {

    public static final String COMMAND_WORD = "cs";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the command name of commands. " + "Example: "
            + COMMAND_WORD;
    // TODO: figure out how to type example

    public static final String MESSAGE_SUCCESS = "File path changed";
    public static final String MESSAGE_DUPLICATE_FILENAME = "This file already exists in the taskBook";

    String shortcutField;
    String shortcutWord;
    Logger logger = LogsCenter.getLogger(ShortcutCommand.class);
    ShortcutSetting shortcutSetting;
    
    /**
     * Parameter: File Path Object
     * 
     * @throws IOException
     */

    /*
     * Changes the name of the command
     */
    public ShortcutCommand(String shortcutField, String shortcutWord) {

        this.shortcutField = shortcutField;
        this.shortcutWord = shortcutWord;
        run();
    }

    private void run() {
        String shortcutFilePathUsed = ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH;

        // Update shortcut file in case it was missing to begin with or there
       try {
            Optional<ShortcutSetting> shortcutOptional = ShortcutUtil.readShortcut(shortcutFilePathUsed);
            shortcutSetting = shortcutOptional.orElse(new ShortcutSetting());
        } catch (DataConversionException e) {
            shortcutSetting = new ShortcutSetting();
        }

        try {
            switch (this.shortcutField) {

            case AddCommand.COMMAND_WORD:
                shortcutSetting.setAdd(this.shortcutWord);

            case DeleteCommand.COMMAND_WORD:
                shortcutSetting.setDelete(this.shortcutWord);

            case ListCommand.COMMAND_WORD:
                shortcutSetting.setList(this.shortcutWord);

            default:
                // tell user that field does not exist
            }

            ShortcutUtil.saveShortcut(this.shortcutSetting, shortcutFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save shortcut change : " + StringUtil.getDetails(e));
        }

    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShortcutChangedEvent(this.shortcutSetting));
        return new CommandResult(String.format(MESSAGE_SUCCESS, shortcutWord));

    }

}
