package Server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Class meant to handle HTTPClient Requests Implements the Runnable interface
 * (is supposed to run on a thread)
 *
 * @author nunoDias fc56330
 * @author brunaSantos fc56328
 * @author alexandrePinto fc55958
 *
 */
public class ClientHandler implements Runnable {
  private Socket socket;
	private Logger log;
	private String myName;
  private String serverDirectory = "./Server/www";

	public ClientHandler(Socket s, Logger log) {
		this.socket = s;
		this.log = log;
	}
  
  /**
   * Function that returns a string with the current date and time in
   * ANSI C's asctime format; this is compatible with HTTP/1.1;
   *
   * @return Current date in asctime format (weekday SP month SP dayMonth SP time SP year)
   */
  private String asctime(){

  	Calendar currDate = Calendar.getInstance();
    StringBuilder properDate = new StringBuilder("");

    // Weekday
    switch(currDate.get(Calendar.DAY_OF_WEEK)){
      case 1: properDate.append("Sun"); break;
      case 2: properDate.append("Mon"); break;
      case 3: properDate.append("Tue"); break;
      case 4: properDate.append("Wed"); break;
      case 5: properDate.append("Thu"); break;
      case 6: properDate.append("Fri"); break;
      case 7: properDate.append("Sat"); break;
    }

    properDate.append(" ");

    // Month
    switch(currDate.get(Calendar.MONTH)){
      case 0: properDate.append("Jan"); break;
      case 1: properDate.append("Feb"); break;
      case 2: properDate.append("Mar"); break;
      case 3: properDate.append("Apr"); break;
      case 4: properDate.append("May"); break;
      case 5: properDate.append("Jun"); break;
      case 6: properDate.append("Jul"); break;
      case 7: properDate.append("Aug"); break;
      case 8: properDate.append("Sep"); break;
      case 9: properDate.append("Oct"); break;
      case 10: properDate.append("Nov"); break;
      case 11: properDate.append("Dec"); break;
    }

    // Day of the Month
    properDate.append(" " + currDate.get(Calendar.DAY_OF_MONTH) + " ");

    //Time
    // \ HOUR
    if(currDate.get(Calendar.HOUR_OF_DAY) < 10){
      properDate.append("0");
    }
    properDate.append(currDate.get(Calendar.HOUR_OF_DAY) + ":");

    // \ MINUTE
    if(currDate.get(Calendar.MINUTE) < 10){
      properDate.append("0");
    }
    properDate.append(currDate.get(Calendar.MINUTE) + ":");

    // \ SECOND
    if(currDate.get(Calendar.SECOND) < 10){
      properDate.append("0");
    }
    properDate.append(currDate.get(Calendar.SECOND) + " ");

    //Year
    properDate.append(currDate.get(Calendar.YEAR));

    return properDate.toString();
  }

  /**
   * Function that takes a UNIX epoch and converts into a string with the
   * current date and time in ANSI C's asctime format; this is compatible with
   * HTTP/1.1
   * 
   * @param datetime - long containing a UNIX epch
   * @return Given date in asctime format
   */
  private static String millisToAsctime(long datetime){
		Calendar currDate = Calendar.getInstance();
		currDate.setTimeInMillis(datetime);
		StringBuilder properDate = new StringBuilder("");

		// Weekday
		switch(currDate.get(Calendar.DAY_OF_WEEK)){
			case 1: properDate.append("Sun"); break;
			case 2: properDate.append("Mon"); break;
			case 3: properDate.append("Tue"); break;
			case 4: properDate.append("Wed"); break;
			case 5: properDate.append("Thu"); break;
			case 6: properDate.append("Fri"); break;
			case 7: properDate.append("Sat"); break;
		}

    		properDate.append(" ");

		// Month
		switch(currDate.get(Calendar.MONTH)){
			case 0: properDate.append("Jan"); break;
			case 1: properDate.append("Feb"); break;
			case 2: properDate.append("Mar"); break;
			case 3: properDate.append("Apr"); break;
			case 4: properDate.append("May"); break;
			case 5: properDate.append("Jun"); break;
			case 6: properDate.append("Jul"); break;
			case 7: properDate.append("Aug"); break;
			case 8: properDate.append("Sep"); break;
			case 9: properDate.append("Oct"); break;
			case 10: properDate.append("Nov"); break;
			case 11: properDate.append("Dec"); break;
		}

		// Day of the Month
		properDate.append(" " + currDate.get(Calendar.DAY_OF_MONTH) + " ");

		//Time
		// \ HOUR
		if(currDate.get(Calendar.HOUR_OF_DAY) < 10){
			properDate.append("0");
		}
		properDate.append(currDate.get(Calendar.HOUR_OF_DAY) + ":");

		// \ MINUTE
		if(currDate.get(Calendar.MINUTE) < 10){
			properDate.append("0");
		}
		properDate.append(currDate.get(Calendar.MINUTE) + ":");

		// \ SECOND
		if(currDate.get(Calendar.SECOND) < 10){
			properDate.append("0");
		}
		properDate.append(currDate.get(Calendar.SECOND) + " ");

		//Year
		properDate.append(currDate.get(Calendar.YEAR));

		return properDate.toString();
	}
  
  private long asctimeToMillis(String dateTime){
    long thing;
    Calendar receiver = Calendar.getInstance();
    System.out.println(dateTime);
    String[] splitDateTime = dateTime.split(" ");

    int weekday;
    switch(splitDateTime[0]){
      case "Sun": weekday = Calendar.SUNDAY; break;
      case "Mon": weekday = Calendar.MONDAY; break;
      case "Tue": weekday = Calendar.TUESDAY; break;
      case "Wed": weekday = Calendar.WEDNESDAY; break;
      case "Thu": weekday = Calendar.TUESDAY; break;
      case "Fri": weekday = Calendar.FRIDAY; break;
      case "Sat": weekday = Calendar.SATURDAY; break;
      default: weekday = 0;
    }
    receiver.set(Calendar.DAY_OF_WEEK, weekday);

    int month;
    switch(splitDateTime[1]){
      case "Jan": month = Calendar.JANUARY; break;
      case "Feb": month = Calendar.FEBRUARY; break;
      case "Mar": month = Calendar.MARCH; break;
      case "Apr": month = Calendar.APRIL; break;
      case "May": month = Calendar.MAY; break;
      case "Jun": month = Calendar.JUNE; break;
      case "Jul": month = Calendar.JULY; break;
      case "Aug": month = Calendar.AUGUST; break;
      case "Sep": month = Calendar.SEPTEMBER; break;
      case "Oct": month = Calendar.OCTOBER; break;
      case "Nov": month = Calendar.NOVEMBER; break;
      case "Dec": month = Calendar.DECEMBER; break;
      default: month = 0;
    }
    receiver.set(Calendar.MONTH, month);

    receiver.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splitDateTime[2]));

    String[] time = splitDateTime[3].split(":");
    receiver.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
    receiver.set(Calendar.MINUTE, Integer.valueOf(time[1]));
    receiver.set(Calendar.SECOND, Integer.valueOf(time[2]));

    receiver.set(Calendar.YEAR, Integer.valueOf(splitDateTime[4]));

    thing = receiver.getTimeInMillis();
    return thing;
  }

  /**
   * Function that checks if a request must be responded to with a 304 status reply
   *
   * @param headerValue Value obtained from the If-Modified-Since header in the request
   * @param lastModifDateTime Date of the last modification made to the requested file
   * @return true if it has been modified since the given Date/Time, false otherwise
   */
  private boolean checkIfModifiedSince(String headerValue, long lastModifDateTime){
    System.out.println("[HTTP/1.1] % Checking If-Modified-Since %");
    long ifModifiedSinceLong = asctimeToMillis(headerValue);
    System.out.println("Date/Time for Check: " + ifModifiedSinceLong);
    System.out.println("Last Modified: " + lastModifDateTime);
     //
     // READ CAREFULLY BEFORE WRITING THE REST OF THIS FUNCTION!!!
     //
     //  splitHeaderValue[0] == WEEKDAY
     //  splitHeaderValue[1] == MONTH
     //  splitHeaderValue[2] == DAY
     //  splitHeaderValue[3] == TIME (Split with regex ":" )
     //  splitHeaderValue[4] == YEAR
     //
     // PROCESS, THEN PLACE THIS INTO A CALENDAR INSTANCE
     // DO THE SAME TO lastModifDateTime
     // THEN CHECK
     // IF headerValue < lastModifDateTime
     //   THEN true
     // ELSE
     //   false
     
    return (lastModifDateTime) > ifModifiedSinceLong;
  }

  public void run() {
    myName = Thread.currentThread().getName() + " ";
    System.out.println("[HTTP/1.1] Starting new thread " + myName);
    log.writeEntry(myName + new Date().toString() + " starting\n");
    try {
      System.out.println("[HTTP/1.1] Reading request from socket...\n");
      BufferedReader bfReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      System.out.println("[HTTP/1.1] Received new request \n");

      String s;
      s = bfReader.readLine();
     
      log.writeEntry(myName + new Date().toString() + "received request : " + s + "\n");
      boolean badRequest = false;
      int badStatus = 0;

      if(s == null){
        System.out.println("[HTTP/1.1] SYNTAX ERROR FOUND :: Preparing 404 Malformed Request");
        badRequest = true;
        badStatus = 400;
      }

      System.out.println(s);
      String[] arr = s.split(" ");
      if(arr.length != 3){
	      System.out.println("[HTTP/1.1] SYNTAX ERROR FOUND :: Preparing 404 Malformed Request");
        badRequest = true;
	      badStatus = 400;
      }

      String method = new String("");
      String file = new String("");
      String version = new String("");
      String fileContents = new String("");
      if(!badRequest){
        method = new String(arr[0]);
        file = new String(arr[1]);
        version = new String(arr[2]);
      }

      StringBuilder response = new StringBuilder("");
      // TODO : HTTP Request Handling
      // String reply will contain the HTTP response
      // NOTE : Split the reply by CR LF to examine the request line and headers 
      

      if(!badRequest){
        if(!method.equals("GET")){
	  System.out.println("[HTTP/1.1] METHOD IS NOT GET :: Preparing 501 Not Implemented");
          badRequest = true;
          badStatus = 501;
        }

        System.out.println("[HTTP/1.1] METHOD := "+method);

        if(!(new File(serverDirectory + file).exists() && !(new File(serverDirectory + file).isDirectory()))){
          System.out.println("[HTTP/1.1] USER AGENT REQUESTED FILE THAT CANT BE FOUND :: Preparing 404 Not Found");
	        badRequest = true;
          badStatus = 404;
        } else {
          BufferedReader fileSC = new BufferedReader(new FileReader(serverDirectory + file));
          StringBuilder fileSB = new StringBuilder("");
          String sc;
          while((sc = fileSC.readLine()) != null)
            fileSB.append(sc);
          fileContents = fileSB.toString();
          fileSC.close();
        }

        System.out.println("[HTTP/1.1] FILE REQUESTED := "+file);

        if(!version.equals("HTTP/1.1") && !badRequest){
	      System.out.println("[HTTP/1.1] USER AGENT NOT USING HTTP/1.1 :: Preparing 505 HTTP Version Not Supported");
          badRequest = true;
          badStatus = 505;
        }

        System.out.println("[HTTP/1.1] VERSION := "+version);
      
      
        while(!badRequest && (s = bfReader.readLine()) != null){
          if(s.isEmpty()) break;
          System.out.println("[HTTP/1.1] HEADER RECEIVED := "+s);
          arr = s.split(": ");
          if(arr[0].equals("If-Modified-Since")){
            badRequest = !checkIfModifiedSince(arr[1], new File(serverDirectory + file).lastModified());
	          if(badRequest) { System.out.println("[HTTP/1.1] FILE HAS NOT BEEN MODIFIED YET :: Preparing 304 Not Modified"); }
	        }
          badStatus = badRequest ? 304 : 0;
        }
      }

      System.out.println("[HTTP/1.1] Preparing reply...");

      if(badRequest){
        switch(badStatus){
          case 304: response.append("HTTP/1.1 304 Not Modified\r\n"); break;
          case 400: response.append("HTTP/1.1 400 Bad Request\r\n"); break;
          case 404: response.append("HTTP/1.1 404 Not Found\r\n"); break;
          case 501: response.append("HTTP/1.1 501 Not Implemented\r\n"); break;
          case 505: response.append("HTTP/1.1 505 HTTP Version Not Supported\r\n"); break;
        }
      } else {
        response.append("HTTP/1.1 200 OK\r\n");
      }

      response.append("Date: "+asctime()+"\r\n");
      response.append("Server: SHServer/2.0 (JVM)\r\n");
      if(!badRequest){
        response.append("Content-Type: text/html\r\n"); 
        response.append("Content-Length: "+fileContents.length()+"\r\n");
        response.append("Last-Modified: "+ millisToAsctime(new File(serverDirectory + file).lastModified())+"\r\n");
        response.append("Set-Cookie: 1234\r\n");
      }
      response.append("\r\n");
      if(!badRequest) response.append(fileContents);

      String reply = response.toString();
      System.out.println("[HTTP/1.1] Sending reply through Output Stream...");
      OutputStream out = socket.getOutputStream();
      out.write(reply.getBytes());
      out.flush();
      System.out.println("[HTTP/1.1] Reply sent!");
      log.writeEntry(myName + new Date().toString() + "reply sent\n");
      socket.close();
    } catch (Exception e) {
      try {
        System.out.println("[HTTP/1.1] CLOSING THREAD :: "+ e.getClass().toString()+ " :: " +e.getMessage());
		    socket.close();
	    } catch (IOException ioexc) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
	    }
    }
  }

}
