package cs551k.examples.simplethreads;

/**
 * @author Martin Kollingbaum
 * University of Aberdeen
 * 2018
 *
 */

public class HelloRunnable implements Runnable {

    public void run()
    {
      System.out.println("Hello from a thread!");
    }

    public static void main(String args[])
    {
        Thread thread1 = new Thread(new HelloRunnable()) ;
        Thread thread2 = new Thread(new HelloRunnable()) ;

        thread1.start() ;
        thread2.start() ;
    }

}