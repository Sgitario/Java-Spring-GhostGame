package game.webapp.controllers.models;

public class StartGameResponse extends Response
{
    private String token;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
