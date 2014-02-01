package gameframework.transversal.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Pojo entity to serialize and deserialize.
 * 
 * @author Jose Carvajal
 */
public class SessionBean implements Serializable
{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private String token;

    private String gameName;

    private Integer level;

    private String lang;

    private Map<String, String> properties;

    private String winner;

    private Date startedAt;

    private boolean finished = false;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getGameName()
    {
        return gameName;
    }

    public void setGameName(String gameName)
    {
        this.gameName = gameName;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    public Date getStartedAt()
    {
        return startedAt;
    }

    public void setStartedAt(Date startedAt)
    {
        this.startedAt = startedAt;
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

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return String.format(
            "[%s] for %s: Started at %s, Finished %s, Winner %s, level %s, lang %s and properties %s.", this.token,
            this.gameName, this.startedAt, this.finished, this.winner, this.level, this.lang, this.properties);
    }
}
