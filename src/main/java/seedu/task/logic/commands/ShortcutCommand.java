package seedu.task.logic.commands;

import static seedu.task.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.events.model.ShortcutChangedEvent;
import seedu.task.commons.events.model.StorageFilepathChangedEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.ShortcutUtil;
import seedu.task.commons.util.StringUtil;
import seedu.task.logic.parser.Parser;
import seedu.task.model.task.DateTime;

/**
 * Changes the Command names of taskBook.
 */

public class ShortcutCommand extends Command {

    public static final String COMMAND_WORD = "shortcut";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the shortkeys to invoke commands. "
            + "Example: " + COMMAND_WORD + "add a";

    public static final String MESSAGE_SUCCESS = "Shortkey changed: ";
    public static final String MESSAGE_DUPLICATE_SHORTKEY = "This shortkey is already in use";

    String shortcutField;
    String shortkey;
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
    public ShortcutCommand(String shortcutField, String shortkey) throws IllegalValueException {

        this.shortcutField = shortcutField;
        this.shortkey = shortkey;
        if (shortkey.equals(ShortcutSetting.add)|shortkey.equals(ShortcutSetting.delete)|shortkey.equals(ShortcutSetting.list)){
            throw new IllegalValueException(MESSAGE_DUPLICATE_SHORTKEY);
        }
        run();
        try {
            ShortcutUtil.saveShortcut(this.shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
        } catch (IOException e) {
            logger.warning("Failed to save shortcut file");
            e.printStackTrace();
        }
    }

    private void run() {

        // Update shortcut file in case it was missing to begin with or there
       try {
            Optional<ShortcutSetting> shortcutOptional = ShortcutUtil.readShortcut(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
            shortcutSetting = shortcutOptional.orElse(new ShortcutSetting());
        } catch (DataConversionException e) {
            this.shortcutSetting = new ShortcutSetting();
        }
            switch (this.shortcutField) {

            case AddCommand.COMMAND_WORD:
                ShortcutSetting.add = this.shortkey;
                return;

            case DeleteCommand.COMMAND_WORD:
                ShortcutSetting.delete = this.shortkey;
                return;

            case ListCommand.COMMAND_WORD:
                ShortcutSetting.list = this.shortkey;
                return;

            default:
                new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
                return;
            }

    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShortcutChangedEvent(this.shortcutSetting));
        return new CommandResult(String.format(MESSAGE_SUCCESS + shortcutField + " changed to " + shortkey));

    }

}
