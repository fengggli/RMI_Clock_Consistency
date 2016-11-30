import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by lifeng on 9/30/16.
 * poject object
 */
public class PO implements  Runnable{
    // each process maintains a id and counter
    private int poId;
    private int logicClock;

    Vector<Integer> listClock = new Vector<>();
    // 1- 2/eventTypeRange will be the ratio of receive

    // message queue
    public BlockingQueue<Integer> mQueue = new LinkedBlockingQueue<Integer>();

    // each event can advance its count with 1 or 2 2
    public double incRate = 0.5;


    // information of timing and how many pos
    //static int timeStep = 1;
    static int numPO;
    // whether use Synchronizing method or not
    static boolean useSyn;
    static int eventTypeRange;
    static int eventNumber;
    static PrintWriter logOut;

    PO[] allPOs;
    // constructor
    public PO(int id){
        poId = id;
        logicClock = 0;
        System.out.println("PO " + id + "is created");

    }

    public void setObjectsArray(PO[] POs){
        allPOs = POs;
    }

    private void threadMessage(String message){
        String threadName = Thread.currentThread().getName();
        String moreMessage = String.format("%s: time changed to %d \t after \t %s \n", threadName, logicClock, message);
        logOut.print(moreMessage);
    }

    // this simulate different processes with have different counter increment
    // how long should we wait between event
    private void increaseClock(){
        int increment = ThreadLocalRandom.current().nextInt(0,100);

        // each time the counter can increase by 1 or 2
        if(increment < 100*incRate){
            logicClock += 1;
        }
        else {
            logicClock += 2;
        }

    }

    private void internalEvent(){
        increaseClock();
        threadMessage("internal event!");
    }

    private void sendEvent(int m, int targetId){
        // some sending

        // advance the counter

        assert targetId!=poId;
        try {
            allPOs[targetId].mQueue.put(logicClock);
        }catch (InterruptedException e){
            System.err.println(Thread.currentThread().getName() + ":interrupted when sending");
        }
        increaseClock();
        threadMessage("send LogicNo = " + m  +" to thread"  + targetId);
    }

    private void receiveEvent() {
        increaseClock();
        // get a message from queue
        int byzantine = ThreadLocalRandom.current().nextInt(0,50);
        if(byzantine < 48) {

            if (!mQueue.isEmpty()) {
                int receivedTimeStep = mQueue.poll();
                // check the timestep

                // two experiment sets
                if (receivedTimeStep >= logicClock && useSyn == true) {
                        logicClock = receivedTimeStep + 1;
                        threadMessage("receive  a message, and adjust current time");
                } else {
                    threadMessage("receive  a message, but its out of date or synchronizing is not used in this setting");
                }

            } else {
                threadMessage("receive  a message, but no message in the queue");


            }
        }
        else if(byzantine == 48){
            threadMessage("byzantine error type 1: not update logic clock");
        }
        else if(byzantine == 49){
            threadMessage("byzantine error type 2: advance logic clock by 100");
            logicClock += 500;
        }
    }

    public void run(){
        threadMessage("thread started");
        //ThreadLocalRandom.current().setSeed(poId);
        int i;
        PrintWriter myout = null;
        String outPath = "results/Counters_useSyn_" + PO.useSyn + "_eventRange_"+ PO.eventTypeRange + "t_"+ poId +".txt";

        try {
            myout = new PrintWriter(new FileWriter(outPath));
        }catch (IOException e) {}

        for(i = 0; i < eventNumber; i ++){

            try {

                Thread.sleep(1);
            }
            catch (InterruptedException e){
                threadMessage("interrupted by user!");
                return;
            }

            int eventType = ThreadLocalRandom.current().nextInt(0,eventTypeRange);

            if(eventType == 0){
                internalEvent();
            }
            else if(eventType == 1){
                int target = -1;
                // do not sent message to itself
                do{
                    target = ThreadLocalRandom.current().nextInt(0, numPO);
                }while (target == poId);

                sendEvent(logicClock, target);
            }
            else{
                receiveEvent();
            }
            myout.println(new Integer(logicClock).toString());
        }
        myout.close();

        // store all the counter series

    }
}


