import java.io.*;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lifeng on 9/30/16.
 * poject object
 */
public class PO extends UnicastRemoteObject implements PeerInterface {
    // each process maintains a id and counter
    private int poId;
    private int logicClock;

    Random randomGen;


    // message queue
    public BlockingQueue<Integer> mQueue = new LinkedBlockingQueue<Integer>();

    // each event can advance its count with 1 or 2 2
    public double incRate = 0.5;


    // information of timing and how many pos
    //static int timeStep = 1;

    // false
    static  int numPO=0;
    // whether use Synchronizing method or not
    static boolean useSyn;
    static int eventTypeRange;
    static int eventNumber;
    PrintWriter logOut;


    Map<Integer, PeerInterface>  allPeers =  new HashMap<Integer, PeerInterface>();
    // constructor
    public PO(int id) throws RemoteException{
        poId = id;
        randomGen = new Random();

        logicClock = 0;
        System.out.println("PO " + poId + " of "+ numPO +" is created");
    }


    private void NodeMessage(String message){
        /*
        String nodeName="null";
        try {
            nodeName = InetAddress.getLocalHost().getHostName();
        }catch (Exception e){
            System.out.println("error when getting hostname");
        }
        */

        String moreMessage = String.format("%s: time changed to %d \t after \t %s \n", poId, logicClock, message);
        logOut.print(moreMessage);
        //System.out.print(moreMessage);
    }

    // this simulate different processes with have different counter increment
    // how long should we wait between event
    private void increaseClock(){


        // for debug only
        //logicClock += 1;


        // each time the counter can increase by 1 or 2
        int increment = randomGen.nextInt(100);
        if(increment < 100*incRate){
            logicClock += 1;
        }
        else {
            logicClock += 2;
        }

    }

    /*
     * this is a remote call, how should local PO respose when another PO sends its timestep
     * this timestep should be add into queue
     */
    public int updateTime(int timestamp){
        // this should update the incoming message queue in the receiver
        try {
            mQueue.put(timestamp);
        }catch (InterruptedException e){
            NodeMessage("interrupted when add incoming message into");
            return -1;
        }
        //NodeMessage("A timestep of " + timestamp + " is added into the MS queue ");

        return  1;
    }

    private void internalEvent(){
        increaseClock();
        NodeMessage("internal event!");
    }

    private void sendEvent(int m, int targetId){
        // some sending

        // advance the counter

        //assert targetId!=poId;
        //System.out.println("try to send to " + targetId);
        try {

            //allPOs[targetId].mQueue.put(logicClock);
            // instead we make a remote call
            allPeers.get(targetId).updateTime(logicClock);


        }catch (RemoteException e){
            NodeMessage("error when send logic lock to PO." + targetId);
        }
        increaseClock();
        NodeMessage("send LogicNo = " + m  +" to PO."  + targetId);
    }

    private void receiveEvent() {
        increaseClock();
        // get a message from queue
        int byzantine = randomGen.nextInt(50);
        if(byzantine < 48) {

            if (!mQueue.isEmpty()) {
                int receivedTimeStep = mQueue.poll();
                // check the timestep

                // two experiment sets
                if (receivedTimeStep >= logicClock && useSyn == true) {
                        logicClock = receivedTimeStep + 1;
                        NodeMessage("receive  a timestep"+ receivedTimeStep+ "and adjust current time-------");
                } else {
                    NodeMessage("receive timestep" + receivedTimeStep +", but its out of date or synchronizing is not used in this setting-------");
                }

            } else {
                NodeMessage("receive  a message, but no message in the queue");
            }
        }
        else if(byzantine == 48){
            NodeMessage("byzantine error type 1: not update logic clock");
        }
        else if(byzantine == 49){
            logicClock += 500;
            NodeMessage("byzantine error type 2: advance logic clock by 500");
        }
    }



    public void startPO() throws IOException{
        NodeMessage("PO started");
        //ThreadLocalRandom.current().setSeed(poId);
        int i;
        String outPath = "results/Counters_useSyn_" + PO.useSyn + "_eventRange_"+ PO.eventTypeRange + "t_"+ poId +".txt";

        PrintWriter myOut = new PrintWriter(new FileWriter(outPath));


        /* prepare all the registries
         *
         */
        String    stubName = "//in-csci-rrpc0"+ (poId+1) +".cs.iupui.edu:1993/PO" + poId;

        PeerInterface pint = this;


        try {
            Naming.rebind(stubName, pint);
        }catch (Exception e){
            System.out.print(e);
        }

        System.out.println("stub " + stubName + "is construct");


        /*
         * also bind other POs
         */

        String peerName;

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ready to connect to all the peers?(y/n)");
        String ans=br.readLine();
        String yes="y";

        if(ans.equalsIgnoreCase(yes)) {
            for (i = 0; i < numPO; i++) {
                if (i != poId) {
                    peerName = "//in-csci-rrpc0"+ (poId+1) +".cs.iupui.edu:1993/PO" + poId;

                    try {

                        PeerInterface pi = (PeerInterface) Naming.lookup(peerName);
                        if(pi== null){
                            System.out.println("errrrrr");
                        }
                        allPeers.put(i, pi);
                    } catch (Exception e) {
                        System.out.println("error when look up " + i + "th peer with entry:" + peerName);
                        e.printStackTrace();
                        System.exit(-1);
                    }

                    NodeMessage("PO No." + i + " is connected");

                }
            }
        }

        // create a random generattor
        System.out.println("Now start"+ eventNumber+" events, please wait!");

        // show the progress
        int percent_count = eventNumber/10;

        // realsystem time
        long realTime;

        for(i = 0; i < eventNumber; i ++){
            if(i%percent_count ==0){
                System.out.println("%" + (i/percent_count)*10 + " finished");
            }

            try {

                Thread.sleep(1);
            }
            catch (InterruptedException e){
                NodeMessage("interrupted by user!");
                return;
            }

            int eventType = randomGen.nextInt(eventTypeRange);

            if(eventType == 0){
                internalEvent();
            }
            else if(eventType == 1){
                int target = -1;
                // do not sent message to itself
                do{
                    target = randomGen.nextInt(2);
                }while (target == poId);

                sendEvent(logicClock, target);
            }
            else{
                receiveEvent();
            }

            realTime=System.currentTimeMillis();
            //myOut.println(realTime + "\t"+new Integer(logicClock).toString());
            myOut.println(new Integer(logicClock).toString());
        }
        myOut.close();
        // store all the counter series
    }
}


