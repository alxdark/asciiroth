package us.asciiroth.client.terrain.decorators;

import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Floor;

/**
 * When this decorator receives a color event, it destroys the agent at 
 * the given location, if there is one. If the color is specified as 
 * NONE, than any agent (besides the player) that enters this terrain 
 * will disappear. This is useful if paired with an <code>AgentCreator</code> 
 * that is feeding something like rolling boulders or pushers onto the
 * board.
 *
 */
public class AgentDestroyer extends Decorator {

    /**
     * Constructor.
     * @param terrain
     * @param color     color to trigger agent destruction, or none if any
     *                  agent that enters cell should be removed from the 
     *                  board (except the player)
     */
    public AgentDestroyer(Terrain terrain, Color color) {
        super(terrain, 0, color);
    }
    public Terrain proxy(Terrain terrain) {
        return new AgentDestroyer(terrain, color);
    }
    @Override
    protected void onAgentEnterInternal(Event event, Agent agent, Cell cell, Direction dir) {
        if (color == NONE) {
            cell.getAdjacentCell(dir.getReverseDirection()).removeAgent(agent);
            Game.get().die(cell, agent);
        }
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        if (cell.getAgent() != null) {
            Game.get().die(cell, cell.getAgent());
        }
    }
    
    /** Type serializer. */
    public static final Serializer<AgentDestroyer> SERIALIZER = new BaseSerializer<AgentDestroyer>() {
        public AgentDestroyer create(String[] args) {
            return new AgentDestroyer(unescTerrain(args[1]), Color.byName(args[2]));
        }
        public AgentDestroyer example() {
            return new AgentDestroyer(new Floor(), Color.STEELBLUE);
        }
        public String store(AgentDestroyer ac) {
            return Util.format("AgentDestroyer|{0}|{1}", esc(ac.terrain), ac.color.getName());
        }
        public String template(String type) {
            return "AgentDestroyer|{terrain}|{color}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
