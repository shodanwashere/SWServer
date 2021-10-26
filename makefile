#
# A simple makefile for compiling all the parts of this miniproject
# Authored by: Nuno Dias, fc56330
#

# define a makefile variable for the java compiler
#
JCC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#
JFLAGS = -g

# typing 'make' will invoke the first target entry in the makefile
# (the default one in this case)
#
default: HTTPServer.class ClientHandler.class Logger.class HTTPClient.class

HTTPServer.class: Server/HTTPServer.java
	$(JCC) $(JFLAGS) Server/HTTPServer.java

ClientHandler.class: Server/ClientHandler.java
	$(JCC) $(JFLAGS) Server/ClientHandler.java

Logger.class: Server/Logger.java
	$(JCC) $(JFLAGS) Server/Logger.java

HTTPClient.class: Client/HTTPClient.java
	$(JCC) $(JFLAGS) Client/HTTPClient.java

clean:
	$(RM) Server/*.class
	$(RM) Client/*.class
