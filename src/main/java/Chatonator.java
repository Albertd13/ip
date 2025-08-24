import jdk.jshell.spi.ExecutionControl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;


public class Chatonator {
    private static final Path SAVE_FILE_PATH = Paths.get("./data/saveFile.txt");
    private ArrayList<Task> tasks = new ArrayList<>();
    private final Storage storage;

    public Chatonator() {
        this.storage = new Storage(SAVE_FILE_PATH);
    }

    public void run() {
        this.tasks = new ArrayList<>(storage.restoreTasks());
        String greeting = """
                         Hello! I'm CHATONATOR!
                         What can I do for you?""";
        String byeResponse = "Bye. Hope to see you again soon!";

        System.out.println(formatMessage(greeting));
        Scanner scanner = new Scanner(System.in);
        String currentCommand;
        while (true) {
            try {
                String[] commandArr = scanner.nextLine().trim().split(" ", 2);
                currentCommand = commandArr[0];
                if (currentCommand.equals("bye")) {
                    break;
                }
                String response = switch (currentCommand) {
                    case "list" -> getNumberedMessage();
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
                System.out.println(formatMessage(response));
            } catch (ExecutionControl.NotImplementedException | InvalidChatInputException e) {
                System.out.println(formatMessage(e.getMessage()));
            }

        }
        System.out.println(formatMessage(byeResponse));
    }

    public static void main(String[] args) {
        Chatonator chatbot = new Chatonator();
        chatbot.run();
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
    private String formatMessage(String message) {
        return String.format(
                """
                ____________________________________________________________
                %s
                ____________________________________________________________
                """, message.trim()
        );
    }
    private String getNumberedMessage() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            res.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }
        return res.toString();
    }
}
