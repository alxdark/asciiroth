package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BARELY_BUILDING_WALL;
import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * This is a plate on the floor that when crossed onto or crossed off of, will
 * trigger a color event. So if the player goes onto it, something happens, and
 * when he or she goes off of it, that something is usually undone. Typically 
 * you need to find a boulder to push onto a pressure plate in order to solve 
 * a puzzle.
 * 
 * @author alxdark
 *
 */
public class PressurePlate extends AbstractTerrain {

    private final Color color;
    
    private PressurePlate(Color color) {
        super("Pressure Plate", (TRAVERSABLE | PENETRABLE), 
            new Symbol("&Xi;", NEARBLACK, BLACK, BARELY_BUILDING_WALL, BUILDING_FLOOR));
        this.color = color;
    }
    
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        super.onAgentEnter(event, agent, cell, dir);
        if (!event.isCancelled()) {
            event.getBoard().fireColorEvent(event, this.color, cell);
        }
    }
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        super.onAgentExit(event, agent, cell, dir);
        if (!event.isCancelled()) {
            event.getBoard().fireColorEvent(event, this.color, cell);
        }
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        super.onEnter(event, player, cell, dir);
        if (!event.isCancelled()) {
            event.getBoard().fireColorEvent(event, this.color, cell);
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        super.onExit(event, player, cell, dir);
        if (!event.isCancelled()) {
            event.getBoard().fireColorEvent(event, this.color, cell);
        }
    }
    /** Type serializer. */
    public static final Serializer<PressurePlate> SERIALIZER = new Serializer<PressurePlate>() {
        public PressurePlate create(String[] args) {
            return new PressurePlate(Color.byName(args[1]));
        }
        public PressurePlate example() {
            return new PressurePlate(RED);
        }
        public String store(PressurePlate f) {
            return "PressurePlate|" + f.color.getName();
        }
        public String template(String type) {
            return "PressurePlate|{color}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
    
}
