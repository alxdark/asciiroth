package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.MUD;
import static us.asciiroth.client.core.Color.NONE;
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
 * Mud. Agents get caught in it sometimes when trying to move out.
 */
public class Mud extends AbstractTerrain {

    private Mud() {
        super("Mud", (TRAVERSABLE | PENETRABLE | ETHEREAL), new Symbol("&emsp;", NONE, MUD));
    }

    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isVertical()) {
            event.cancel();
            return;
        }
        // It's more likely you'll get stuck in the middle of the mud...
        int chanceToExit = (cell.getApparentTerrain() instanceof Mud) ? 70 : 30;
        if (dir.isVertical() || Random.nextInt(100) < chanceToExit) {
            event.cancel("You're stuck in the mud");
        }
    }
    /** Type serializer. */
    public static final Serializer<Mud> SERIALIZER = new TypeOnlySerializer<Mud>() {
        public Mud create(String[] args) {
            return new Mud();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
