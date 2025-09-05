package chatonator.chatbot;

import java.nio.file.Path;
import java.nio.file.Paths;

import chatonator.Storage;
import chatonator.exceptions.InvalidChatInputException;
import jdk.jshell.spi.ExecutionControl;


public class Chatonator {
    private final Path SAVE_FILE_PATH = Paths.get("./data/saveFile.txt");
    private final CommandHandler commandHandler = new CommandHandler(new Storage(SAVE_FILE_PATH));


    /**
     * Gets the response to user input
     * @param input user's input chat message
     * @return Response messagea
     */
    public String getResponse(String input) {
        try {
            return commandHandler.handleCommand(input);
        } catch (ExecutionControl.NotImplementedException | InvalidChatInputException e) {
            return(e.getMessage());
        }
    }
}
