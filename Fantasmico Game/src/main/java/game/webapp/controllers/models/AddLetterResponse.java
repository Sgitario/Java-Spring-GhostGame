package game.webapp.controllers.models;

public class AddLetterResponse {
	private String letter;

	private boolean finished;

	private String winner;

	public AddLetterResponse() {

	}

	public AddLetterResponse(String answerLetter, boolean isFinished,
			String winner) {
		setLetter(answerLetter);
		setFinished(isFinished);
		setWinner(winner);
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
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
}
