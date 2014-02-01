package game.webapp.controllers;

import game.webapp.controllers.models.EndGameRequest;
import game.webapp.controllers.models.Response;
import game.webapp.controllers.models.StartGameRequest;
import game.webapp.controllers.models.StartGameResponse;
import gameframework.services.interfaces.ISessionService;
import gameframework.transversal.logging.GameLogger;

import java.util.HashMap;

import org.perf4j.aop.Profiled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController
{
    @Autowired
    protected ISessionService sessionService;

    /**
     * @return Session service instance.
     */
    public ISessionService getSessionService()
    {
        return this.sessionService;
    }

    /**
     * Set the session service instance.
     * 
     * @param sessionService
     */
    public void setSessionService(ISessionService sessionService)
    {
        this.sessionService = sessionService;
    }

    @Profiled
    @ResponseBody
    @RequestMapping(value = "/{game}/startGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StartGameResponse startGame(@PathVariable String game, @RequestBody StartGameRequest request)
    {
        GameLogger.info(String.format("Request for %s/startGame with lang %s and level %s.", game, request.getLang(),
            request.getLevel()));

        StartGameResponse response = new StartGameResponse();

        String token =
            this.sessionService.registerSession(game, request.getLang(), Integer.valueOf(request.getLevel()),
                new HashMap<String, String>());

        response.setToken(token);

        GameLogger.info(String.format("Response for %s/startGame with token %s.", game, response.getToken()));

        return response;
    }

    @Profiled
    @ResponseBody
    @RequestMapping(value = "/{game}/endGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response endGame(@PathVariable String game, @RequestBody EndGameRequest request)
    {
        GameLogger.info(String.format("Request for %s/endGame with token %s.", game, request.getToken()));

        Response response = new Response();

        boolean result = this.sessionService.unregisterSession(game, request.getToken());

        GameLogger.info(String.format("Response for %s/endGame with result %s.", game, result));

        return response;
    }
}
