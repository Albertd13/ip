package chatonator.controller;

import chatonator.chatbot.Chatonator;
import chatonator.chatbot.CommandHandler;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private Chatonator chatonator;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/surprisePikachu.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/Trollface.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setChatonator(Chatonator d) {
        chatonator = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Chatonator's reply
     * and then appends them to the dialog container. Clears the user input after processing.
     * If user types 'exit', app closes after a short delay.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = chatonator.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, dukeImage)
        );
        userInput.clear();
        // Exits the application if chatonator returns the exit response
        if (response.equals(CommandHandler.EXIT_MESSAGE)) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.3));
            delay.setOnFinished(event -> {
                Platform.exit();
                System.exit(0);
            });
            delay.play();
        }
    }
}