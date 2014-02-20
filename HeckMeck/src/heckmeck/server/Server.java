package heckmeck.server;

import heckmeck.exceptions.WrongPlayerCountException;

import java.io.*;
import java.net.*;
import java.lang.Thread;

public class Server {

	// Attributes
	private ServerSocket mServerSocket;
	private ClientManagement mClientManagement;
	private Game mGame;
	private int mPlayerCount;
	private static final int MINPLAYER = 2;
	private static final int MAXPLAYER = 7;
	private Logger mLog;
	private String mServerIP;

	// Constructor
	public Server(int playerCount, Logger logger) {
		mPlayerCount = playerCount;
		mLog = logger;
		mServerIP = getServerIP();
		mClientManagement = new ClientManagement(mPlayerCount);
	}

	private String getServerIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * Main method - gets player count and starts server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		int playerCount = 2;

		if (args.length > 0) {
			try {
				playerCount = new Integer(args[0]);
			} catch (NumberFormatException nfe) {
			}
			;
		}

		Server server = new Server(playerCount, new SysoLog());
		server.start(playerCount);
	}

	/**
	 * initializes gameStateMessage and sends message
	 * 
	 * @param decision
	 */
	public void move(DecisionMessage decision) {
		GameStateMessage gameStateMessage = new GameStateMessage(
				mGame.move(decision));

		mClientManagement.sendMessage(gameStateMessage);
	}

	/**
	 * checks player count
	 * 
	 * @param playerCount
	 * 
	 * @throws WrongPlayerCountException
	 */
	public void checkPlayerCount(int playerCount)
			throws WrongPlayerCountException {
		if (playerCount < MINPLAYER) {
			mLog.log("Mindestens zwei Spieler ben�tigt!");
			throw new WrongPlayerCountException();
		}

		if (playerCount > MAXPLAYER) {
			mLog.log("Maximal sieben Spieler erlaubt!");
			throw new WrongPlayerCountException();
		}
	}

	/**
	 * waits for players and starts threads
	 * 
	 * @param playerCount
	 */
	public void start(int playerCount) {
		try {
			mServerSocket = new ServerSocket(23534);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			checkPlayerCount(playerCount);
		} catch (WrongPlayerCountException e1) {
			return;
		}

		mLog.log("Start server for " + playerCount + " players");
		waitForNewClients();

	}

	/**
	 * waits for new clients to start game
	 */
	private void waitForNewClients() {
		while (true) {

			Socket socket;
			try {
				socket = mServerSocket.accept();

				if (mClientManagement.isPlayerCountReached()) {
					sendFullMessage(socket);
				} else {
					addClient(socket);
				}

				startGameIfAllClientsConnected();
			} catch (IOException e) {
				mLog.log(e);
			}

		}
	}

	/**
	 * sends initial GameStateMessage
	 */
	private void sendInitialGameStateMessage() {
		GameStateMessage gameStateMessage = new GameStateMessage(
				mGame.getGameState());
		mClientManagement.sendMessage(gameStateMessage);
	}

	/**
	 * adds Client Connection to Client Management
	 * 
	 * @param socket
	 * @throws IOException
	 */
	private void addClient(Socket socket) throws IOException {
		ClientConnection clientConnection = new ClientConnection(socket, this);

		mClientManagement.addClient(clientConnection);
		new Thread(clientConnection).start();
	}

	/**
	 * sends Message when server is full
	 * 
	 * @param socket
	 */
	private void sendFullMessage(Socket socket) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					socket.getOutputStream());
			oos.writeObject(new FullMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if all the clients are conntected and starts the game if they are
	 */
	public void startGameIfAllClientsConnected() {

		if (mClientManagement.isPlayerCountReached()) {
			mLog.log("All players connected. Start game!");
			mGame = new Game(mClientManagement.getPlayerNames(), mClientManagement);
			sendInitialGameStateMessage();
		}

	}

	/**
	 * shuts server down
	 */
	public void shutdown() {
		try {
			mServerSocket.close();
			mClientManagement.shutdown();
			mLog.log("Server beendet.");
		} catch (IOException e) {
			mLog.log(e);
		}
	}

}