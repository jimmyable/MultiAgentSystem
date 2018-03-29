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
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{

  public Client() {}

  public static void main(String[] args) throws NumberFormatException, 
                                                UnknownHostException, 
                                                IOException 
  {
      Socket server = new Socket( args[0],                     // host
                                    Integer.parseInt(args[1]) ); // port

      BufferedReader in = new BufferedReader(new InputStreamReader( server.getInputStream() ));    
      PrintWriter out = new PrintWriter(new OutputStreamWriter( server.getOutputStream() ), true);

      // get something from the keyboard (Input stream on System.in)
      BufferedReader stdin = new BufferedReader(
                                 new InputStreamReader( System.in ));
      
      // start conversation with server
      System.out.println( in.readLine() ); // read from server
      
      System.out.print("Client> "); String fromConsole = stdin.readLine() ;
      // send your console input to server
      out.println( fromConsole );
      //wait for server to return something and print it out
      System.out.println( in.readLine() ); // read from server


  }

}