import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import chatonator.Storage;
import chatonator.chatbot.CommandHandler;
import jdk.jshell.spi.ExecutionControl;

public class CommandTest {
    @Test
    public void run_userInputsBye_returnsExitMessage() throws ExecutionControl.NotImplementedException {
        String simulatedBye = "bye";
        Storage storage = new Storage(Path.of("data/test.txt"));
        assertEquals(CommandHandler.EXIT_MESSAGE, new CommandHandler(storage).handleCommand(simulatedBye));
    }
}
