package fr.zeteox.javachatroom.ui;

import fr.zeteox.javachatroom.client.ChatClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChatController {
    @FXML
    private Label connectText;

    ChatClient chatClient;

    public ChatController() {
        chatClient = new ChatClient();
    }

    @FXML
    protected void onConnectButtonClick() {
        chatClient.connect("127.0.0.1", 5000, "test");
        chatClient.listen(message ->
                Platform.runLater(() -> connectText.setText(message))
        );
    }

    @FXML
    protected void onSendButtonClick() {
        chatClient.sendMessage("Hello");
    }
}
