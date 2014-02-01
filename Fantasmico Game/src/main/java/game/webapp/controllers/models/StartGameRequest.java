package game.webapp.controllers.models;

/**
 * @version $Id$
 */
public class StartGameRequest
{
    private String lang = "eng";

    private String level = "1";

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

}
