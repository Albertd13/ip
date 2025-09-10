package chatonator;

import chatonator.task.Deadline;
import chatonator.task.Event;
import chatonator.task.Task;
import chatonator.task.TimedTask;
import chatonator.task.Todo;
import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path saveFilePath;
    public Storage (Path saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    /**
     * Saves provided tasks to a .txt file
     * @param tasks
     * @throws IOException
     * @throws ExecutionControl.NotImplementedException
     */
    public void saveTasks(List<Task> tasks) throws IOException, ExecutionControl.NotImplementedException {
        try {
            Files.createDirectories(saveFilePath.getParent());
            if (Files.notExists(saveFilePath)) {
                Files.createFile(saveFilePath);
            }
            ArrayList<String> saveStrings = new ArrayList<>();
            for (Task t: tasks) {
                try {
                    String saveStr = getTaskSaveStr(t);
                    saveStrings.add(saveStr);
                } catch (ExecutionControl.NotImplementedException e) {
                    throw new ExecutionControl.NotImplementedException("This task type doesn't exist yet!");
                }
            }
            Files.write(saveFilePath, saveStrings);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * @param task
     * @return encoded string representing a task to be saved in .txt file
     * @throws ExecutionControl.NotImplementedException
     */
    private static String getTaskSaveStr(Task task) throws ExecutionControl.NotImplementedException {
        String baseString = String.format("%d|%s", task.getStatus() ? 1 : 0, task.name);
        if (task instanceof Todo) {
            return "T|" + baseString;
        } else if (task instanceof Deadline d) {
            return String.format("D|%s|%s", baseString, d.getBy());
        } else if (task instanceof Event e) {
            return String.format("E|%s|%s|%s", baseString, e.getFrom(), e.getTo());
        } else if (task instanceof TimedTask timed) {
            return String.format("TIM|%s|%s", baseString, timed.getDuration());
        } else {
            throw new ExecutionControl.NotImplementedException("Task type saving is not implemented!");
        }
    }

    /**
     *
     * @return list of tasks from the saveFile if it exists, otherwise returns empty list
     */
    public List<Task> restoreTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (Files.notExists(saveFilePath)) {
            return tasks;
        }
        try {
            List<String> savedLines = Files.readAllLines(saveFilePath);
            for (String saveStr: savedLines) {
                Task t = parseTaskStr(saveStr);
                tasks.add(t);
            }
        } catch (IOException e) {
            return tasks;
        }
        return tasks;
    }

    /**
     * Parses string from saveFile to convert to a task
     * @param saveStr string containing task details
     * @return Task
     */
    private static Task parseTaskStr(String saveStr) {
        String[] contents = saveStr.split("\\|");
        Task t = switch (contents[0]) {
        case "D" -> new Deadline(contents[2], LocalDate.parse(contents[3]));
        case "E" -> new Event(contents[2], contents[3], contents[4]);
        case "TIM" -> new TimedTask(contents[2], Duration.parse(contents[3]));
        default -> new Todo(contents[2]);
        };
        if (contents[1].equals("1")) {
            t.complete();
        }
        return t;
    }

}
