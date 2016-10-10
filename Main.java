
import java.io.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
	// write your code here
        int i, j;
        int numPO = 4;
        int eventNumber = 10000;


        // key experiment settings
        if(args.length == 2){
            try{
                PO.useSyn = Boolean.parseBoolean(args[0]);
                PO.eventTypeRange = Integer.parseInt(args[1]);
                if(PO.eventTypeRange <3){
                    throw new NumberFormatException();
                }
            }catch (NumberFormatException e){
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.err.println("Command format:java bin/ run.java useSyn eventTypeRange," +
                        " where useSyn can be either 'true' or 'false', eventTypeRange should be a integer no less than 3");
                System.exit(1);
            }

        }
        else{
            PO.useSyn = true;
            PO.eventTypeRange = 6;
        }
        PO.eventNumber = eventNumber;
        PO.numPO = numPO;


        String  threadName;
        //String logName = "results/MQwithBinzantine_R_4over6_1.txt";
        PrintWriter logOut = null;
        String logPath = "logs/useSyn_" + PO.useSyn + "_eventRange_"+ PO.eventTypeRange +".log";

        try {
            logOut = new PrintWriter(new FileWriter(logPath));
        }catch (IOException e) {
            System.out.println("cannot open log file at " + logPath);
        }

        PO.logOut  = logOut;



        Thread myThreads[] = new Thread[numPO] ;
        PO myPOs[] = new PO[numPO];


            for (i = 0; i < numPO; i++) {
                myPOs[i] = new PO(i);
            }
            for(i = 0; i < numPO; i++){
                threadName = "thread" + i;
                myThreads[i] = new Thread(myPOs[i], threadName);

                // also add the object information of other objects
                myPOs[i].setObjectsArray(myPOs);

                myThreads[i].start();
            }


        System.err.println("all " + numPO + " process started, wait for " + PO.eventNumber + "events");
        System.err.println("log will be stored in file "+ logPath);

        for(i = 0; i < numPO; i++){
            myThreads[i].join();
        }
        logOut.close();

        // join all threads
    }
}
