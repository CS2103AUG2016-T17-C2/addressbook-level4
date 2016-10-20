package seedu.task.commons.core;

import java.awt.Point;
import java.util.Objects;
import java.util.logging.Logger;

public class ShortcutSetting {
    public static final String DEFAULT_SHORTCUT_FILEPATH = "data/shortcut.json";

        
    private static final String DEFAULT_ADD = "add";
    private static final String DEFAULT_DELETE = "delete";
    private static final String DEFAULT_LIST = "list";
    

    private String add;
    private String delete;
    private String list;

    public ShortcutSetting() {
        this.add = DEFAULT_ADD;
        this.delete = DEFAULT_DELETE;
        this.list = DEFAULT_LIST;
    }

    public ShortcutSetting(String add, String delete, String list) {
        this.add = add;
        this.delete = delete;
        this.list = list;
    }

    public String getAdd() {
        return add;
    }

    public String getDelete() {
        return delete;
    }

    public String getList() {
        return list;
    }
    
    public void setAdd(String add) {
        this.add = add;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setList(String list) {
        this.list = list;
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

    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof ShortcutSetting)){ //this handles null as well.
            return false;
        }

        ShortcutSetting o = (ShortcutSetting)other;

        return Objects.equals(add, o.add)
                && Objects.equals(delete, o.delete)
                && Objects.equals(list, o.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(add, delete, list);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("add : " + add + "\n");
        sb.append("delete : " + delete + "\n");
        sb.append("list : " + list);
        return sb.toString();
    }
}


