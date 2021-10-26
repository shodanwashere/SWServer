package Client;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Client that sends HTTP Requests to a server located at localhost:6789
 * Usage: java Client.HTTPClient <filename> [<if_modified_since_epoch>]
 *        <filename> = name/path of the file to be requested, with no extensions
 *        <if_modified_since_epoch> [Optional] = value in milliseconds to be introduced into the "If-Modified-Since" header of the request
 *
 * @author nunoDias fc56330
 * @author brunaSantos fc56328
 * @author alexandrePinto fc55958
 */
public class HTTPClient{
	private static final int PORT = 6789;

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

	private static String asctime(){
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

	public static void main(String[] args){
		try{
		int argc = args.length;
		if(argc < 1){
			System.err.println("Error: Not enough args");
			System.exit(0);
		}

		Socket socket = new Socket("localhost", PORT);
		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();
		StringBuilder rqstBuilder = new StringBuilder("");
		
		String file = new String(args[0]);
		rqstBuilder.append("GET /"+file+".html HTTP/1.1\r\n");
		File cookieFile = new File("cookie.txt");
                if(cookieFile.exists() && !cookieFile.isDirectory()){
                        Scanner cookieSC = new Scanner(cookieFile);
                        String cookieContent = new String(cookieSC.nextLine());
                        cookieSC.close();
                        rqstBuilder.append("Cookie: "+cookieContent+"\r\n");
                }
		rqstBuilder.append("Date: "+ asctime()+"\r\n");
		rqstBuilder.append("Host: localhost:6789\r\n");
		if(argc == 2){
                        rqstBuilder.append("If-Modified-Since: " + millisToAsctime(Long.valueOf(args[1])) + "\r\n");
                }
		rqstBuilder.append("User-Agent: SHClient/1.0 (JVM)\r\n");
		rqstBuilder.append("\r\n");
		BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
		out.write(rqstBuilder.toString().getBytes());
		String answer = inReader.readLine();

		System.out.println("[HTTP/1.1] Received answer!!!\n"+answer);
		String[] check = answer.split(" ");
		boolean OK = check[1].equals("200");
		while(!answer.equals("")){
			answer = inReader.readLine();
			System.out.println(":: "+answer);
			String[] answerSplit = answer.split(": ");
			if(answerSplit[0].equals("Set-Cookie")){
				FileOutputStream fosCookie = new FileOutputStream("cookie.txt", false);
				fosCookie.write(answerSplit[1].getBytes());
				fosCookie.close();
			}
		}

		if(OK){
			File rqstedFile = new File(args[0]+".html");
			if(rqstedFile.exists()){
				rqstedFile.delete();
				rqstedFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(rqstedFile.getName(), true);
			while((answer = inReader.readLine())!=null){
				byte[] b = answer.getBytes();
				fos.write(b);
				fos.write((byte) '\n');
			}
			fos.close();
		}
		socket.close();
	} catch (Exception e) {
		// oh fuck
	}
	}
}
