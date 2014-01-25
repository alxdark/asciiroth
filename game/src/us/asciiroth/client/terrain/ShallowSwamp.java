package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BUSHES;
import static us.asciiroth.client.core.Color.SURF;
import static us.asciiroth.client.core.Flags.ETHEREAL;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

import com.google.gwt.user.client.Random;

/**
 * Swamp. Agents get caught in it sometimes when trying to move out.
 */
public class ShallowSwamp extends AbstractTerrain {

    protected ShallowSwamp() {
        super("Swamp", (TRAVERSABLE | PENETRABLE | ETHEREAL), new Symbol("&#8230;", BUSHES, SURF));
    }

    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isVertical()) {
            event.cancel();
            return;
        }
        // It's more likely you'll get stuck in the middle of the swamp...
        int chanceToExit = (cell.getApparentTerrain() instanceof Swamp) ? 70 : 30;
        if (dir.isVertical() || Random.nextInt(100) < chanceToExit) {
            event.cancel("You're stuck in the swamp");
        }
    }
    /** Type serializer. */
    public static final Serializer<ShallowSwamp> SERIALIZER = new TypeOnlySerializer<ShallowSwamp>() {
        public ShallowSwamp create(String[] args) {
            return new ShallowSwamp();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
