package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.YELLOW;
import static us.asciiroth.client.core.Flags.ETHEREAL;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * A campfire. 
 */
public class Campfire extends AbstractAgent implements Animated {

    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&omega;", RED),
        new Symbol("&omega;", ORANGE),
        new Symbol("&omega;", RED),
        new Symbol("&omega;", YELLOW)
    };    
    /** Constructor. */
    public Campfire() {
        super("Campfire", (PENETRABLE | ETHEREAL), NONE, CombatStats.INDESTRUCTIBLE, SYMBOLS[0]);
    }
    public boolean randomSeed() {
        return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%4==0) { // So it's 4, 8, 12, 16
            Events.get().fireRerender(cell, this, SYMBOLS[(frame %16)/4]);    
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Campfire> SERIALIZER = new TypeOnlySerializer<Campfire>() {
        public Campfire create(String[] args) {
            return new Campfire();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
