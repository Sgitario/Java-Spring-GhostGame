package game.webapp.controllers.models;

public class Response
{
    private Integer errorKey;

    private String error;

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public Integer getErrorKey()
    {
        return errorKey;
    }

    public void setErrorKey(Integer errorKey)
    {
        this.errorKey = errorKey;
    }
}
