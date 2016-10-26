package seedu.address.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonProcessingException;

import seedu.task.commons.util.FileUtil;
import seedu.task.commons.util.JsonUtil;
import seedu.task.model.TaskBook;

/**
 * Tests JSON Read and Write
 */
public class JsonUtilTest {
    
    
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/JsonUtilTest/");
    private static final File EMPTY_FILE = new File(TEST_DATA_FOLDER + "empty.json");
    private static final File MISSING_FILE = new File(TEST_DATA_FOLDER + "missing.json");
    private static final File VALID_FILE = new File(TEST_DATA_FOLDER + "TestJson.json");
  
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    
    @Test
    public void fromJsonString_nullFile_AssertionError() throws IOException {
        thrown.expect(NullPointerException.class);
        JsonUtil.fromJsonString(null, TestClass.class);
    }



    @Test
    public void fromJsonString_missingFile_FileNotFoundException() throws Exception {
        thrown.expect(IOException.class);
        JsonUtil.fromJsonString(MISSING_FILE.toString(), TaskBook.class);
    }

    @Test
    public void fromJsonString_emptyFile_DataFormatMismatchException() throws Exception {
        thrown.expect(IOException.class);
        JsonUtil.fromJsonString(EMPTY_FILE.toString(), TaskBook.class);
    }

    /*
     * Read from file and check
     */
   @Test
    public void fromJsonString_validFile_validResult() throws Exception {
        TestClass tc = new TestClass();
        
       JsonUtil.fromJsonString(FileUtil.readFromFile(VALID_FILE), TestClass.class);
        int testFirstElement = 123;
         assertEquals(testFirstElement, tc.getFirstElement());
         assertEquals("two", tc.getSecondElement());
    }

   
    //TODO: @Test jsonUtil_readJsonStringToObjectInstance_correctObject()
    
    //TODO: @Test jsonUtil_writeThenReadObjectToJson_correctObject()

}
