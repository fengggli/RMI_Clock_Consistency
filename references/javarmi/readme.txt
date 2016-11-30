Steps to run the JavaRMI Program:
1.Extract the contents of NairA4Example.zip onto a location using Winrar or 7zip. 
2.Go into the extracted folder using cd NairA4Example command on pegasus.
3.Go into javarmi directory
4. Giving permissions:
4.1 Type chmod +x *.sh to give permission to all makefiles.
4.2 Type chmod +x *.java to give permission to all .java files.
4.3 Type chmod +x *.class to give permission to all .class files.
4.4 Type chmod +x policy to give permission to policy file.
5. Change the CodeBase in the policy file to the appropriate path in your directory. The current path is 
/home/arvnair/javarmi/
6. Type ./compile.sh to compile all the java files.
7. Log in to machine in-csci-rrpc01.cs.iupui.edu and go into folder javarmi directory and type ./rmiregistry.sh to run the registry on machine this machine.
8. Do step 7 for machine 02. The rmiregistry will be running on all those machines.
9. Open another instance of machines 01 and 02 go into javarmi folder using cd as mentioned in steps above.
10. On machine 01, type ./peer1.sh to run peer1 on machine 01.
11. On machine 02, type ./peer2.sh to run peer2 on machine 02.

For interchanging machines and different port numbers:
1. If you want to run the peers on a different hosts, make that change in peer1.java, peer2.java.
2. The corresponding line in files will be:
String name ="//in-csci-rrpc01.cs.iupui.edu:6754/peer1"
3. If you wish to run on a different port eg:on 2222  and machine 02 then change 
String name ="//in-csci-rrpc01.cs.iupui.edu:2222/peer1"
4. You need to make changes in the peer2.java file as it accesses peer1.
5. Also in the rmiregistry.sh file change the second line
rmiregistry 2222 from the corresponding line rmiregistry 6754
6. I have configured the registry to run on 6754 port and the peers to run on machines 01 and 02.

Note:
1.Always ensure that the registry is running on all servers and then start the peers.Each of the peers require a registry on their machine running as all of them act as client as well as server in my design.
2.Type y for all questions asked on console.
3.To connect to each other ensure that all are up and running.eg: when peer1 asks to connect to peer2 ensure that peer2 is running and asking to connect to peer1.So type y in peer1 and then y in peer2.
4. Run the registry and the object on different instances of the machine.(eg: run registry on machine 01 and open another window of machine 01 and run the peer there.
5. The rmiregistry.sh file has the command javac *.java for compiling all the java files.
6. You can also try as localhost on each machine and on pegasus.
7. The code gives subtraction result.