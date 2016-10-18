package seedu.task.commons.core;

public class ShortcutSetting {
    public static final String DEFAULT_SHORTCUT_FILEPATH = "data/shortcut.json";
    
    public String add = "add";
    private String delete = "delete";
    private String list = "list";
    
    public ShortcutSetting () {
         }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

}
