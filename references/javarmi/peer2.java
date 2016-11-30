import java.rmi.*;  
import java.rmi.registry.*;
import java.io.*;
public class peer2{  
public static void main(String args[]){  
try{  
peerInterface stub=new peerRemote();  
Naming.rebind("//in-csci-rrpc02.cs.iupui.edu:6754/peer2",stub); 

BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
System.out.println("Ready to connect to peer1?(y/n)");
String ans=br.readLine();
String yes="y";
if(ans.equalsIgnoreCase(yes)){
	peerInterface stub1=(peerInterface)Naming.lookup("//in-csci-rrpc01.cs.iupui.edu:6754/peer1");  
	System.out.println(stub1.subtract(200,100));	
} 
 
}catch(Exception e){System.out.println(e);}  
}  
}  