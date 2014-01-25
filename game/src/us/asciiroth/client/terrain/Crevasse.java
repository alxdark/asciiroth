package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.VERYBLACK;
import static us.asciiroth.client.core.Flags.ETHEREAL;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * An impassable crevasse. On outside terrain it's more convincing to use the 
 * cliff decorator to achieve something similar, so this piece has no
 * outside representation. 
 * 
 * @author alxdark
 *
 */
public class Crevasse extends AbstractTerrain {

    private static final Symbol REVEALED = new Symbol("&asymp;", BLACK, VERYBLACK);
    private static final Symbol NOT_REVEALED = new Symbol("&emsp;", NONE, BLACK);
    
    /** Constructor. */
    private Crevasse() {
        super("Crevasse", (ETHEREAL | PENETRABLE), NOT_REVEALED);
    }
    
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        super.onEnter(event, player, cell, dir);
        if (event.isCancelled()) {
            event.cancel("A deep crevasse spans before you");    
        }
    }
    @Override
    public void onAdjacentTo(Context context, Cell cell) {
        Events.get().fireRerender(cell, this, REVEALED);
    }
    @Override
    public void onNotAdjacentTo(Context context, Cell cell) {
        Events.get().fireRerender(cell, this, NOT_REVEALED);
    }
    
    /** Type serializer. */
    public static final Serializer<Crevasse> SERIALIZER = new TypeOnlySerializer<Crevasse>() {
        public Crevasse create(String[] args) {
            return new Crevasse();
        }
        public String getTag() {
            return "Terrain";
        }
    };
}
