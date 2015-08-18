package gameframework.transversal.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Pojo entity to serialize and deserialize.
 * 
 * @author Jose Carvajal
 */
public class SessionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5436699561824398464L;

	private String token;

	private String gameName;

	private Integer level;

	private String lang;

	private Map<String, String> properties;

	private String winner;

	private Date startedAt;

	private boolean finished = false;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode = 31 * hashCode + getGameName().hashCode();
		hashCode = 31 * hashCode + getToken().hashCode();

		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {

		boolean equals = false;
		if (obj instanceof SessionBean) {
			SessionBean session = (SessionBean) obj;
			equals = this.gameName.equals(session.getGameName())
					&& this.token.equals(session.getToken());
		}

		return equals;
	}

	@Override
	public String toString() {
		return String
				.format("[%s] for %s: Started at %s, Finished %s, Winner %s, level %s, lang %s and properties %s.",
						this.token, this.gameName, this.startedAt,
						this.finished, this.winner, this.level, this.lang,
						this.properties);
	}
}
