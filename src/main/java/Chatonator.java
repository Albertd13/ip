import java.util.ArrayList;
import java.util.Scanner;

public class Chatonator {
    private static final ArrayList<Task> tasks = new ArrayList<>();
    public static void main(String[] args) {
        String greeting = """
                         Hello! I'm CHATONATOR!
                         What can I do for you?""";
        String byeResponse = "Bye. Hope to see you again soon!";

        System.out.println(formatMessage(greeting));
        Scanner scanner = new Scanner(System.in);
        String currentCommand;
        while (true) {
            String[] commandArr = scanner.nextLine().trim().split(" ");
            currentCommand = commandArr[0];
            if (currentCommand.equals("bye")) {
                break;
            }
            String response = switch (currentCommand) {
                case "list" -> getNumberedMessage();
                case "mark" -> markTask(commandArr);
                default -> {
                    tasks.add(new Task(currentCommand));
                    yield "added: " + currentCommand;
                }
            };
            System.out.println(formatMessage(response));
        }
        System.out.println(formatMessage(byeResponse));

    }
    private static boolean isInt(String intStr) {
        try {
            Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    private static String markTask(String[] markCommandArr) {
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
    private static String formatMessage(String message) {
        return String.format(
                """
                ____________________________________________________________
                %s
                ____________________________________________________________
                """, message.trim()
        );
    }
    private static String getNumberedMessage() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            res.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }
        return res.toString();
    }
}
