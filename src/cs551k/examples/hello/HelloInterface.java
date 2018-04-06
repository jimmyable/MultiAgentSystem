package cs551k.examples.hello;		//the first line defines the package where this interface belongs to

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloInterface extends Remote		//this interface extends java.rmi.Remote, an interface provided by the RMI package of Java
{
    public String receiveMessage ( String s ) throws RemoteException;		    //the interface specifies / makes public the method receiveMessage
    																			//that can be called by another program; 
    																			//it throws a java.rmi.RemoteException in case of error


}