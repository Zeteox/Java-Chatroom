# Java Chatroom

Application de chat en temps réel — serveur TCP Java + client JavaFX.

## Prérequis

- **Java 17+**
- **Maven** (ou utiliser le wrapper `./mvnw` inclus)
- **IntelliJ IDEA** (recommandé pour le serveur)

---

## Lancer le serveur

Dans IntelliJ, lancer directement la classe principale du serveur :

```
fr.zeteox.javachatroom.server.ChatServer
```

> Run > Run 'Launcher' — sans arguments.

Le serveur écoute sur le port **5000** par défaut.

---

## Lancer le client JavaFX

Dans un terminal à la racine du projet :

```bash
./mvnw javafx:run
```

> Sur Windows : `mvnw.cmd javafx:run`

L'interface s'ouvre, entrez un pseudo et cliquez sur **Connexion**.

---

## Compiler sans lancer

```bash
./mvnw compile
```

---

## Ordre de démarrage

1. Démarrer le **serveur** en premier (IntelliJ)
2. Lancer autant de **clients** que souhaité (`./mvnw javafx:run` dans des terminaux séparés)
3. Les clients se connectent sur `127.0.0.1:5000`

---

## Structure du projet

```
src/main/java/fr/zeteox/javachatroom/
├── server/
│   ├── ChatServer.java        # Point d'entrée serveur
│   ├── ClientHandler.java     # Thread par client connecté
│   └── ClientRegistry.java    # Registre des clients (Singleton)
├── client/
│   └── ChatClient.java        # Connexion TCP côté client
├── protocol/
│   ├── Message.java           # Modèle de message
│   └── MessageType.java       # Enum TEXT / CONNECT / DISCONNECT / SERVER_INFO
└── ui/
    ├── ChatApplication.java   # Point d'entrée JavaFX
    ├── ChatController.java    # Contrôleur FXML
    └── chatPage.fxml          # Layout de l'interface
```

---

## Dépendances principales

| Dépendance | Version |
|---|---|
| JavaFX Controls + FXML | 17.0.14 |
| JUnit Jupiter | 5.12.1 |