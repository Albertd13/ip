package chatonator.task;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void removeTask(int index) {
        tasks.remove(index);
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public List<Task> getAll() {
        return tasks;
    }


    public void add(Task task) {
        tasks.add(task);
    }
    public int getCount() {
        return tasks.size();
    }
}
