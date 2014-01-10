package Heckmeck;

import java.io.Serializable;

public class LogonMessage extends ClientMessage implements Serializable {

	//Attributes
	private static final long serialVersionUID = -2490711427776617424L;
	private String mName;
	
	
	//Constructor
	public LogonMessage(String name) {
		this.mName = name;
	}

	/**
	 * gets name of player
	 * @return
	 */
	public String getName() {
		return mName;
	}

	@Override
	public String getMessageType() {
		// TODO Auto-generated method stub
		return LOGON;
	}
	
		
}
