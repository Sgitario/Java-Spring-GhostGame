package game.webapp.controllers.models;

import java.io.Serializable;

public class AddLetterRequest implements Serializable {

	private static final long serialVersionUID = 3196814320016906325L;

	private String token;

	private String letter;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}
}
