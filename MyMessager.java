import java.rmi.RemoteException;

/**
 * Created by lifeng on 11/11/16.
 */
public interface MyMessager extends  java.rmi.Remote{
    // server will get notified to start counter
    int getMyId() throws RemoteException;
    void startEvents() throws RemoteException;

    // add a timestamp message from client to server message queue
    void pushMessage() throws RemoteException;
}
