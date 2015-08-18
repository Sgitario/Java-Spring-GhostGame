package game.webapp.controllers.models;

public class StartGameRequest {
	private String lang = "eng";

	private int level = 1;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
