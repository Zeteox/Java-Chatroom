package fr.zeteox.javachatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    PrintWriter writer;
    BufferedReader reader;
    Socket server;

    public ChatClient() {}

    public void connect(String host, int port, String username) {
        try {
            server = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            writer = new PrintWriter(server.getOutputStream(), true);
            sendMessage("CONNECT " + username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listen(Consumer<String> onMessageReceived) {
        Thread listener = new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    onMessageReceived.accept(message);
                }
            } catch (IOException e) {
                System.err.println("Disconnected: " + e.getMessage());
            }
        });
        listener.setDaemon(true);
        listener.start();
    }

    public void sendMessage(String message) {
        if (server == null) return;
        writer.println(message);
    }

    public void disconnect() throws IOException {
        if (server == null) return;
        writer.println("DISCONNECT");
        writer.flush();
        writer.close();
        reader.close();
        server.close();
    }
}
