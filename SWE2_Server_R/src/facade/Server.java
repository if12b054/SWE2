package facade;
/* 
 * Server.java
 * 
 * RITZAL Roman
 * if12b051
 * SWE2
 * */
 import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 
/*
 * Die Server Klasse verarbeitet Netzwerkzugriffe von Clients.
 * Jeder Request wird ein einem eigenen Thread gekapselt
 * Die weitere Verarbeitung des Requests wird vom Handler
 * übernommen
 * 
 * Die Klasse implementiert die start und die warte auf
 * Anmeldung Methode
 * 
 * */

 public class Server {
    public static void main(String[] args) {
    	
	 	Server server = new Server();
	 	server.start(); 
    }
    
    /*
     * Die Start Methode legt den Server am Port
     * 11111 an und wartet bis ein neuer Host angemeldet hat
     * und kapselt den Request in einem eigenen Thread und
     * übergibt die jeweilige Socket NR an den Thread weiter
     * */
    
    void start() {
    	
	 	int port = 11111;
	 	ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server gestartet: " + serverSocket);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	 	for(;;) {
			try {
				Socket client = warteAufAnmeldung(serverSocket);
				System.out.println("Neue Clientverbindung:" + client);
				/*Neuer Request -> neues Handler Objekt*/
				Handler h = new Handler(client);
				/*Handler Objekt wird in einem eigenen Thread gestartet*/
				Thread thread = new Thread(h);
				thread.run();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	 	}
    }
    
    /*
     * Die warteAufAnmeldung Methode blockiert solange bis sich ein Client
     * verbunden hat und gibt die SocketNR zurück
     * */
    
    java.net.Socket warteAufAnmeldung(java.net.ServerSocket serverSocket) throws IOException {
    	/* blockiert, bis sich ein Client angemeldet hat*/
    	java.net.Socket socket = serverSocket.accept(); 
 	return socket;
    }
 }
    
 