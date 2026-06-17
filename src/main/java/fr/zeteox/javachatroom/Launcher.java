package fr.zeteox.javachatroom;

import fr.zeteox.javachatroom.server.ChatServer;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        if (args.length > 0) {
            Application.launch(ChatApplication.class, args);
        } else {
            new ChatServer().listen(5000);
        }
    }
}
