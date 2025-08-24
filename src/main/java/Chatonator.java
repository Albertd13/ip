import jdk.jshell.spi.ExecutionControl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Chatonator {
    private static final Path SAVE_FILE_PATH = Paths.get("./data/saveFile.txt");
    private final Storage storage;

    public Chatonator() {
        this.storage = new Storage(SAVE_FILE_PATH);
    }

    public void run() {
        String greeting = """
                         Hello! I'm CHATONATOR!
                         What can I do for you?""";
        String byeResponse = "Bye. Hope to see you again soon!";
        Parser parser = new Parser(storage);
        System.out.println(formatMessage(greeting));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String fullCommand = scanner.nextLine().trim();
                if (fullCommand.startsWith("bye")) {
                    break;
                }
                String response = parser.handleCommand(fullCommand);
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

    private String formatMessage(String message) {
        return String.format(
                """
                ____________________________________________________________
                %s
                ____________________________________________________________
                """, message.trim()
        );
    }
}
