package fr.zeteox.javachatroom.client;

import fr.zeteox.javachatroom.protocol.Message;
import fr.zeteox.javachatroom.protocol.MessageSerializer;
import fr.zeteox.javachatroom.protocol.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket server;
    private String username;
    private MessageReceiver messageReceiver;

    public ChatClient() {}

    public void connect(String host, int port, String username, Consumer<Message> onMessage) {
        try {
            this.username = username;
            server = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            writer = new PrintWriter(server.getOutputStream(), true);

            Message connectMsg = new Message(MessageType.CONNECT, username, username);
            writer.println(MessageSerializer.toCsv(connectMsg));

            messageReceiver = new MessageReceiver(reader, onMessage);
            Thread receiverThread = new Thread(messageReceiver, "MessageReceiver-" + username);
            receiverThread.setDaemon(true);
            receiverThread.start();

        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to " + host + ":" + port, e);
        }
    }

    public void sendMessage(String content) {
        if (server == null || server.isClosed()) return;
        Message msg = new Message(MessageType.TEXT, username, content);
        writer.println(MessageSerializer.toCsv(msg));
    }

    public void disconnect() {
        if (server == null || server.isClosed()) return;
        try {
            Message disconnectMsg = new Message(MessageType.DISCONNECT, username, username);
            writer.println(MessageSerializer.toCsv(disconnectMsg));
            writer.flush();

            if (messageReceiver != null) {
                messageReceiver.stop();
            }
            writer.close();
            reader.close();
            server.close();
        } catch (IOException e) {
            System.err.println("Error during disconnect: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return server != null && !server.isClosed() && server.isConnected();
    }
}