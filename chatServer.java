import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcastMessage(String message, String sender) {
        for (ClientHandler client : clients.values()) {
            if (!client.getUsername().equals(sender)) {
                client.sendMessage(sender + ": " + message);
            }
        }
    }

    static void privateMessage(String recipient, String message, String sender) {
        ClientHandler client = clients.get(recipient);
        if (client != null) {
            client.sendMessage("[Private] " + sender + ": " + message);
        } else {
            clients.get(sender).sendMessage("User " + recipient + " not found.");
        }
    }

    static void addClient(String username, ClientHandler clientHandler) {
        clients.put(username, clientHandler);
    }

    static void removeClient(String username) {
        clients.remove(username);
    }
}
