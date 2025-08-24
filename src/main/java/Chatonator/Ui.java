package Chatonator;

public class Ui {
    public void greet() {
        this.sendMessage("""
                     Hello! I'm CHATONATOR!
                     What can I do for you?"""
        );
    }

    public void exitBye() {
        this.sendMessage("Bye. Hope to see you again soon!");
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
    public void sendMessage(String message) {
        System.out.println(formatMessage(message));
    }
}
