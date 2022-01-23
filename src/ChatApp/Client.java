package ChatApp;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {

    private JTextField txtSend;
    private JTextArea history;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private String message = " ";
    private String server;

    public Client(String server) {
        super("Client");

        this.server = server;
        initUI();

    }

    public void initUI() {

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
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
        add(new JScrollPane(history), BorderLayout.CENTER);
        setSize(500, 400);
        setVisible(true);
        setLocationRelativeTo(null);//
    }

    private void runClient() {
        try {
            connectToServer();
            getStreams();
            processConnection();

        } catch (EOFException ex) {
            displayMessage("\nClient terminated connection");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void connectToServer() throws IOException {
        displayMessage("Attempting Connection");
        client = new Socket(InetAddress.getByName(server), 54321);
        displayMessage("Connected to: " + client.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();

        input = new ObjectInputStream(client.getInputStream());

        displayMessage("\n Got I/O streams");
    }

    private void processConnection() throws IOException {
        setTextFieldEditable(true);
        do {
            try {
                message = (String) input.readObject();
                displayMessage("\n" + message);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        } while (!message.equals("Server>> bye"));
    }

    private void closeConnection() {
        displayMessage("\nClosing connection");
        setTextFieldEditable(false);

        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendData(String message) {
        try {
            output.writeObject("Client>> " + message);
            output.flush();
            displayMessage("\nClient>> " + message);
        } catch (IOException ex) {
            history.append("Error writing object");
        }
    }

    private void displayMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                history.append(message);
            }
        });
    }

    private void setTextFieldEditable(final boolean b) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                txtSend.setEditable(b);
            }
        });
    }

    public static void main(String[] args) {
        Client app;
        if (args.length == 0) {
            app = new Client("127.0.0.1");
        } else {
            app = new Client(args[0]);
        }
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.runClient();

    }
}
