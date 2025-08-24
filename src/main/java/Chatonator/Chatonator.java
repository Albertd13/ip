package Chatonator;

import Chatonator.exceptions.InvalidChatInputException;
import jdk.jshell.spi.ExecutionControl;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Chatonator {
    private static final Path SAVE_FILE_PATH = Paths.get("./data/saveFile.txt");
    private final Ui ui = new Ui();

    /**
     *
     * Starts running the chatbot
     * @param source should be kept to System.in unless for testing purposes only
     */
    public void run(InputStream source) {
        CommandHandler commandHandler = new CommandHandler(new Storage(SAVE_FILE_PATH));
        ui.greet();
        Scanner scanner = new Scanner(source);
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
        chatbot.run(System.in);
    }
}
