package heckmeck.server;

import java.util.List;

public class GameEndMessage extends ServerMessage {

	// Attributes
	private static final long serialVersionUID = 5798735020017058703L;
	private List<PlayerState> mPlayers;

	// Constructor
	public GameEndMessage(List<PlayerState> players) {
		mType = GAMEEND;
		mPlayers = players;
	}

	public List<PlayerState> getPlayers() {
		return mPlayers;
	}

	@Override
	public String getMessageType() {
		return mType;
	}
}