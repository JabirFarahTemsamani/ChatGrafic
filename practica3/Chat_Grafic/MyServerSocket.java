import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {
    public ServerSocket serverSocket;

    public MyServerSocket(int port) throws IOException {

        try {
        
            serverSocket = new ServerSocket(port);
        
        } catch (IOException e) {
        
            throw new IOException("Error al iniciar el servidor: " + e.getMessage(), e);
        }
    }

    public MySocket accept() throws IOException {

        try {

            Socket socket = serverSocket.accept();
            return new MySocket(socket);

        } catch (IOException e) {

            throw new IOException("Error al aceptar conexión: " + e.getMessage(), e);
        }
    }

    public void close() throws IOException {

        try {

            serverSocket.close();

        } catch (IOException e) {
            
            throw new IOException("Error al cerrar el servidor: " + e.getMessage(), e);
        }
    }
}