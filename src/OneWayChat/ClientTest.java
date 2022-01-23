package OneWayChat;


import javax.swing.JFrame;

public class ClientTest
{
   public static void main( String[] args )
   {
      Client application = new Client(); // create client
      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      application.waitForPackets(); // run client application
   } // end main
}  // end class ClientTest


