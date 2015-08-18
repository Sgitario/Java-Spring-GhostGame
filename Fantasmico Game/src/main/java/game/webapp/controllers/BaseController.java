package game.webapp.controllers;

import game.webapp.controllers.models.ErrorResponse;
import game.webapp.controllers.models.StartGameRequest;
import game.webapp.controllers.models.StartGameResponse;
import gameframework.services.interfaces.ISessionService;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Base Controller enables: - Start and end games with session tokens.
 * - Error handling.
 * 
 * @author jhilario
 *
 */
public abstract class BaseController {

	private static final Logger LOG = Logger.getLogger(BaseController.class);

	@Autowired
	protected ISessionService sessionService;

	/**
	 * Start game request handler by registering the
	 * session to generate a new token.
	 * 
	 * @param game
	 * @param request
	 * @return
	 */
	protected StartGameResponse startGame(String game, StartGameRequest request) {
		LOG.info(String.format(
				"Request for %s/startGame with lang %s and level %s.", game,
				request.getLang(), request.getLevel()));

		String token = this.sessionService.registerSession(game,
				request.getLang(), request.getLevel(),
				new HashMap<String, String>());

		LOG.info(String.format("Response for %s/startGame with token %s.",
				game, token));

		return new StartGameResponse(token);
	}

	/**
	 * End the game by unregistering the session.
	 * 
	 * @param game
	 * @param request
	 * @return
	 */
	protected ErrorResponse endGame(String game, String token) {
		LOG.info(String.format("Request for %s/endGame with token %s.", game,
				token));

		boolean result = this.sessionService.unregisterSession(game,
				token);

		LOG.info(String.format("Response for %s/endGame with result %s.", game,
				result));
		
		return new ErrorResponse();
	}

	/**
	 * Exception handler to parse the error into the proper response instance.
	 * @param req
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public @ResponseBody ErrorResponse handleError(HttpServletRequest req,
			Exception exception) {
		LOG.error("Request: " + req.getRequestURL() + " raised " + exception,
				exception);

		ErrorResponse response = new ErrorResponse();
		response.setError(exception.getMessage());
		return response;
	}
}
