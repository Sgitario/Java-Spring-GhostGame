package game.webapp.controllers;

import game.webapp.controllers.errors.ErrorMessages;
import game.webapp.controllers.models.AddLetterRequest;
import game.webapp.controllers.models.AddLetterResponse;
import game.webapp.controllers.models.EndGameRequest;
import game.webapp.controllers.models.GetStringRequest;
import game.webapp.controllers.models.GetStringResponse;
import game.webapp.controllers.models.ErrorResponse;
import game.webapp.controllers.models.StartGameRequest;
import game.webapp.controllers.models.StartGameResponse;
import gameframework.services.interfaces.IGhostService;
import gameframework.transversal.models.SessionBean;

import org.apache.log4j.Logger;
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
public class GhostController extends BaseController {
	private final static String GHOST_NAME = "ghost";
	private static final Logger LOG = Logger.getLogger(GhostController.class);

	@Autowired
	private IGhostService ghostService;

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView modelView = new ModelAndView("ghost");

		return modelView;
	}

	@Profiled
	@ResponseBody
	@RequestMapping(value = "/" + GHOST_NAME + "/startGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public StartGameResponse startGame(@RequestBody StartGameRequest request) {
		return super.startGame(GHOST_NAME, request);
	}

	@Profiled
	@ResponseBody
	@RequestMapping(value = "/" + GHOST_NAME + "/endGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ErrorResponse endGame(@RequestBody EndGameRequest request) {
		return super.endGame(GHOST_NAME, request.getToken());
	}

	@Profiled
	@ResponseBody
	@RequestMapping(value = "/ghost/addLetter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AddLetterResponse addLetter(@RequestBody AddLetterRequest request) {
		LOG.info(String.format(
				"Request for ghost/addLetter with token %s and letter %s.",
				request.getToken(), request.getLetter()));

		// Get session.
		SessionBean session = this.sessionService.getSession(GHOST_NAME,
				request.getToken());

		validateSession(session);
		validateLetter(request.getLetter(), session);

		// Get response from ghost service.
		String answerLetter = this.ghostService.addLetter(request.getLetter(),
				session);

		// Update properly the session.
		if (!session.isFinished()) {
			this.sessionService.updateSession(session);
		} else {
			endGame(GHOST_NAME, request.getToken());
		}

		LOG.info(String.format(
				"Response for ghost/addLetter with letter %s, winner %s.",
				answerLetter, session.getWinner()));

		return new AddLetterResponse(answerLetter, session.isFinished(),
				session.getWinner());
	}

	@Profiled
	@ResponseBody
	@RequestMapping(value = "/ghost/getString", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GetStringResponse getString(@RequestBody GetStringRequest request) {
		LOG.info(String.format("Request for ghost/getString with token %s.",
				request.getToken()));

		// Get session
		SessionBean session = this.sessionService.getSession(GHOST_NAME,
				request.getToken());
		validateSession(session);

		// Get current string by session.
		String currentString = this.ghostService.getString(session);

		LOG.info(String.format("Response for ghost/getString with result %s.",
				currentString));

		return new GetStringResponse(currentString);
	}

	private void validateLetter(String letter, SessionBean session) {
		if (!this.ghostService.checkLetter(letter, session)) {
			throw new IllegalArgumentException(
					ErrorMessages.ERROR_LETTER_NOT_RECOGNISED);
		}
	}

	private void validateSession(SessionBean session) {
		if (session == null) {
			throw new IllegalArgumentException(
					ErrorMessages.ERROR_SESSION_NOT_ACTIVE);
		}
	}
}
