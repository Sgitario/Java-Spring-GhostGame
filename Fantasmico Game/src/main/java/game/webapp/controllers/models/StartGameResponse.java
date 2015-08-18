package game.webapp.controllers.models;

public class StartGameResponse {
	private String token;

	public StartGameResponse() {

	}

	public StartGameResponse(String token) {
		setToken(token);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
