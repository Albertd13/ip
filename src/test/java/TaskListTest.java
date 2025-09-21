import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import chatonator.task.Task;
import chatonator.task.TaskList;
import chatonator.task.Todo;

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
