import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private final MyServerSocket serverSocket;
    private final Map<String, MySocket> clients = new ConcurrentHashMap<>();

    public ChatServer(int port) throws IOException {
        serverSocket = new MyServerSocket(port);
    }

    
    public void start() {

        System.out.println("Servidor iniciado. Esperando conexiones...");

        while (true) {

            try {
               
                MySocket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado.");
                new Thread(() -> handleClient(clientSocket)).start();

            } catch (IOException e) {

                System.err.println("Error al aceptar una conexión: " + e.getMessage());
            }
        }
    }

    private void handleClient(MySocket clientSocket) {
        try {
            
            String nick = clientSocket.readLine();

            if (nick == null || nick.isEmpty()) {

                System.err.println("Cliente sin nick, cerrando conexión.");
                clientSocket.close();
                return;
            }

            
            if (clients.putIfAbsent(nick, clientSocket) != null) {

                System.err.println("Nick ya en uso: " + nick);
                clientSocket.writeLine("ERROR Nick ya en uso. Desconectando...");
                clientSocket.close();
                return;
            }

            
            System.out.println("Cliente registrado con nick: " + nick);
            broadcast("Servidor: " + nick + " se ha unido al chat.", nick);

            sendUserListToAllClients();
            
            String message;

            while ((message = clientSocket.readLine()) != null) {

                if (message.equals("DESCONECTADO...")) {


                    clients.remove(nick);
                    sendUserListToAllClients();   
                    broadcast("Servidor: " + nick + " se ha desconectado del chat.", nick);
                
                }else{
                
                    broadcast(nick + ": " + message, nick);
                }
                
            }

        } catch (IOException e) {

            System.err.println("Error al manejar cliente: " + e.getMessage());
        } finally {

            this.disconnectClient(clientSocket);
        }
    }

  
    private void sendUserListToAllClients() {

        StringBuilder usersList = new StringBuilder();
    
        for (String user : clients.keySet()) {
            if (usersList.length() > 0) {
                usersList.append(",");
            }

            usersList.append(user); // Añadir el nombre del usuario a la lista // Cadena es nombre1,mobre2,nombre3,........
        }
    
        broadcast("USERS:" + usersList.toString(), null);
         

        
    }


    private void broadcast(String message, String senderNick) {
        
        System.out.println(message); // Log en el servidor

        for (Map.Entry<String, MySocket> entry : clients.entrySet()) {

            entry.getValue().writeLine(message);
            
        }
    }
    
    private void disconnectClient(MySocket clientSocket) {

        clients.values().removeIf(socket -> socket == clientSocket);

        try {

            clientSocket.close();

        } catch (IOException e) {
            
            System.err.println("Error al cerrar conexión de cliente: " + e.getMessage());
        }
    }

    // Método main
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Formato: java ChatServer <puerto>");
            return;
        }

        int port = Integer.parseInt(args[0]); //int port=12345;
        
        try {

            ChatServer server = new ChatServer(port);
            server.start();

        } catch (IOException e) {
            
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
