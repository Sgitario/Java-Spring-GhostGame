package gameframework.services.ghost.strategies;

import java.util.List;

/**
 * This interface contains the logic to play to Ghost game.
 * 
 * @author Jose Carvajal
 */
public interface IGhostStrategy {
	/**
	 * Checks whether the concrete strategy is linked with the specified level.
	 * 
	 * @param level
	 * @return
	 */
	public abstract boolean isStrategyForLevel(Integer level);

	/**
	 * Find a letter according to the strategy to continue playing.
	 * 
	 * @param string
	 * @return
	 */
	public abstract String addLetter(List<String> choices, String currentString);
}
