package ChatApp;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {

    private JTextArea history;
    private JTextField txtSend;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;

    public Server() {
        super("Server");
        initUI();
    }

    private void initUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        txtSend = new JTextField();
        txtSend.setEditable(false);
        txtSend.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendData(e.getActionCommand());
                txtSend.setText("");
            }
        });
        add(txtSend, BorderLayout.SOUTH);

        history = new JTextArea();
        history.setEditable(false);
        add(new JScrollPane(history), BorderLayout.CENTER);
        setSize(500, 400);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void runServer() {
        try {
            server = new ServerSocket(54321, 10);
            while (true) {
                try {
                    waitForConnection();
                    getStreams();
                    processConnection();
                } catch (EOFException ex) {
                    displayMessage("\n Server terminated connection");
                } finally {
                    closeConnection();
                    counter++;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        displayMessage("waiting for connection");
        connection = server.accept();//
        displayMessage("Connection " + counter + " received from: "
                + connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());

        displayMessage("\nGot I/O Streams");
    }

    private void processConnection() throws IOException {
        String message = "Connection Successfull";
        sendData(message);

        changetxtSendStatus(true);
        do {
            try {
                message = (String) input.readObject();
                displayMessage("\n" + message);
            } catch (ClassNotFoundException ex) {
                displayMessage("Unknown Object type received");
            }
        } while (!message.equals("Client>> bye"));

    }

    private void closeConnection() {

        displayMessage("\nTerminating Connection\n");
        changetxtSendStatus(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendData(String message) {
        try {
            output.writeObject("Server>> " + message);
            output.flush();
            displayMessage("\nServer>> " + message);
        } catch (IOException ex) {
            history.append("Error writing object");
        }
    }

    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                history.append(message);
            }
        });
    }

    private void changetxtSendStatus(final boolean b) {
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                txtSend.setEditable(b);
            }
        });
    }

    public static void main(String[] args) {
        Server app = new Server();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.runServer();
        //app.setSize(600,550);
        //app.setVisible(true);
    }
}
