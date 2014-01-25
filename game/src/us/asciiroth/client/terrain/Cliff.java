package us.asciiroth.client.terrain;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.decorators.Decorator;

/**
 * Creates a cliff-like version of the terrain where the only way to enter or 
 * exit is via another cell that contains the same apparent terrain.
 *
 */
public class Cliff extends Decorator {

    /**
     * Constructor.
     * @param terrain
     */
    public Cliff(Terrain terrain) {
        super(terrain, 0);
    }
    public Terrain proxy(Terrain terrain) {
        return new Cliff(terrain);
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction dir) {
        return (super.canEnter(agent, cell, dir) && 
            cell.getAdjacentCell(dir.getReverseDirection()).getApparentTerrain() != terrain);
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction dir) {
        return (super.canExit(agent, cell, dir) && 
            cell.getAdjacentCell(dir).getApparentTerrain() != terrain);
    }
    @Override
    public void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
        if (cell.getAdjacentCell(dir.getReverseDirection()).getApparentTerrain() != terrain) {
            event.cancel("It's too steep to climb up here.");
        }
    }
    @Override
    public void onExitInternal(Event event, Player player, Cell cell, Direction dir) {
        if (cell.getAdjacentCell(dir).getApparentTerrain() != terrain) {
            event.cancel("It's too steep to climb down here.");
        }
    }
    /** Type serializer. */
    public static final Serializer<Cliff> SERIALIZER = new BaseSerializer<Cliff>() {
        public Cliff create(String[] args) {
            return new Cliff(unescTerrain(args[1]));
        }
        public Cliff example() {
            return new Cliff(new Floor());
        }
        public String store(Cliff cliff) {
            return Util.format("Cliff|{0}", esc(cliff.getProxiedTerrain()));
        }
        public String template(String type) {
            return "Cliff|{terrain}";
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
    
}
