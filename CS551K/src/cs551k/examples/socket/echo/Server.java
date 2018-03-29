package cs551k.examples.socket.echo;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {}

    public static void main(String[] args) throws NumberFormatException, IOException {

    ServerSocket listener = new ServerSocket( Integer.parseInt(args[0]) );

    while (true) 
    {
      // wait for client connects
      Socket client = listener.accept();
      
      // if a client connects, create the in and out streams
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream() ));
      PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream() ), true);
            
      // start a conversation
      out.println("Hello client");

      String msg = in.readLine();
            
      // do something with the message
               
      out.println( msg );
            
      // close the client connection
      client.close();
    }

  }

}