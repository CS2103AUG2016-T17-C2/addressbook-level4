package seedu.task.logic.commands;

//@@author A0141064U

import static seedu.task.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.task.commons.core.Config;
import seedu.task.commons.core.EventsCenter;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.core.ShortcutSetting;
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
        assert shortcutField != null;
        assert shortkey != null;

        this.shortcutField = shortcutField;
        this.shortkey = shortkey;

    }

    @Override
    public CommandResult execute() {
        
        Config config = new Config();
        if (!config.getShortcutFilePath().equals(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH)) {
            config.setShortcutFilePath(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
        }

        try {
            Optional<ShortcutSetting> shortcutOptional = ShortcutUtil
                    .readShortcut(ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
            shortcutSetting = shortcutOptional.orElse(new ShortcutSetting());
        } catch (DataConversionException e) {
            shortcutSetting = new ShortcutSetting();
        }

        if (shortkey.equals(shortcutSetting.getAdd()) | shortkey.equals(shortcutSetting.getDelete())
                | shortkey.equals(shortcutSetting.getList())) {
            try {
                throw new IllegalValueException(MESSAGE_DUPLICATE_SHORTKEY);
            } catch (IllegalValueException e) {
                e.printStackTrace();
                return new CommandResult(e.getMessage());
            }
        }

        run(); // matches and edits the shortkeys;
        saveShortcutFile();
        return new CommandResult(String.format(MESSAGE_SUCCESS+shortcutField+" changed to "+shortkey));

    }

    private void saveShortcutFile() {
        try {
            ShortcutUtil.saveShortcut(this.shortcutSetting, ShortcutSetting.DEFAULT_SHORTCUT_FILEPATH);
        } catch (IOException e) {
            new CommandResult(e.getMessage());
            logger.warning("Failed to save shortcut file");
            e.printStackTrace();
        }
    }

    private void run() {

        switch (this.shortcutField) {

        case AddCommand.COMMAND_WORD:
            shortcutSetting.setAdd(this.shortkey);
            return;

        case DeleteCommand.COMMAND_WORD:
            shortcutSetting.setDelete(this.shortkey);
            return;

        case ListCommand.COMMAND_WORD:
            shortcutSetting.setList(this.shortkey);
            return;

        default:
            new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
            return;
        }
    }

    
}
