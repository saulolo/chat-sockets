package chat_sockets.view;

import chat_sockets.data.ConversationDAOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class CustomerA extends JFrame implements Observer {

    private JTextArea chatArea;
    private JTextField txtMessage, txtName;
    private JButton btnSend, btnConnect, btnDisconnect;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Thread listenerThread;
    private boolean connected = false;

    ConversationDAOImpl conversationDAO = new ConversationDAOImpl();

    public CustomerA() {
        setTitle("Chat - Cliente A");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con el nombre del usuario y botones de conexión
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Tu nombre:"));
        txtName = new JTextField(15);
        topPanel.add(txtName);

        btnConnect = new JButton("Conectar");
        btnConnect.addActionListener(this::connectToServer);
        topPanel.add(btnConnect);

        btnDisconnect = new JButton("Desconectar");
        btnDisconnect.addActionListener(this::disconnectFromServer);
        btnDisconnect.setEnabled(false);
        topPanel.add(btnDisconnect);

        add(topPanel, BorderLayout.NORTH);

        // Área del chat (solo lectura)
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior (escribir mensaje)
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        txtMessage = new JTextField();
        btnSend = new JButton("Enviar");
        btnSend.addActionListener(this::sendMessage);
        btnSend.setEnabled(false);
        bottomPanel.add(txtMessage, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Conectar al servidor
    private void connectToServer(ActionEvent evt) {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre para conectarse.");
            return;
        }

        try {
            socket = new Socket("localhost", 5000);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.println(name);

            listenerThread = new Thread(this::listenForMessages);
            listenerThread.start();

            connected = true;
            btnConnect.setEnabled(false);
            btnDisconnect.setEnabled(true);
            btnSend.setEnabled(true);
            chatArea.append("Conectado al servidor como " + name + "\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor: " + e.getMessage());
        }
    }

    // Escuchar mensajes
    private void listenForMessages() {
        try {
            String msg;
            while ((msg = input.readLine()) != null) {
                chatArea.append(msg + "\n");
            }
        } catch (IOException e) {
            if (connected)
                chatArea.append("Conexión perdida.\n");
        }
    }

    // Enviar mensaje
    private void sendMessage(ActionEvent evt) {
        if (!connected) return;
        String msg = txtMessage.getText().trim();
        if (!msg.isEmpty()) {
            output.println(txtName.getText() + ": " + msg);
            txtMessage.setText("");
        }
    }

    // Desconectar
    private void disconnectFromServer(ActionEvent evt) {
        try {
            connected = false;
            if (socket != null) socket.close();
            btnConnect.setEnabled(true);
            btnDisconnect.setEnabled(false);
            btnSend.setEnabled(false);
            chatArea.append("Desconectado del servidor.\n");
        } catch (IOException e) {
            chatArea.append("Error al desconectar: " + e.getMessage() + "\n");
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerA::new);
    }


}