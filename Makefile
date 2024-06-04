.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
JAVAC=/usr/bin/javac
JAVA=/usr/bin/java

# List of Java source files (with .java extension) to be compiled
SOURCES=$(wildcard $(SRCDIR)/*.java)

# List of corresponding class files in the bin directory
CLASSES=$(SOURCES:$(SRCDIR)/%.java=$(BINDIR)/%.class)

# Default target: build all the class files
default: $(CLASSES)

# Rule to compile Java source files into class files
$(BINDIR)/%.class: $(SRCDIR)/%.java | $(BINDIR)
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR):$(SRCDIR) $<

# Create the bin directory if it does not exist
$(BINDIR):
	mkdir -p $(BINDIR)

# Target to run the ClubSimulation class
run: $(CLASSES)
	$(JAVA) -cp $(BINDIR):$(SRCDIR) SchedulingSimulation 800 0

# Target to clean compiled class files
clean:
	rm -rf $(BINDIR)

.PHONY: default run clean