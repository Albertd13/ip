import chatonator.task.Task;
import chatonator.task.TaskList;
import chatonator.task.Todo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {
    @Test
    public void addTask_todoTask_tasksUpdated() {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo task = new Todo("Test");
        TaskList taskList = new TaskList(tasks);

        taskList.add(task);
        assertEquals(taskList.getAll(), List.of(new Todo("Test")));
    }

}
