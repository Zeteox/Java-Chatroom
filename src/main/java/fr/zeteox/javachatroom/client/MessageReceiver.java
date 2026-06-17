package fr.zeteox.javachatroom.client;

import fr.zeteox.javachatroom.protocol.Message;
import fr.zeteox.javachatroom.protocol.MessageSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

public class MessageReceiver implements Runnable {

    private final BufferedReader reader;
    private final Consumer<Message> onMessage;
    private volatile boolean running = true;

    public MessageReceiver(BufferedReader reader, Consumer<Message> onMessage) {
        this.reader = reader;
        this.onMessage = onMessage;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            String line;
            while (running && (line = reader.readLine()) != null) {
                try {
                    Message message = MessageSerializer.fromCsv(line);
                    onMessage.accept(message);
                } catch (Exception e) {
                    System.err.println("[MessageReceiver] Could not parse message: " + line);
                    System.err.println("  Cause: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("[MessageReceiver] Connection lost: " + e.getMessage());
            }
        }
    }
}