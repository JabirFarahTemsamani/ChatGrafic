import java.io.*;
import java.net.Socket;

public class MySocket {
    
    public Socket socket;
    public BufferedReader reader;
    public PrintWriter writer;

    public MySocket(String host, int port) throws IOException {

        try {

            this.socket = new Socket(host, port);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {

            throw new IOException("Error al conectar con el servidor: " + e.getMessage(), e);
        }
    }

    public MySocket(Socket socket) throws IOException {

        try {

            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {

            throw new IOException("Error al conectar con el servidor: "  + e.getMessage(), e);
        }
    }


    public String readLine() throws IOException {
    
        return reader.readLine();
    }

    public void writeLine(String line) {

        writer.println(line);
    }

    public void close() throws IOException {

        try {

            reader.close();
            writer.close();
            socket.close();

        } catch (IOException e) {
            
            throw new IOException("Error al cerrar MySocket: " + e.getMessage(), e);
        }
    }


}
