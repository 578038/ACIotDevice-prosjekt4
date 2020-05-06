package no.hvl.dat110.aciotdevice.client;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


import com.google.gson.Gson;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";
	
	private static final String host = "localhost";
	private static final int port = 8080;
	

	public void doPostAccessEntry(String message) {

		
		try(Socket s = new Socket(host, port)){
			
			Gson gson = new Gson();
			String jsonBody = gson.toJson(new AccessMessage(message));
			
			String httpputrequest = "POST " + logpath + " HTTP/1.1\r\n" + "Host: " + host + "\r\n" + "Content-type: application/json\r\n" +
			"Content-length: " + jsonBody.length() + "\r\n" +
			"Connection: close\r\n" + "\r\n" + jsonBody + "\r\n";
			
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httpputrequest);
			pw.flush();
			
			InputStream in = s.getInputStream();
			
			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;
			
			while(scan.hasNext()) {
				
				String nextline = scan.nextLine();
				
				if(header == true) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}
				
				if(nextline.isEmpty()) {
					header = false;
				}
			}
			
			System.out.println("BODY:");
			System.out.println(jsonresponse.toString());
			
			scan.close();
			
		}catch (IOException ex) {
			System.err.println(ex);
			
		}
		
		// TODO: implement a HTTP POST on the service to post the message
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		try(Socket s = new Socket(host, port)){
			
			
			
			String httpgetrequest = "GET " + codepath + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";
			
			OutputStream output = s.getOutputStream();
			
			PrintWriter pw = new PrintWriter(output, false);
			pw.print(httpgetrequest);
			pw.flush();
			
			InputStream in = s.getInputStream();
			
			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;
			
			while(scan.hasNext()) {
				
				String nextline = scan.nextLine();
				
				if(header == false) {
					jsonresponse.append(nextline);
				}
				if(nextline.isEmpty()) {
					header = false;
				}
			}
				Gson gson = new Gson();
				
				code = gson.fromJson(jsonresponse.toString(), AccessCode.class);
			
		
			
			scan.close();
			
		}catch (IOException ex) {
			ex.printStackTrace();
			
		}
		
		
		
		
		// TODO: implement a HTTP GET on the service to get current access code
		
		return code;
	}
}
