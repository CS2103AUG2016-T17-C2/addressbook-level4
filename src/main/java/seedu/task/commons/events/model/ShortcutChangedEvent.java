package seedu.task.commons.events.model;

import seedu.task.commons.core.ShortcutSetting;
import seedu.task.commons.events.BaseEvent;
import seedu.task.model.ReadOnlyTaskBook;


/** Indicates the Shortcut file has changed*/
public class ShortcutChangedEvent extends BaseEvent {

        public final ShortcutSetting shortcutSetting;

        public ShortcutChangedEvent(ShortcutSetting shortcutSetting){
            this.shortcutSetting = shortcutSetting;
        }

        @Override
        public String toString() {
            return "shortcut key changed";
        }
}
