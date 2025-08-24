import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class CommandHandler {
    private final ArrayList<Task> tasks;
    private final Storage storage;
    public CommandHandler(Storage storage) {
        this.tasks = new ArrayList<>(storage.restoreTasks());
        this.storage = storage;
    }

    public String handleCommand(String fullCommand) throws ExecutionControl.NotImplementedException {

        String[] commandArr = fullCommand.split(" ", 2);
        String currentCommand = commandArr[0];
        return switch (currentCommand) {
            case "list" -> getNumberedTasks();
            case "mark" -> markTask(commandArr);
            case "delete" -> deleteTask(commandArr);
            case "deadline" -> {
                Deadline deadline = getDeadline(commandArr);
                tasks.add(deadline);
                yield taskAdditionResponse(deadline);
            }
            case "todo" -> {
                if (commandArr.length < 2) {
                    throw new InvalidChatInputException("Give a description for your todo!");
                }
                Todo todo = new Todo(commandArr[1]);
                tasks.add(todo);
                yield taskAdditionResponse(todo);
            }
            case "event" ->  {
                Event event = getEvent(commandArr);
                tasks.add(event);
                yield taskAdditionResponse(event);
            }
            case "save" -> {
                try {
                    storage.saveTasks(tasks);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    yield "Tasks could not be saved due to an error!";
                } catch (ExecutionControl.NotImplementedException e) {
                    yield "Task was not implemented yet!";
                }
                yield "Tasks saved successfully!";
            }
            default -> throw new ExecutionControl.NotImplementedException("Sorry! I do not understand.");
        };
    }

    private static Event getEvent(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Give a description for your event!");
        }
        String[] taskDetails = commandArr[1].split("/from | /to");
        if (taskDetails.length < 2) {
            throw new InvalidChatInputException("Add /from <start> /to <end> for event!");
        }
        return new Event(taskDetails[0], taskDetails[1].trim(), taskDetails[2].trim());
    }

    private static Deadline getDeadline(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Give a description for your deadline!");
        }
        String[] taskDetails = commandArr[1].split("/by");
        if (taskDetails.length < 2) {
            throw new InvalidChatInputException("Add /by <due date> for deadlines!");
        }
        return new Deadline(taskDetails[0].trim(), LocalDate.parse(taskDetails[1].trim()));
    }

    private String deleteTask(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Enter index of task to delete!");
        }
        if (!isInt(commandArr[1])) {
            throw new InvalidChatInputException("Enter a valid index! delete <index>");
        }
        int index = Integer.parseInt(commandArr[1]) - 1;
        Task deletedTask = tasks.get(index);
        tasks.remove(index);
        return String.format("""
            Noted. I've removed this task:
                %st
            Now you have %d tasks in the list.
            """,
                deletedTask, tasks.size()
        );
    }

    private boolean isInt(String intStr) {
        try {
            Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private String markTask(String[] markCommandArr) {
        if (markCommandArr.length < 2 || !isInt(markCommandArr[1])) {
            return "Invalid mark command";
        }
        int taskIndex = Integer.parseInt(markCommandArr[1]) - 1;
        if (taskIndex >= tasks.size() || taskIndex < 0) {
            return "Invalid Task Index";
        }
        Task selectedTask = tasks.get(taskIndex);
        selectedTask.complete();
        return String.format("""
            Nice! I've marked this task as done:
            %s""",
                selectedTask
        );
    }

    private String taskAdditionResponse(Task task) {
        return String.format("""
            Got it. I've added this task:
                %s
            Now you have %d tasks in the list.""",
                task,
                tasks.size()
        );
    }

    private String getNumberedTasks() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            res.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }
        return res.toString();
    }
}
