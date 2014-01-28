package heckmeck.server;

import heckmeck.exceptions.WrongPlayerCount;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.lang.Thread;
import java.net.Socket;

public class Server {

	// Attributes
	private ServerSocket mServerSocket;
	private Thread mThread;
	private ClientManagement mClientManagement;
	private Game mGame;

	// Constructor
	private Server(int playerCount) {
		mClientManagement = new ClientManagement(playerCount);
		try {
			mServerSocket = new ServerSocket(23534);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			checkPlayerCount(playerCount);
		} catch (WrongPlayerCount e1) {
			return;
		}
		
		startThreads(playerCount);

		mClientManagement.sendMessage(new WelcomeMessage("Hallo"));

		mGame = new Game(mClientManagement.getPlayerNames());
	}
	
	
	public static void log(String message) {
		System.out.println(message);
	}
	
	public void move(Decision decision) {
		
		GameStateMessage gameStateMessage = new GameStateMessage(mGame.move(decision));
		mClientManagement.sendMessage(gameStateMessage);
	}
	
	public void checkPlayerCount(int playerCount) throws WrongPlayerCount {
		if (playerCount == 0) {
			log("Mindestens zwei Spieler ben�tigt!");
			throw new WrongPlayerCount();
		}
		if (playerCount == 1) {
			log("Mindestens zwei Spieler ben�tigt!");
			throw new WrongPlayerCount();
		}

		if (playerCount > 7) {
			log("Maximal sieben Spieler erlaubt!");
			throw new WrongPlayerCount();
		}
	}
	
	public void startThreads(int playerCount) {
		log("Starte Server f�r " + playerCount + " Spieler");
		for (int i = 0; i < playerCount; i++) {
			Socket socket;
			try {
				socket = mServerSocket.accept();
				ClientConnection clientConnection = new ClientConnection(socket, this);

				mClientManagement.addClient(clientConnection);
				new Thread(clientConnection).start();
				log("Thread mit ClientConnection erstellt und gestartet");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void main(String[] args) {

		int playerCount = 2;

		if (args.length > 0) {
			try {
				playerCount = new Integer(args[0]);
			} catch (NumberFormatException nfe) {
			}
			;
		}

		new Server(playerCount);
	}
}