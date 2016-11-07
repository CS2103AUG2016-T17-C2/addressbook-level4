package seedu.task.model;

import java.util.Objects;

import seedu.task.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    public GuiSettings guiSettings;

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    //@@author A0138301U
    public UserPrefs(){//default ideal size for window
        this.setGuiSettings(700, 1000, 0, 0);
    }
    //@@author
    
    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof UserPrefs)){ //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs)other;

        return Objects.equals(guiSettings, o.guiSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings);
    }

    @Override
    public String toString(){
        return guiSettings.toString();
    }

}
