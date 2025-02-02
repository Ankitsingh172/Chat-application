import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to the chat server. Type messages below:");

            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("\n" + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            receiveThread.start();

            String userInput;
            while ((userInput = reader.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
