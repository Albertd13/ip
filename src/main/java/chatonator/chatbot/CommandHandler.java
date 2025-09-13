package chatonator.chatbot;

import chatonator.Storage;
import chatonator.exceptions.InvalidChatInputException;
import chatonator.task.Deadline;
import chatonator.task.Event;
import chatonator.task.Task;
import chatonator.task.TaskList;
import chatonator.task.TimedTask;
import chatonator.task.Todo;
import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles commands from the user input
 */
public class CommandHandler {
    private final TaskList taskList;
    private final Storage storage;
    public static String EXIT_MESSAGE = "Have a nice day! Come back again after completing some tasks!";

    /**
     * Initialises the handler with link to task storage
     * @param storage Used to store task information in saveFile if save command is used
     */
    public CommandHandler(Storage storage) {
        this.taskList = new TaskList(storage.restoreTasks());
        this.storage = storage;
    }

    /**
     * Handles users string input commands and executes them directly
     * @param fullCommand the string the user input, max one command
     * @return the response after handling the command
     * @throws ExecutionControl.NotImplementedException typically occurs when a command that does not exist is used
     */
    public String handleCommand(String fullCommand) throws ExecutionControl.NotImplementedException {

        // splits the main command word with the rest of the command
        String[] commandArr = fullCommand.split(" ", 2);
        String currentCommand = commandArr[0];
        return switch (currentCommand) {
        case "list" -> numberedTasks(this.taskList.getAll());
        case "mark" -> markTask(commandArr);
        case "delete" -> deleteTask(commandArr);
        case "deadline" -> {
            Deadline deadline = getDeadline(commandArr);
            taskList.add(deadline);
            yield taskAdditionResponse(deadline);
        }
        case "timed" -> {
            TimedTask timedTask = getTimedTask(commandArr);
            taskList.add(timedTask);
            yield taskAdditionResponse(timedTask);
        }
        case "todo" -> {
            Todo todo = getTodo(commandArr);
            taskList.add(todo);
            yield taskAdditionResponse(todo);
        }
        case "event" -> {
            Event event = getEvent(commandArr);
            taskList.add(event);
            yield taskAdditionResponse(event);
        }
        case "save" -> saveTasks();
        case "find" -> findTasks(commandArr);
        case "bye" -> EXIT_MESSAGE;
        default -> throw new ExecutionControl.NotImplementedException("Sorry! I do not understand.");
        };
    }

    /**
     *
     * @return Response to user after task save is attempted
     */
    private String saveTasks() {
        try {
            storage.saveTasks(taskList.getAll());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "Tasks could not be saved due to an error!";
        } catch (ExecutionControl.NotImplementedException e) {
            return "This task has not been implemented yet!";
        }
        return "Tasks saved successfully!";
    }
    /**
     * Gets a to-do task depending on commandArr from user input
     * @param commandArr array split into main command and command details
     * @return 'to-do' task
     */
    private static Todo getTodo(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Give a description for your todo!");
        }
        return new Todo(commandArr[1]);
    }

    /**
     *
     */
    private static TimedTask getTimedTask(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Give a duration for the task! Use /for XXhrs XXmins XXs");
        }
        String[] taskDetails = commandArr[1].split("/for ");
        return new TimedTask(taskDetails[0], myDurationParse(taskDetails[1]));

    }

    private static Duration myDurationParse(String inputStr) {
        Duration result = Duration.ZERO;
        result = result.plus(Duration.ofHours(Integer.parseInt(inputStr.split("hrs")[0])));
        result = result.plus(Duration.ofMinutes(Integer.parseInt(inputStr.split(" ")[1].split("mins")[0])));
        result = result.plus(Duration.ofSeconds(Integer.parseInt(inputStr.split(" ")[2].split("s")[0])));
        return result;
    }
    /**
     * Creates Event object based on command params
     * @param commandArr contains individual command words
     * @return Event
     */
    private static Event getEvent(String[] commandArr) {
        assert commandArr[0].equals("event");
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Give a description for your event!");
        }
        String[] taskDetails = commandArr[1].split("/from | /to");
        if (taskDetails.length < 2) {
            throw new InvalidChatInputException("Add /from <start> /to <end> for event!");
        }
        return new Event(taskDetails[0], taskDetails[1].trim(), taskDetails[2].trim());
    }

    /**
     * Creates Deadline object based on command params
     * @param commandArr must contain a valid LocalDate representation of YYYY-MM-DD in index 1
     * @return Deadline
     */
    private static Deadline getDeadline(String[] commandArr) {
        assert commandArr[0].equals("event");
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Give a description for your deadline!");
        }
        String[] taskDetails = commandArr[1].split("/by");
        if (taskDetails.length < 2) {
            throw new InvalidChatInputException("Add /by <due date> for deadlines!");
        }
        return new Deadline(taskDetails[0].trim(), LocalDate.parse(taskDetails[1].trim()));
    }

    /**
     * Deletes a task from the TaskList
     * @param commandArr must contain a valid index of task to delete
     * @return response message from deletion
     */
    private String deleteTask(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Enter index of task to delete!");
        }
        if (!isInt(commandArr[1])) {
            throw new InvalidChatInputException("Enter a valid index! delete <index>");
        }
        int index = Integer.parseInt(commandArr[1]) - 1;
        Task deletedTask = taskList.getTask(index);
        taskList.removeTask(index);
        return String.format("""
            Noted. I've removed this task:
                %s
            Now you have %d tasks in the list.
            """,
                deletedTask, taskList.getCount()
        );
    }

    /**
     * Checks if a string contains only an integer
     * @param intStr string containing ONLY an integer
     * @return true if string contains only an integer
     */
    private boolean isInt(String intStr) {
        try {
            Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Marks task as complete
     * @param markCommandArr must contain valid task index
     * @return response message regardless of success or failure to complete task
     */
    private String markTask(String[] markCommandArr) {
        if (markCommandArr.length < 2 || !isInt(markCommandArr[1])) {
            return "Invalid mark command";
        }
        int taskIndex = Integer.parseInt(markCommandArr[1]) - 1;
        if (taskIndex >= taskList.getCount() || taskIndex < 0) {
            return "Invalid task Index";
        }
        Task selectedTask = taskList.getTask(taskIndex);
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
                taskList.getCount()
        );
    }

    private String numberedTasks(List<Task> taskList) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
            res.append(String.format("%d. %s\n", i + 1, taskList.get(i)));
        }
        return res.toString();
    }

    /**
     * Searches available tasks for matching keyword, matches as long as keyword is a substring
     * @param commandArr must contain keyword to search for in index 1
     * @return string containing filtered, numbered list of tasks
     */
    private String findTasks(String[] commandArr) {
        if (commandArr.length < 2) {
            throw new InvalidChatInputException("Enter a keyword to find!");
        }
        List<Task> matchingTasks = this.taskList.getAll()
                .stream()
                .filter(
                        task -> task.name.matches(String.format(".*%s.*", commandArr[1]))
                ).toList();
        return numberedTasks(matchingTasks);
    }
}
