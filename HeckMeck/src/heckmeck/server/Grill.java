package heckmeck.server;

import heckmeck.exceptions.NoTokenFoundException;

import java.io.Serializable;
import java.util.*;

public class Grill implements Serializable{

	// Attributes
	private static final long serialVersionUID = 5888357567911676482L;
	private List<Token> mTokens;

	// Constructor
	public Grill() {
		init();
	}

	/**
	 * initializes list with 16 tokens
	 */
	private void init() {
		mTokens = new ArrayList<Token>();
		for (int i = 21; i < 37; i++) {
			this.mTokens.add(new Token(i));
		}
	}

	/**
	 * removes token from grill
	 * 
	 * @param tokenNr
	 * @return 
	 * @throws NoTokenFoundException
	 */
	public Token remove(int tokenNr) throws NoTokenFoundException {
		for (Iterator<Token> iterator = mTokens.iterator(); iterator.hasNext();) {
			Token token = iterator.next();

			if (tokenNr == token.getValue()) {
				mTokens.remove(token);
				return token;
			}
		}

		throw new NoTokenFoundException();
	}

	/**
	 * adds token to grill after misthrow
	 * 
	 * @param token
	 */
	public void failure(Token token) {
		if (token != null) {
			mTokens.add(token);
		}
	}

	public List<Token> getTokens() {
		return mTokens;
	}

	public void setTokens(List<Token> mTokens) {
		this.mTokens = mTokens;
	}

	public void deactivateHighestToken() {
		for (int i = 0; i < mTokens.size( ); i++) {
			if (!mTokens.get(i).isActive()){
				mTokens.get(i - 1).deactivate();
				return;
			}			
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sB = new StringBuilder();
		sB.append("GRILL \n");
		
		for (Iterator<Token> iterator = mTokens.iterator(); iterator.hasNext();) {
			Token token = iterator.next();
			if (token.isActive()) {
				sB.append(token.toString());
			}
		}
		sB.append("\n********************\n");
		return sB.toString();
	}

	

}
