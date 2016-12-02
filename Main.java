
import java.io.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
	// write your code here
        int numPO=0;
        int poId=-1;
        int eventNumber = 10;


        // key experiment settings
        if(args.length <2){
            System.out.println("you should at least give current PO id and the total PO numbers");
            System.exit(-1);

        }
        else {
            numPO = Integer.parseInt(args[0]);
            poId = Integer.parseInt(args[1]);
            if (args.length == 4) {
                try {
                    PO.useSyn = Boolean.parseBoolean(args[2]);
                    PO.eventTypeRange = Integer.parseInt(args[3]);
                    if (PO.eventTypeRange < 3) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Argument" + args[0] + " must be an integer.");
                    System.err.println("Command format:java bin/ run.java useSyn eventTypeRange," +
                            " where useSyn can be either 'true' or 'false', eventTypeRange should be a integer no less than 3");
                    System.exit(1);
                }

            } else {
                PO.useSyn = true;
                PO.eventTypeRange = 6;
            }
        }
        PO.eventNumber = eventNumber;


        PrintWriter logOut = null;
        String logPath = "logs/useSyn_" + PO.useSyn + "_eventRange_"+ PO.eventTypeRange +"_No."+ poId+".log";

        try {
            logOut = new PrintWriter(new FileWriter(logPath));
        }catch (IOException e) {
            System.out.println("cannot open log file at " + logPath);
        }


        PO.numPO = numPO;

        // the constructor will assign a id
        PO po = new PO(poId);
        po.logOut = logOut;

        // start this PO
        po.startPO();


        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ready to quit?(y/n)");
        String ans=br.readLine();
        String yes="y";

        if(ans.equalsIgnoreCase(yes)) {

            logOut.close();
            System.exit(0);
        }

        // join all threads
    }
}
