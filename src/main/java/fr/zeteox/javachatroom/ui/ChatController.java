package fr.zeteox.javachatroom.ui;

import fr.zeteox.javachatroom.client.ChatClient;
import fr.zeteox.javachatroom.protocol.Message;
import fr.zeteox.javachatroom.protocol.MessageType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class ChatController {

    @FXML private TextArea messagesArea;
    @FXML private ListView<String> usersListView;
    @FXML private TextField inputField;
    @FXML private Button sendButton;
    @FXML private TextField pseudoField;
    @FXML private Button connectButton;
    @FXML private Label statusLabel;

    private ChatClient chatClient;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        setConnectedState(false);

        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSendButtonClick();
            }
        });
    }

    @FXML
    protected void onConnectButtonClick() {
        String pseudo = pseudoField.getText().trim();
        if (pseudo.isEmpty()) {
            statusLabel.setText("Please enter a username.");
            return;
        }

        chatClient = new ChatClient();
        try {
            chatClient.connect("127.0.0.1", 5000, pseudo, this::handleIncomingMessage);
            setConnectedState(true);
            statusLabel.setText("Connected as " + pseudo);

            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.setOnCloseRequest(e -> chatClient.disconnect());

        } catch (RuntimeException e) {
            statusLabel.setText("Connection failed: " + e.getCause().getMessage());
        }
    }

    @FXML
    protected void onSendButtonClick() {
        if (chatClient == null || !chatClient.isConnected()) return;

        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        chatClient.sendMessage(text);
        inputField.clear();
    }

    private void handleIncomingMessage(Message message) {
        switch (message.getType()) {
            case TEXT -> Platform.runLater(() -> appendMessage(message));
            case CONNECT -> Platform.runLater(() -> {
                addUser(message.getPseudo());
                appendSystemMessage("→ " + message.getPseudo() + " joined the chat");
            });
            case DISCONNECT -> Platform.runLater(() -> {
                removeUser(message.getPseudo());
                appendSystemMessage("← " + message.getPseudo() + " left the chat");
            });
            case SERVER_INFO -> Platform.runLater(() ->
                    appendSystemMessage("[Server] " + message.getContent())
            );
        }
    }

    private void appendMessage(Message message) {
        String time = message.getDate().format(TIME_FMT);
        messagesArea.appendText("[" + time + "] " + message.getPseudo() + ": " + message.getContent() + "\n");
    }

    private void appendSystemMessage(String text) {
        messagesArea.appendText("--- " + text + " ---\n");
    }

    private void addUser(String pseudo) {
        if (!usersListView.getItems().contains(pseudo)) {
            usersListView.getItems().add(pseudo);
        }
    }

    private void removeUser(String pseudo) {
        usersListView.getItems().remove(pseudo);
    }

    private void setConnectedState(boolean connected) {
        inputField.setDisable(!connected);
        sendButton.setDisable(!connected);
        connectButton.setDisable(connected);
        pseudoField.setDisable(connected);
    }
}