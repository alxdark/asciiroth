package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.agents.Boulder;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.event.Events;

/**
 * A gate. Agents (all agents, not just the player) can move through a gate straight on,
 * but must stop to open it first.
 */
public class Gate extends AbstractTerrain {

    private final State state; 
    
    /**
     * Constructor.
     * @param state
     */
    public Gate(State state) {
        super("Gate", (TRAVERSABLE | PENETRABLE), 
            state.isOff() ? new Symbol("#", WHITE, null, BLACK, BUILDING_FLOOR) : 
            state.isOn() ? new Symbol("#", NEARBLACK, null, BUILDING_WALL, BUILDING_FLOOR) :
            null
        );
        this.state = state;
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return (!direction.isDiagonal());
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return (!direction.isDiagonal());
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal()) {
            event.cancel();
        }
        if (!event.isCancelled() && state == State.OFF) {
            Terrain t = TerrainUtils.getTerrainOtherState(this, state);
            cell.setTerrain(t);
            event.cancel();
            Events.get().fireMessage(cell, "You open the gate");    
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal()) {
            event.cancel();
        }
        if (!event.isCancelled()) {
            Terrain t = TerrainUtils.getTerrainOtherState(this, state);
            cell.setTerrain(t);
        }
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (agent instanceof Boulder) {
            event.cancel(cell, "It's too big.");
        } else if (state == State.OFF) {
            Terrain t = TerrainUtils.getTerrainOtherState(this, state);
            cell.setTerrain(t);
            event.cancel();
        }
    }
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        Terrain t = TerrainUtils.getTerrainOtherState(this, state);
        cell.setTerrain(t);
    }
    
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (flier.getDirection().isDiagonal()) {
            event.cancel();
        }
    }
    /** Type serializer. */
    public static final Serializer<Gate> SERIALIZER = new BaseSerializer<Gate>() {
        public Gate create(String[] args) {
            return new Gate(State.byName(args[1]));
        }
        public Gate example() {
            return new Gate(State.ON);
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(Gate g) {
            return "Gate|"+g.state.getName();
        }
        public String template(String type) {
            return "Gate|{state}";
        }
    };
    
}
