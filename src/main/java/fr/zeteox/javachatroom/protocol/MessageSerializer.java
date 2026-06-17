package fr.zeteox.javachatroom.protocol;

import java.time.LocalDateTime;

public class MessageSerializer {

    public static String toCsv(Message msg) {
        return String.join(";",
                msg.getType().name(),
                msg.getPseudo(),
                msg.getContent(),
                msg.getDate().toString()
        );
    }

    public static Message fromCsv(String csv) {
        String[] parts = csv.split(";", 4);

        return new Message(
                MessageType.valueOf(parts[0]),
                parts[1],
                parts[2],
                LocalDateTime.parse(parts[3])
        );
    }

    public static String toJson(Message msg) {
        return String.format(
                "{\"type\":\"%s\",\"pseudo\":\"%s\",\"content\":\"%s\",\"date\":\"%s\"}",
                msg.getType().name(),
                escape(msg.getPseudo()),
                escape(msg.getContent()),
                msg.getDate().toString()
        );
    }

    public static Message fromJson(String json) {
        String type = json.replaceAll(".*\"type\":\"([^\"]+)\".*", "$1");
        String pseudo = json.replaceAll(".*\"pseudo\":\"([^\"]+)\".*", "$1");
        String content = json.replaceAll(".*\"content\":\"([^\"]+)\".*", "$1");
        String date = json.replaceAll(".*\"date\":\"([^\"]+)\".*", "$1");

        return new Message(
                MessageType.valueOf(type),
                pseudo,
                content,
                LocalDateTime.parse(date)
        );
    }

    private static String escape(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}