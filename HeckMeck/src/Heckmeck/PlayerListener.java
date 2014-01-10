package Heckmeck;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerListener implements Runnable {

	private Socket mClientSocket;
	private ObjectOutputStream mOos;
	private ObjectInputStream mOis;
	
	public PlayerListener( Socket socket ) throws IOException{
		mClientSocket = socket; 
		mOos = new ObjectOutputStream( socket.getOutputStream());
		mOis = new ObjectInputStream(mClientSocket.getInputStream( ));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		waitForMessages();
	}
	private void waitForMessages(){
		try {
			do {				
			ClientMessage message = (ClientMessage)mOis.readObject();
			switch (message.getMessageType()) {
			case ClientMessage.LOGON:
				logon( message );
				break;
			case ClientMessage.MOVE:
				move( message );
				break;

			default:
				break;
			}
			
			} while (true);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void move(ClientMessage message) {
		
		
	}

	private void logon(ClientMessage message) {
		LogonMessage logonMessage = (LogonMessage)message;
		
		System.out.println("New Player: " + logonMessage.getName());
		
	}
}
