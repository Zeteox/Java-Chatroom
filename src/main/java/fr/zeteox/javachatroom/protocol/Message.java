package fr.zeteox.javachatroom.protocol;

import java.time.LocalDateTime;

public class Message {
    private MessageType type;
    private String pseudo;
    private String content;
    private LocalDateTime date;

    public Message(MessageType type, String pseudo, String content) {
        this.type = type;
        this.pseudo = pseudo;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Message(MessageType type, String pseudo, String content, LocalDateTime date) {
        this.type = type;
        this.pseudo = pseudo;
        this.content = content;
        this.date = date;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
