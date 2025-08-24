package Chatonator;

import Chatonator.exceptions.InvalidChatInputException;
import jdk.jshell.spi.ExecutionControl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Chatonator {
    private static final Path SAVE_FILE_PATH = Paths.get("./data/saveFile.txt");
    private final Ui ui = new Ui();

    public void run() {
        CommandHandler commandHandler = new CommandHandler(new Storage(SAVE_FILE_PATH));
        ui.greet();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String fullCommand = scanner.nextLine().trim();
                if (fullCommand.startsWith("bye")) {
                    break;
                }
                String response = commandHandler.handleCommand(fullCommand);
                ui.sendMessage(response);
            } catch (ExecutionControl.NotImplementedException | InvalidChatInputException e) {
                ui.sendMessage(e.getMessage());
            }

        }
        ui.exitBye();
    }

    public static void main(String[] args) {
        Chatonator chatbot = new Chatonator();
        chatbot.run();
    }
}
