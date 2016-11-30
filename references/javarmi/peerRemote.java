import java.rmi.*;  
import java.rmi.server.*;  
public class peerRemote extends UnicastRemoteObject implements peerInterface{  
peerRemote()throws RemoteException{  
super();  
}  
public int subtract(int x,int y){return x-y;}  
}  