import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by lifeng on 12/2/16.
 */
public interface PeerInterface extends Remote {


    int updateTime(int timestamp) throws RemoteException;
}
