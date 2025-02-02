import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Prompt for username
            out.println("Enter your username:");
            username = in.readLine();
            ChatServer.addClient(username, this);
            ChatServer.broadcastMessage(username + " has joined the chat.", "Server");

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.startsWith("@")) {
                    String[] parts = clientMessage.split(" ", 2);
                    if (parts.length > 1) {
                        String recipient = parts[0].substring(1); // Remove @ symbol
                        String message = parts[1];
                        ChatServer.privateMessage(recipient, message, username);
                    } else {
                        out.println("Invalid private message format. Use: @username message");
                    }
                } else {
                    ChatServer.broadcastMessage(clientMessage, username);
                }
            }
        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            ChatServer.removeClient(username);
            ChatServer.broadcastMessage(username + " has left the chat.", "Server");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
