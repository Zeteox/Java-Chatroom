package fr.zeteox.javachatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    BufferedReader clientReader;
    PrintWriter clientWriter;

    public ClientHandler(Socket client, ClientRegistry clientRegistry) throws IOException {
        clientSocket = client;
        clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        clientRegistry.registerClient(this);
    }

    public void sendMessage(String message) throws IOException {  // called by broadcastMessage
        clientWriter.println(message);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = clientReader.readLine()) != null) {
                ClientRegistry.getInstance().broadcastMessage(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
