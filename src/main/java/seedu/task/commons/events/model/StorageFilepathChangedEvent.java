package seedu.task.commons.events.model;
//@@author A0141064U

import seedu.task.commons.core.Config;
import seedu.task.commons.events.BaseEvent;

/** Indicates the xmlTaskBook Filepath in the Config has changed*/

public class StorageFilepathChangedEvent extends BaseEvent{
    public final String newConfigFilepathString;
    Config config;
    
    
    public StorageFilepathChangedEvent( Config initializedConfig){
        assert initializedConfig != null;
        this.config = initializedConfig;
        this.newConfigFilepathString = Config.DEFAULT_CONFIG_FILE;
    }
    
   
   
    @Override
    public String toString() {
        return "File Path changed to " + config.getTaskBookFilePath();
   
    }
    
    public String getTaskBookFilepath() {
        return config.getTaskBookFilePath();
   
    }



}
