package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private Button sendButton, connectButton, disconnectButton;
    private TextArea textArea;
    private Label feedbackLabel;
    private Client client;

    public void connectToUI(Stage primaryStage) {
        sendButton = (Button) primaryStage.getScene().lookup("#sendBtn");
        textArea = (TextArea) primaryStage.getScene().lookup("#messageField");
        feedbackLabel = (Label) primaryStage.getScene().lookup("#feedbackLbl");
        connectButton = (Button) primaryStage.getScene().lookup("#connectBtn");
        disconnectButton = (Button) primaryStage.getScene().lookup("#disconnectBtn");
        client = new Client(this);
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    client.connect("localhost", 5555);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    client.sendMessage(textArea.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        disconnectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.disconnect();
            }
        });

    }

    public void setFeedback(String feedback){
        this.feedbackLabel.setText(feedback);
    }
}
