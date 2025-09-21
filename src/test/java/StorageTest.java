import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import chatonator.Storage;
import chatonator.task.Todo;

public class StorageTest {
    private final Path TEST_SAVE_FILE_PATH = Path.of("data/testSave.txt");
    private final Todo testTask; {
        testTask = new Todo("Test");
        testTask.complete();
    };
    @Test
    public void restore_saveFileWithSingleTask_correctTasks() {
        Storage storage = new Storage(TEST_SAVE_FILE_PATH);
        assertEquals(1, storage.restoreTasks().size());
        assertEquals(testTask, storage.restoreTasks().get(0));
    }

    @Test
    public void saveTasks_saveSingleTask_fileCreated() {
        Storage storage = new Storage(TEST_SAVE_FILE_PATH);
        try {
            storage.saveTasks(java.util.List.of(testTask));
            assertTrue(Files.exists(TEST_SAVE_FILE_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
