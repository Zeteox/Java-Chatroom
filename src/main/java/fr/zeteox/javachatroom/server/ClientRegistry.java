package fr.zeteox.javachatroom.server;

import fr.zeteox.javachatroom.protocol.Message;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientRegistry {
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static ClientRegistry clientRegistry;

    public static ClientRegistry getInstance() {
        if (clientRegistry == null) {
            synchronized (ClientRegistry.class) {
                if (clientRegistry == null) {
                    clientRegistry = new ClientRegistry();
                }
            }
        }
        return clientRegistry;
    }

    public void registerClient(ClientHandler client) {
        clients.add(client);
    }

    public void unregisterClient(ClientHandler client) {
        clients.remove(client);
    }

    public List<ClientHandler> getClients() {
        return List.copyOf(clients);
    }

    public void broadcastMessage(Message message) {
        for (ClientHandler client : clients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                unregisterClient(client);
            }
        }
    }
}
