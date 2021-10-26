package Server;

import java.net.*;

/**
 * HTTP Server instance running on localhost at port 6789
 *
 * Based off of a class with a similar name, authored by José Legatheaux Martins
 * in his book (accessible at https://legatheaux.eu/book/cnfbook-pub.pdf) [Listing 5.15]
 *
 * @author nunoDias fc56330
 * @author brunaSantos fc56328
 * @author alexandrePinto fc55958
 */
public class HTTPServer{

  private static final int PORT = 6789;

  public static void main(String[] args){
    try{
      Logger log = new Logger("log.txt");
      ServerSocket srvSock = new ServerSocket(PORT); // Server socket is never closed
      for(;;){
	      System.out.println("Awaiting connection...");
        Socket socket = srvSock.accept(); // a client connected
	      System.out.println("Connection estabilished!");
        ClientHandler thread = new ClientHandler(socket, log);
        (new Thread(thread)).start();
      }
      // never reached, leaves by exception
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
