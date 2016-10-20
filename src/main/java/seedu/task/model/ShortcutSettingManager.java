package seedu.task.model;

import java.util.Objects;

import seedu.task.commons.core.ShortcutSetting;

public class ShortcutSettingManager {
    public ShortcutSetting shortcutSetting;

    public ShortcutSetting getShortcutSetting() {
        return shortcutSetting == null ? new ShortcutSetting() : shortcutSetting;
    }

    public void updateLastUsedShortcutSetting(ShortcutSetting shortcutSetting) {
        this.shortcutSetting = shortcutSetting;
    }

    public ShortcutSettingManager(){
        this.setShortcutSetting("add", "delete", "list");
    }

    public void setShortcutSetting(String add, String delete, String list) {
        shortcutSetting = new ShortcutSetting(add , delete, list);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof ShortcutSettingManager)){ //this handles null as well.
            return false;
        }

        ShortcutSettingManager o = (ShortcutSettingManager)other;

        return Objects.equals(shortcutSetting, o.shortcutSetting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortcutSetting);
    }

    @Override
    public String toString(){
        return shortcutSetting.toString();
    }

}
