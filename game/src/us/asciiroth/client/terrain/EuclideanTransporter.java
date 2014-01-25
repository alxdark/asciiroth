package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLUE;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

/**
 * This is a special transporter that has complicated activation requirements (exactly
 * for Euclidean Engines need to be activated on the board before this transporter will
 * switch from the off to the on state, and allow the player to transport).
 */
public class EuclideanTransporter extends AbstractTerrain implements Animated {

    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&Theta;", RED, null, RED, BUILDING_FLOOR),
        new Symbol("O", BLUE, null, BLUE, BUILDING_FLOOR),
        new Symbol("&Theta;", RED, null, RED, BUILDING_FLOOR),
        new Symbol("O", BLUE, null, BLUE, BUILDING_FLOOR)
    };
    
    private final String boardID;
    private final int x;
    private final int y;
    private final State state;
    
    /**
     * Constructor.
     * @param boardID
     * @param x
     * @param y
     * @param state
     */
    private EuclideanTransporter(String boardID, int x, int y, State state) {
        super("Euclidean Transporter", (TRAVERSABLE | PENETRABLE), SYMBOLS[0]);
        this.boardID = boardID;
        this.x = x;
        this.y = y;
        this.state = state;
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (state.isOn()) {
            player.teleport(event, dir, boardID, x, y);
        } else {
            Events.get().fireMessage(cell, "You're standing on a complex device");
        }
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (state.isOn()) {
            event.cancel();
        }
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOn() && frame%3==0) {
            Events.get().fireRerender(cell, this, SYMBOLS[frame%4]);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<EuclideanTransporter> SERIALIZER = new Serializer<EuclideanTransporter>() {
        public EuclideanTransporter create(String[] args) {
            return new EuclideanTransporter(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), State.byName(args[4]));
        }
        public EuclideanTransporter example() {
            return new EuclideanTransporter("", 0, 0, State.OFF);
        }
        public String store(EuclideanTransporter et) {
            return Util.format("EuclideanTransporter|{0}|{1}|{2}|{3}",
                et.boardID, Integer.toString(et.x), Integer.toString(et.y), et.state.getName());
        }
        public String template(String type) {
            return "EuclideanTransporter|{boardID}|{x}|{y}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
};
}
