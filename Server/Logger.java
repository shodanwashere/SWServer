package Server;

import java.io.*;
import java.util.*;

/**
 * Class meant to generate moment-by-moment server logs
 *
 * Based off of a class with the same name, authored by José Legatheaux Martins
 * in his book (accessible at https://legatheaux.eu/book/cnfbook-pub.pdf) [Listing 5.17]
 * 
 * @author nunoDias fc56330
 * @author brunaSantos fc56328
 * @author alexandrePinto fc55958
 *
 */
public class Logger{
  private PrintStream f;

  /**
   * Generates a Logger instance, connected to the specified log file through a PrintStream
   *
   * @param logFileName - name of the log file
   * @return An instance of the Logger class
   */
  public Logger(String logFileName){
    try {
      f = new PrintStream(logFileName);
      String message = "Logging started at " + new Date().toString();
      f.println(message+"\n");
    } catch (IOException e) {
      System.err.println("Can't open log file " + logFileName);
      System.exit(0);
    }
  }

  /**
   * Writes a string into the log file, then flushes the used stream
   */
  public synchronized void writeEntry(String s){
    f.println(s);
    f.flush();
  }
}
