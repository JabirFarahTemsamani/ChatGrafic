import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;

public class ChatClientGrafico {
    public MySocket clientSocket;
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JList<String> userList;
    private DefaultListModel<String> userModel;
    

    public ChatClientGrafico(String host, int port) throws IOException {

        clientSocket = new MySocket(host, port);

        initializeGUI();
    }

    private void initializeGUI() {

        frame = new JFrame("Chat Gráfico");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {  
            
            @Override
            public void windowClosing(WindowEvent e) {

                disconnectClient();
                super.windowClosing(e);
                System.exit(0);
            }
        });

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        frame.add(messageScrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        frame.add(inputField, BorderLayout.SOUTH);

        userModel = new DefaultListModel<>();
        userList = new JList<>(userModel);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        frame.add(userScrollPane, BorderLayout.EAST);

        inputField.addActionListener(e -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                inputField.setText("");
            }
        });

        frame.setVisible(true);
    }

    public void start() {

        String nick = JOptionPane.showInputDialog(frame, "Introduce tu nick:", "Nick", JOptionPane.PLAIN_MESSAGE);
        if (nick == null || nick.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nick inválido. Cerrando cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        clientSocket.writeLine(nick);
     
        new Thread(() -> {
            try {
                String line;
                while ((line = clientSocket.readLine()) != null) {
                    handleIncomingMessage(line);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Conexión perdida con el servidor: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }).start();
    }

    private void sendMessage(String message) {
        
        clientSocket.writeLine(message);
    }

    private void disconnectClient() {
        
        try {

            clientSocket.writeLine("DESCONECTADO...");
            clientSocket.close();
            
        } catch (IOException e) {

            JOptionPane.showMessageDialog(frame, "Error al desconectar el cliente: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

        } finally {

            System.exit(0);
        }
    }

    private void handleIncomingMessage(String message) {

        SwingUtilities.invokeLater(() -> {

            if (message.startsWith("USERS:")) {
                
                userModel.clear();
                String[] users = message.substring(6).split(",");
                
                for (String user : users) {
                    userModel.addElement(user.trim());
                }

            } else {
    
                messageArea.append(message + "\n");
            }
        });
    }

    public static void main(String[] args) {
        
        if (args.length != 2) {
            System.out.println("Formato: java ChatClientGrafico <host> <puerto>");
            return;
        }

        String host = args[0]; //String host = "127.0.0.1";//
        int port = Integer.parseInt(args[1]); // int port = 12345;

        try {
            ChatClientGrafico client = new ChatClientGrafico(host, port);
            client.start();
        } catch (IOException e) {
            System.err.println("Error al iniciar el cliente: " + e.getMessage());
        }
    }
}
