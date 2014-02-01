package game.webapp.controllers;

import game.webapp.controllers.errors.ErrorKeys;
import game.webapp.controllers.models.AddLetterRequest;
import game.webapp.controllers.models.AddLetterResponse;
import game.webapp.controllers.models.GetStringRequest;
import game.webapp.controllers.models.GetStringResponse;
import gameframework.services.interfaces.IGhostService;
import gameframework.transversal.logging.GameLogger;
import gameframework.transversal.models.SessionBean;

import org.perf4j.aop.Profiled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GhostController extends BaseController
{
    private final static String GHOST_NAME = "ghost";

    @Autowired
    private IGhostService ghostService;

    /**
     * @return Ghost service instance.
     */
    public IGhostService getGhostService()
    {
        return this.ghostService;
    }

    /**
     * Set ghost service instance.
     * 
     * @param ghostService
     */
    public void setGhostService(IGhostService ghostService)
    {
        this.ghostService = ghostService;
    }

    @RequestMapping("/")
    public ModelAndView index()
    {
        ModelAndView modelView = new ModelAndView("ghost");
        modelView.addObject("message", "Spring MVC Demo");

        return modelView;
    }

    @Profiled
    @ResponseBody
    @RequestMapping(value = "/ghost/addLetter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AddLetterResponse addLetter(@RequestBody AddLetterRequest request)
    {
        GameLogger.info(String.format("Request for ghost/addLetter with token %s and letter %s.", request.getToken(),
            request.getLetter()));

        AddLetterResponse response = new AddLetterResponse();

        SessionBean session = this.sessionService.getSession(GHOST_NAME, request.getToken());
        if (session != null) {
            if (this.ghostService.checkLetter(request.getLetter(), session)) {
                try {
                    String letter = this.ghostService.addLetter(request.getLetter(), session);

                    response.setLetter(letter);
                    response.setFinished(session.isFinished());
                    if (!session.isFinished()) {
                        this.sessionService.updateSession(session);
                    } else {
                        response.setWinner(session.getWinner());

                        this.sessionService.unregisterSession(GHOST_NAME, request.getToken());
                    }
                } catch (IllegalArgumentException ex) {
                    response.setErrorKey(ErrorKeys.ERROR_LANGUAGE_NOT_FOUND);
                    response.setError("Language not found");
                } catch (Exception ex) {
                    response.setErrorKey(ErrorKeys.ERROR_SERVER);
                    response.setError("Server unavailable");
                    GameLogger.error("Unexpected error", ex);
                }
            } else {
                response.setErrorKey(ErrorKeys.ERROR_LETTER_NOT_RECOGNISED);
                response.setError("Invalid letter");
            }

        } else {
            response.setErrorKey(ErrorKeys.ERROR_SESSION_NOT_ACTIVE);
            response.setError("Session not found");
        }

        GameLogger.info(String.format("Response for ghost/addLetter with letter %s, result %s and error %s.",
            response.getLetter(), response.getWinner(), response.getError()));

        return response;
    }

    @Profiled
    @ResponseBody
    @RequestMapping(value = "/ghost/getString", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public GetStringResponse getString(@RequestBody GetStringRequest request)
    {
        GameLogger.info(String.format("Request for ghost/getString with token %s.", request.getToken()));

        GetStringResponse response = new GetStringResponse();

        SessionBean session = this.sessionService.getSession(GHOST_NAME, request.getToken());
        if (session != null) {
            try {
                response.setString(this.ghostService.getString(session));

            } catch (Exception ex) {
                response.setErrorKey(ErrorKeys.ERROR_SERVER);
                response.setError("Server unavailable");
            }

        } else {
            response.setErrorKey(ErrorKeys.ERROR_SESSION_NOT_ACTIVE);
            response.setError("Session not found");
        }

        GameLogger.info(String.format("Response for ghost/getString with result %s and error %s.",
            response.getString(), response.getError()));

        return response;
    }
}
