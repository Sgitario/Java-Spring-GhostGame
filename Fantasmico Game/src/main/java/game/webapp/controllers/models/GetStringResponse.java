package game.webapp.controllers.models;

/**
 * @version $Id$
 */
public class GetStringResponse extends Response
{
    private String string;

    public String getString()
    {
        return string;
    }

    public void setString(String string)
    {
        this.string = string;
    }
}
