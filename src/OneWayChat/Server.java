package OneWayChat;// Fig. 27.9: Server.java
// Server side of connectionless client/server computing with datagrams.

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame {

    private JTextArea displayArea; // displays packets received
    private DatagramSocket socket; // socket to connect to client

    // set up GUI and DatagramSocket
    public Server() {
        super("Server");

        displayArea = new JTextArea(); // create displayArea
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(400, 300); // set size of window
        setVisible(true); // show window

        try // create DatagramSocket for sending and receiving packets
        {
            socket = new DatagramSocket(5000);
        } // end try
        catch (SocketException socketException) {
            socketException.printStackTrace();
            System.exit(1);
        } // end catch
    } // end Server constructor

    // wait for packets to arrive, display data and echo packet to client
    public void waitForPackets() {
        while (true) {
            try // receive packet, display contents, return copy to client
            {
                byte[] data = new byte[100]; // set up packet
                DatagramPacket receivePacket
                        = new DatagramPacket(data, data.length);

                socket.receive(receivePacket); // wait to receive packet

                // display information from received packet 
                displayMessage("\nPacket received:"
                        + "\nFrom host: " + receivePacket.getAddress()
                        + "\nHost port: " + receivePacket.getPort()
                        + "\nLength: " + receivePacket.getLength()
                        + "\nContaining:\n\t" + new String(receivePacket.getData(),
                                0, receivePacket.getLength()));

                sendPacketToClient(receivePacket); // send packet to client
            } // end try
            catch (IOException ioException) {
                displayMessage(ioException + "\n");
                ioException.printStackTrace();
            } // end catch
        } // end while
    } // end method waitForPackets

    // echo packet to client
    private void sendPacketToClient(DatagramPacket receivePacket)
            throws IOException {
        displayMessage("\n\nEcho data to client...");

        // create packet to send
        DatagramPacket sendPacket = new DatagramPacket(
                receivePacket.getData(), receivePacket.getLength(),
                receivePacket.getAddress(), receivePacket.getPort());

        socket.send(sendPacket); // send packet to client
        displayMessage("Packet sent\n");
    } // end method sendPacketToClient

    // manipulates displayArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
            public void run() // updates displayArea
            {
                displayArea.append(messageToDisplay); // display message
            } // end method run
        } // end anonymous inner class
        ); // end call to SwingUtilities.invokeLater
    } // end method displayMessage
} // end class Server

