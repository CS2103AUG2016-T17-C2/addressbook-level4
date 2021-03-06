package seedu.task.testutil;

import com.google.common.io.Files;
import guitests.guihandles.TaskCardHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import junit.framework.AssertionFailedError;

import org.apache.commons.lang3.time.DateUtils;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import seedu.address.TestApp;
import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.FileUtil;
import seedu.task.commons.util.XmlUtil;
import seedu.task.model.TaskBook;
import seedu.task.model.tag.Tag;
import seedu.task.model.tag.UniqueTagList;
import seedu.task.model.task.*;
import seedu.task.storage.XmlSerializableTaskBook;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    public static String LS = System.lineSeparator();

    public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        }
        catch (Throwable actualException) {
            if (!actualException.getClass().isAssignableFrom(expected)) {
                String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                        actualException.getClass().getName());
                throw new AssertionFailedError(message);
            } else return;
        }
        throw new AssertionFailedError(
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    //@@author A0139958H
    
    public static final Task[] sampleTaskData = getSampleTaskData();

    private static Task[] getSampleTaskData() {
        try {
        	Task  task1 = new Task();
        	Date date = DateUtils.addDays(new Date(), 1);
        	date = DateUtils.setMinutes(date, 0);
        	date = DateUtils.setSeconds(date, 0);

        	task1.setName(new Name("Study for exam"));
        	task1.setStartDate(new DateTime(DateUtils.setHours(date, 14)));
        	task1.setEndDate(new DateTime(DateUtils.setHours(date, 17)));
        	task1.setVenue(new Venue(" School"));
        	task1.setPriority(TaskPriority.HIGH);
        	task1.setPinTask(PinTask.PIN);
        	task1.setTags(new UniqueTagList(new Tag("Study"), new Tag("Exam")));   
        	
        	Task  task2 = new Task();
        	date = DateUtils.addDays(date, 2);

        	task2.setName(new Name("Play Football"));
        	task2.setStartDate(new DateTime(DateUtils.setHours(date, 17)));
        	task2.setEndDate(new DateTime(DateUtils.setHours(date, 19)));
        	task2.setVenue(new Venue(" School"));
        	task2.setPriority(TaskPriority.MEDIUM);
        	task2.setPinTask(PinTask.PIN);
        	task2.setTags(new UniqueTagList(new Tag("Play"), new Tag("sports")));   
        	
        	Task  task3 = new Task();
        	date = DateUtils.addDays(date, 3);

        	task3.setName(new Name("Code cs2103"));
        	task3.setStartDate(new DateTime(DateUtils.setHours(date, 19)));
        	task3.setEndDate(new DateTime(DateUtils.setHours(date, 23)));
        	task3.setVenue(new Venue(" School"));
        	task3.setPriority(TaskPriority.HIGH);
        	task3.setPinTask(PinTask.UNPIN);
        	task3.setTags(new UniqueTagList(new Tag("Study"), new Tag("Code")));   
        	
        	return new Task[] { task1, task2, task3 };
        	
        } catch (IllegalValueException e) {
            assert false;
            return null;
        }
    }

    public static final Tag[] sampleTagData = getSampleTagData();

    private static Tag[] getSampleTagData() {
        try {
            return new Tag[]{
                    new Tag("bros"),
                    new Tag("friends")
            };
        } catch (IllegalValueException e) {
            assert false;
            return null;
        }
    }

    //@@author
    
    public static List<Task> generateSamplePersonData() {
        return Arrays.asList(sampleTaskData);
    }

    /**
     * Appends the file name to the sandbox folder path.
     * Creates the sandbox folder if it doesn't exist.
     * @param fileName
     * @return
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    public static void createDataFileWithSampleData(String filePath) {
        createDataFileWithData(generateSampleStorageAddressBook(), filePath);
    }

    public static <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... s) {
        createDataFileWithSampleData(TestApp.SAVE_LOCATION_FOR_TESTING);
    }

    public static TaskBook generateEmptyTaskBook() {
        return new TaskBook(new UniqueTaskList(), new UniqueTagList());
    }

    public static XmlSerializableTaskBook generateSampleStorageAddressBook() {
        return new XmlSerializableTaskBook(generateEmptyTaskBook());
    }

    /**
     * Tweaks the {@code keyCodeCombination} to resolve the {@code KeyCode.SHORTCUT} to their
     * respective platform-specific keycodes
     */
    public static KeyCode[] scrub(KeyCodeCombination keyCodeCombination) {
        List<KeyCode> keys = new ArrayList<>();
        if (keyCodeCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.ALT);
        }
        if (keyCodeCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.SHIFT);
        }
        if (keyCodeCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.META);
        }
        if (keyCodeCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
            keys.add(KeyCode.CONTROL);
        }
        keys.add(keyCodeCombination.getCode());
        return keys.toArray(new KeyCode[]{});
    }

    public static boolean isHeadlessEnvironment() {
        String headlessProperty = System.getProperty("testfx.headless");
        return headlessProperty != null && headlessProperty.equals("true");
    }

    public static void captureScreenShot(String fileName) {
        File file = GuiTest.captureScreenshot();
        try {
            Files.copy(file, new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String descOnFail(Object... comparedObjects) {
        return "Comparison failed \n"
                + Arrays.asList(comparedObjects).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }

    public static void setFinalStatic(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException{
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        // ~Modifier.FINAL is used to remove the final modifier from field so that its value is no longer
        // final and can be changed
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    public static void initRuntime() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.hideStage();
    }

    public static void tearDownRuntime() throws Exception {
        FxToolkit.cleanupStages();
    }

    /**
     * Gets private method of a class
     * Invoke the method using method.invoke(objectInstance, params...)
     *
     * Caveat: only find method declared in the current Class, not inherited from supertypes
     */
    public static Method getPrivateMethod(Class objectClass, String methodName) throws NoSuchMethodException {
        Method method = objectClass.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method;
    }

    public static void renameFile(File file, String newFileName) {
        try {
            Files.copy(file, new File(newFileName));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Gets mid point of a node relative to the screen.
     * @param node
     * @return
     */
    public static Point2D getScreenMidPoint(Node node) {
        double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x,y);
    }

    /**
     * Gets mid point of a node relative to its scene.
     * @param node
     * @return
     */
    public static Point2D getSceneMidPoint(Node node) {
        double x = getScenePos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScenePos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x,y);
    }

    /**
     * Gets the bound of the node relative to the parent scene.
     * @param node
     * @return
     */
    public static Bounds getScenePos(Node node) {
        return node.localToScene(node.getBoundsInLocal());
    }

    public static Bounds getScreenPos(Node node) {
        return node.localToScreen(node.getBoundsInLocal());
    }

    public static double getSceneMaxX(Scene scene) {
        return scene.getX() + scene.getWidth();
    }

    public static double getSceneMaxY(Scene scene) {
        return scene.getX() + scene.getHeight();
    }

    public static Object getLastElement(List<?> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Removes a subset from the list of persons.
     * @param persons The list of persons
     * @param personsToRemove The subset of persons.
     * @return The modified persons after removal of the subset from persons.
     */
    public static TestTask[] removePersonsFromList(final TestTask[] persons, TestTask... personsToRemove) {
        List<TestTask> listOfPersons = asList(persons);
        listOfPersons.removeAll(asList(personsToRemove));
        return listOfPersons.toArray(new TestTask[listOfPersons.size()]);
    }


    /**
     * Returns a copy of the list with the person at specified index removed.
     * @param list original list to copy from
     * @param targetIndexInOneIndexedFormat e.g. if the first element to be removed, 1 should be given as index.
     */
    public static TestTask[] removeTaskFromList(final TestTask[] list, int targetIndexInOneIndexedFormat) {
        List<TestTask> listOfPersons = asList(list);
        sortList(listOfPersons);
        listOfPersons.remove(targetIndexInOneIndexedFormat-1);
        return listOfPersons.toArray(new TestTask[listOfPersons.size()]);
    }
    
    //@@author A0138301U
    /* methods to sort lists before comparison */
    public static void sortList(List<TestTask> listOfTasks) {
        Collections.sort(listOfTasks, new Comparator<TestTask>() {
            @Override
            public int compare(TestTask task1, TestTask task2) 
            {
                int value = task1.getPinTask().compareTo(task2.getPinTask());
                if(value == 0) {
                    value = task1.getStatus().compareTo(task2.getStatus());
                    if(value == 0) {
                        value = task1.getPriority().compareTo(task2.getPriority());
                        if(value == 0) {
                            return task1.getName().fullName.compareTo(task2.getName().fullName);
                        }
                        return value;
                    }
                    return value;
                }
                return value;
            }
        });
    }

    public static void sortReadList(List<ReadOnlyTask> listOfTasks) {
        Collections.sort(listOfTasks, new Comparator<ReadOnlyTask>() {
            @Override
            public int compare(ReadOnlyTask task1, ReadOnlyTask task2) 
            {
                int value = task1.getPinTask().compareTo(task2.getPinTask());
                if(value == 0) {
                    value = task1.getStatus().compareTo(task2.getStatus());
                    if(value == 0) {
                        value = task1.getPriority().compareTo(task2.getPriority());
                        if(value == 0) {
                            return task1.getName().fullName.compareTo(task2.getName().fullName);
                        }
                        return value;
                    }
                    return value;
                }
                return value;
            }
        });
    }
        
    //@@author
    

    /**
     * Replaces persons[i] with a person.
     * @param persons The array of persons.
     * @param person The replacement person
     * @param index The index of the person to be replaced.
     * @return
     */
    public static TestTask[] replacePersonFromList(TestTask[] persons, TestTask person, int index) {
        persons[index] = person;
        return persons;
    }

    /**
     * Appends persons to the array of persons.
     * @param persons A array of persons.
     * @param personsToAdd The persons that are to be appended behind the original array.
     * @return The modified array of persons.
     */
    public static TestTask[] addTasksToList(final TestTask[] persons, TestTask... personsToAdd) {
        List<TestTask> listOfPersons = asList(persons);
        listOfPersons.addAll(asList(personsToAdd));
        TestUtil.sortList(listOfPersons);
        return listOfPersons.toArray(new TestTask[listOfPersons.size()]);
    }

    private static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for(T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndTask(TaskCardHandle card, ReadOnlyTask task) {
        return card.isSameTask(task);
    }

    public static Tag[] getTagList(String tags) {

        if (tags.equals("")) {
            return new Tag[]{};
        }

        final String[] split = tags.split(", ");

        final List<Tag> collect = Arrays.asList(split).stream().map(e -> {
            try {
                return new Tag(e.replaceFirst("Tag: ", ""));
            } catch (IllegalValueException e1) {
                //not possible
                assert false;
                return null;
            }
        }).collect(Collectors.toList());

        return collect.toArray(new Tag[split.length]);
    }

}
