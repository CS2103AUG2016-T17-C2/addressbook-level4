package seedu.task.commons.core;

import java.util.logging.Logger;

public class ShortcutSetting {
    public static final String DEFAULT_SHORTCUT_FILEPATH = "data/shortcut.json";

    public static String add = "add";
    public static String delete = "delete";
    public static String list = "list";

    public ShortcutSetting() {
    }




    public String convertShortcut(String tempCommandWord) {
        if (tempCommandWord.equals(add)) {
            tempCommandWord = "add";
        }
        if (tempCommandWord.equals(delete)) {
            tempCommandWord = "delete";
        }
        if (tempCommandWord.equals(list)) {
            tempCommandWord = "list";
        }
        return tempCommandWord;
    }
}
