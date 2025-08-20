import java.util.Scanner;

public class Chatonator {
    public static void main(String[] args) {
        String greeting = """
                         Hello! I'm CHATONATOR!
                         What can I do for you?""";
        System.out.println(formatMessage(greeting));
        String byeResponse = "Bye. Hope to see you again soon!";
        Scanner scanner = new Scanner(System.in);
        String currentCommand;
        while (true) {
            currentCommand = scanner.nextLine().trim();
            if (currentCommand.equals("bye")) {
                break;
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
                """, message
        );
    }

}
