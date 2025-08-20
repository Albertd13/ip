import java.util.ArrayList;
import java.util.Scanner;

public class Chatonator {
    public static void main(String[] args) {
        String greeting = """
                         Hello! I'm CHATONATOR!
                         What can I do for you?""";
        String byeResponse = "Bye. Hope to see you again soon!";

        System.out.println(formatMessage(greeting));

        ArrayList<String> tasks = new ArrayList();
        Scanner scanner = new Scanner(System.in);
        String currentCommand;
        while (true) {
            currentCommand = scanner.nextLine().trim();
            if (currentCommand.equals("bye")) {
                break;
            }
            if (currentCommand.equals("list")) {
                currentCommand = getNumberedMessage(tasks);
            } else {
                tasks.add(currentCommand);
                currentCommand = "added: " + currentCommand;
            }
            System.out.println(formatMessage(currentCommand));
        }
        System.out.println(formatMessage(byeResponse));

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
    private static String getNumberedMessage(ArrayList<String> messages) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            res.append(String.format("%d. %s\n", i + 1, messages.get(i)));
        }
        return res.toString();
    }

}
