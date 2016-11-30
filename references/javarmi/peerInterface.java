import java.rmi.*;  
public interface peerInterface extends Remote{  
public int subtract(int x,int y)throws RemoteException;  
}