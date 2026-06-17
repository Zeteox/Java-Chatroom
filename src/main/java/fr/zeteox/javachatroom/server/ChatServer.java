package fr.zeteox.javachatroom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public ChatServer() {}

    public void listen(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            ClientRegistry registry = ClientRegistry.getInstance();
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New client connected on port " + client.getPort());
                System.out.println("Client connected ip: " + client.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(client, registry);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
