package fr.zeteox.javachatroom.server;

import fr.zeteox.javachatroom.protocol.Message;
import fr.zeteox.javachatroom.protocol.MessageSerializer;
import fr.zeteox.javachatroom.protocol.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final BufferedReader clientReader;
    private final PrintWriter clientWriter;
    private final ClientRegistry clientRegistry;

    private String pseudo = "unknown";

    public ClientHandler(Socket client, ClientRegistry clientRegistry) throws IOException {
        this.clientSocket = client;
        this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientRegistry = clientRegistry;
        clientRegistry.registerClient(this);
    }

    public void sendMessage(Message message) {
        clientWriter.println(MessageSerializer.toCsv(message));
    }

    public String getPseudo() {
        return pseudo;
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = clientReader.readLine()) != null) {
                try {
                    Message message = MessageSerializer.fromCsv(line);
                    handleMessage(message);
                } catch (Exception e) {
                    System.err.println("[ClientHandler] Bad message from " + pseudo + ": " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("[ClientHandler] " + pseudo + " lost connection: " + e.getMessage());
        } finally {
            handleDisconnect();
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case CONNECT -> {
                this.pseudo = message.getPseudo();
                System.out.println("[Server] " + pseudo + " connected.");
                clientRegistry.broadcastMessage(message);
                sendCurrentUserList();
            }
            case TEXT -> {
                System.out.println("[" + pseudo + "]: " + message.getContent());
                clientRegistry.broadcastMessage(message);
            }
            case DISCONNECT -> {
                System.out.println("[Server] " + pseudo + " disconnected gracefully.");
                throw new RuntimeException("Graceful disconnect");
            }
            default -> System.out.println("[Server] Unhandled message type: " + message.getType());
        }
    }

    private void sendCurrentUserList() {
        String users = clientRegistry.getClients().stream()
                .map(ClientHandler::getPseudo)
                .filter(p -> !p.equals("unknown"))
                .reduce((a, b) -> a + ", " + b)
                .orElse("(none)");

        Message infoMsg = new Message(MessageType.SERVER_INFO, "server",
                "Connected users: " + users);
        sendMessage(infoMsg);
    }

    private void handleDisconnect() {
        clientRegistry.unregisterClient(this);
        System.out.println("[Server] Unregistered " + pseudo);

        Message leaveMsg = new Message(MessageType.DISCONNECT, pseudo, pseudo);
        clientRegistry.broadcastMessage(leaveMsg);

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("[ClientHandler] Error closing socket for " + pseudo);
        }
    }
}