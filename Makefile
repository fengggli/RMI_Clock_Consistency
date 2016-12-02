JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        PO.java \
        Main.java \
		PeerInterface.java
		

default: classes
	mkdir -pv results/summary
	mkdir -p logs

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) -rf results/*
	$(RM) logs/*
