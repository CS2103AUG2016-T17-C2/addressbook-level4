package seedu.task.logic.commands;

    import java.io.IOException;
import seedu.task.commons.core.Config;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.commons.util.ConfigUtil;

    /**
     * Changes the filepath of taskBook.
     */
    public class ChangeFilePathCommand extends Command {

        public static final String COMMAND_WORD = "changefilepath";

        public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the filepath of Taskbook. "
                + "Example: " + COMMAND_WORD;
        //TODO: figure out how to type example


        public static final String MESSAGE_SUCCESS = "File path changed";
        public static final String MESSAGE_DUPLICATE_FILENAME = "This file already exists in the taskBook";

        
        /**
         * Parameter: File Path Object
         * @throws IOException 
         *
         * 
         */
        String newFilepathString;
        
        /*
         * Changes the Filepath of the file it writes to
         * Creates a new file if it does not exist
         * File path can only be changed to within 'main' folder.
         */
        public ChangeFilePathCommand(String newFilepathString) {
            this.newFilepathString = newFilepathString;
            
            execute(newFilepathString);
        }

        private void execute(String newFilepathString) {
            try {
                    Config config = ConfigUtil.readConfig(Config.DEFAULT_CONFIG_FILE).orElse(null);
                    if (config==null) 
                        System.out.println("not changed!");
                    config.setTaskBookFilePath(newFilepathString+".xml");                    
                    ConfigUtil.saveConfig(config, Config.DEFAULT_CONFIG_FILE);
                    
                } catch (DataConversionException | IOException e2) {
                    System.out.println("Changefilepath command failed");
                    e2.printStackTrace();
                }
        }

        @Override
        public CommandResult execute() {
            assert model != null;
                return new CommandResult(String.format(MESSAGE_SUCCESS, newFilepathString));

        }

    }

