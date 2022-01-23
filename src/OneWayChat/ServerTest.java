package OneWayChat;

import javax.swing.JFrame;

public class ServerTest {

    public static void main(String[] args) {
        Server application = new Server(); // create server
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.waitForPackets(); // run server application
    } // end main
} // end class ServerTest

