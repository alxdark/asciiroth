package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.SALMON;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

/**
 * When the player enters this terrain, he or she is teleported to the indicated 
 * map and position. Be careful because this class does no error checking and the 
 * game will fail if the map is not valid. 
 *
 */
public class Teleporter extends AbstractTerrain implements Animated {

    private final String boardID;
    private final int x;
    private final int y;
    
    private static Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&#8734;", RED, null, RED, BUILDING_FLOOR),
        new Symbol("&#8734;", SALMON, null, SALMON, BUILDING_FLOOR),
        new Symbol("&#8734;", ORANGE, null, ORANGE, BUILDING_FLOOR),
        new Symbol("&#8734;", WHITE, null, WHITE, BUILDING_FLOOR)
    };
    
    /**
     * Constructor.
     * @param boardID
     * @param x
     * @param y
     */
    private Teleporter(String boardID, int x, int y) {
        super("Teleporter", PENETRABLE, SYMBOLS[3]);
        this.boardID = boardID;
        this.x = x;
        this.y = y;
    }
    @Override
    public void onEnter(final Event event, Player player, final Cell cell, Direction dir) {
        player.teleport(event, dir, boardID, x, y);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Events.get().fireRerender(cell, this, SYMBOLS[(int)frame % 4]);
    }
    
    /** Type serializer. */
    public static final Serializer<Teleporter> SERIALIZER = new Serializer<Teleporter>() {
        public Teleporter create(String[] args) {
            return new Teleporter(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        public Teleporter example() {
            return new Teleporter("", 0, 0);
        }
        public String store(Teleporter t) {
            return Util.format("Teleporter|{0}|{1}|{2}",
                t.boardID, Integer.toString(t.x), Integer.toString(t.y));
        }
        public String template(String type) {
            return "Teleporter|{boardID}|{x}|{y}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
