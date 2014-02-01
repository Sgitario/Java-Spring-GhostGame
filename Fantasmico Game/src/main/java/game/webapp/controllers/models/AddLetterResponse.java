package game.webapp.controllers.models;

/**
 * @version $Id$
 */
public class AddLetterResponse extends Response
{
    private String letter;

    private boolean finished;

    private String winner;

    public String getLetter()
    {
        return letter;
    }

    public void setLetter(String letter)
    {
        this.letter = letter;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public String getWinner()
    {
        return winner;
    }

    public void setWinner(String winner)
    {
        this.winner = winner;
    }
}
