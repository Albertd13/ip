import Chatonator.task.Deadline;
import Chatonator.task.Task;
import Chatonator.task.TaskList;
import Chatonator.task.Todo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {
    @Test
    public void addTask_todoTask_tasksUpdated() {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo task = new Todo("Test");
        TaskList taskList = new TaskList(tasks);

        taskList.add(task);
        assertEquals(taskList.getAll(), Arrays.asList(new Todo("Test")));
    }

}
