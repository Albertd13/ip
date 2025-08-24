import Chatonator.Chatonator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatonatorTest {
    @Test
    @Timeout(1)
    public void run_userInputsBye_stopsRunning() {
        String simulatedBye = "bye\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedBye.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        new Chatonator().run(in);
        System.setOut(originalOut);
        assertEquals("""
                ____________________________________________________________
                Hello! I'm CHATONATOR!
                What can I do for you?
                ____________________________________________________________
                
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                
                """,
            out.toString().replace("\r", ""));
    }
}
