package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.SADDLEBROWN;
import static us.asciiroth.client.core.Color.SANDYBROWN;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.effects.InFlightItem;

/**
 * A rusty gate that cannot be opened, but you can still shoot things through it. 
 */
public class RustyGate extends AbstractTerrain {
    
    /**
     * Constructor.
     */
    public RustyGate() {
        super("Rusty Gate", PENETRABLE, 
            new Symbol("#", SANDYBROWN, null, SADDLEBROWN, BUILDING_FLOOR));
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel("The gate is too rusty to open");
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (flier.getDirection().isDiagonal()) {
            event.cancel();
        }
    }
    /** Type serializer. */
    public static final Serializer<RustyGate> SERIALIZER = new TypeOnlySerializer<RustyGate>() {
        public RustyGate create(String[] args) {
            return new RustyGate();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
