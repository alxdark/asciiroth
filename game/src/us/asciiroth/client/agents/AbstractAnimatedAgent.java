package us.asciiroth.client.agents;

import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Symbol;

/**
 * Abstract class for animated agents.
 *
 */
public abstract class AbstractAnimatedAgent extends AbstractAgent implements Animated {
	
    /**
     * Constructor. 
     * @param name
     * @param flags
     * @param color
     * @param chanceToHit
     * @param symbol
     */
	public AbstractAnimatedAgent(String name, int flags, Color color, int chanceToHit, Symbol symbol) {
		super(name, flags, color, chanceToHit, symbol);
	}

	public boolean randomSeed() {
		return true;
	}

}
