import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient {
    public MySocket clientSocket;
    BufferedReader keyboardInput;

    public ChatClient(String host, int port) throws IOException {
        clientSocket = new MySocket(host, port);
        this.keyboardInput = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() {

        
        Thread inputThread = new Thread(() -> {

            try  {

                String line;

                while ((line = keyboardInput.readLine()) != null) {
                    clientSocket.writeLine(line);
                }
                
                clientSocket.close();
            
            } catch (IOException e) {

                System.err.println("Error en el Input Thread: " + e.getMessage());
            }
        });

        
        Thread outputThread = new Thread(() -> {

            try {

                String line;

                while ((line = clientSocket.readLine()) != null) {
                    System.out.println(line);
                }

            } catch (IOException e) {

                System.err.println("Error en el Output Thread: " + e.getMessage());

            } finally {

                try {

                    clientSocket.close();

                } catch (IOException e) {

                    System.err.println("Error al cerrar el cliente: " + e.getMessage());
                }
            }
        });

        
        inputThread.start();
        outputThread.start();

        // Asegurarse de que los threads terminan
        try {

            inputThread.join();
            outputThread.join();

        } catch (InterruptedException e) {
            
            System.err.println("Transmision finalizada: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        if (args.length != 2) {

            System.out.println("Formato: java ChatClient <host> <puerto>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]); 

        // String host = "127.0.0.1";
        // int port = 12345;

        try {

            ChatClient client = new ChatClient(host, port);
            System.out.print("Intoduce un nick a continuación: ");
            client.start();

        } catch (IOException e) {

            System.err.println("Error al iniciar el cliente: " + e.getMessage());
        }
    }
}
