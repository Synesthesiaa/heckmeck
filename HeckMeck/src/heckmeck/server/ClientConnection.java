package heckmeck.server;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable {

	// Attributes
	private Socket mClientSocket;
	private ObjectOutputStream mOos;
	private ObjectInputStream mOis;
	private Server mServer;
	private String mName;
	private SysoLog mLog;

	// Constructor
	public ClientConnection(Socket socket, Server server) throws IOException {
		mClientSocket = socket;
		mServer = server;
		mOos = new ObjectOutputStream(socket.getOutputStream());
		mOis = new ObjectInputStream(mClientSocket.getInputStream());
		mLog = new SysoLog();
	}

	@Override
	public void run() {
		waitForMessages();
	}

	/**
	 * sends Message
	 * 
	 * @param message
	 */
	public void sendMessage(ServerMessage message) {
		try {
			mOos.writeObject(message);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets name of player
	 * 
	 * @return
	 */
	public String getName() {
		return mName;
	}

	/**
	 * waits for messages
	 */
	private void waitForMessages() {
		try {
			do {
				ClientMessage message = (ClientMessage) mOis.readObject();

				switch (message.getMessageType()) {
				case ClientMessage.LOGON:
					mLog.log("Message Typ 'LOGON' empfangen");
					logon(message);
					break;

				case ClientMessage.MOVE:
					mServer.move((DecisionMessage) message);
					break;

				default:
					break;
				}

			} while (true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates logonMessage and logs player
	 * 
	 * @param message
	 */
	private void logon(ClientMessage message) {
		LogonMessage logonMessage = (LogonMessage) message;
		mLog.log("Logon Message erstellt");

		mName = logonMessage.getName();
		mLog.log("New Player: " + mName);

	}

	/**
	 * shuts Client connection down
	 */
	public void shutdown() {
		try {
			mClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return mName;
	}

}
