package game.webapp.controllers.models;

public class GetStringResponse {
	private String string;

	public GetStringResponse() {

	}

	public GetStringResponse(String string) {
		setString(string);
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
