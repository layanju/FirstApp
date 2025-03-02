package pack305;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 8080; // رقم المنفذ
    private static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Server is running on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
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

                // استلام اسم المستخدم
                username = in.readLine();
                clients.put(username, out);
                System.out.println(username + " has connected.");

                // استقبال الرسائل ومعالجتها
                String message;
                while ((message = in.readLine()) != null) {
                    processMessage(message);
                }
            } catch (IOException e) {
                System.out.println(username + " disconnected.");
            } finally {
                try {
                    clients.remove(username);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processMessage(String message) {
            // الرسالة تكون بصيغة recipient:message
            String[] parts = message.split(":", 2);
            if (parts.length == 2) {
                String recipient = parts[0];
                String chatMessage = parts[1];

                PrintWriter recipientOut = clients.get(recipient);
                if (recipientOut != null) {
                    recipientOut.println(username + ": " + chatMessage);
                } else {
                    out.println("User " + recipient + " is not online.");
                }
            } else {
                out.println("Invalid message format. Use recipient:message");
            }
        }
    }
}
