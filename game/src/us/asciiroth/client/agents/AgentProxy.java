package us.asciiroth.client.agents;

import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Symbol;

/**
 * An agent decorator that can be subclassed to alter the behavior or appearance
 * of an agent, usually for a period of time (hence the implementation of the 
 * <code>Animated</code> interface). Examples include paralysis and being turned
 * to stone (or if you prefer, statues that can come to life).
 *
 */
public abstract class AgentProxy implements Agent, Animated {

    /** The agent being wrapped. */
    protected final Agent agent;
    /** Flags added to the underlying agent's flags by this proxy. */
    protected final int flags;

    /**
     * Constructor.
     * @param agent
     * @param flags
     */
    public AgentProxy(Agent agent, int flags) {
        this.agent = agent;
        this.flags = flags;
    }
    public boolean is(int flag) {
    	return (agent.is(flag) || ((flags & flag) == flag));
    }
    public boolean not(int flag) {
    	return (agent.not(flag) && ((flags & flag) != flag));
    }
    public String getName() {
        return agent.getName();
    }
    public Color getColor() {
        return agent.getColor();
    }
    public Symbol getSymbol() {
        return agent.getSymbol();
    }
    /**
     * Get the agent being proxied.
     * @return  the agent being proxied.
     */
    public Agent getAgent() {
        return agent;
    }
    public boolean canEnter(Direction direction, Cell from, Cell to) {
        return agent.canEnter(direction, from, to);
    }
    public int changeHealth(int value) {
        return agent.changeHealth(value);
    }
    public void onDie(Event event, Cell agentLoc) {
        this.agent.onDie(event, agentLoc);
    }
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        this.agent.onHit(event, attackerLoc, agentLoc, agent);
    }
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        this.agent.onHitBy(event, agentLoc, agent, dir);
    }
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir) {
        this.agent.onHitBy(event, itemLoc, item, dir);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (agent instanceof Animated) {
            ((Animated)agent).onFrame(ctx, cell, frame);
        }
    }
}
