package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.MEAT;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

import com.google.gwt.user.client.Random;


/**
 * The head of an agent. You would create this using the <code>PieceCreator</code>
 * on the death of a specific agent, in order to create something that the player
 * could pick up and carry back in order to finish a "slay monster" type quest.
 * <p>
 * A head does attract the same attention as fish and bones. But there are some
 * restrictions, based on decorum, as to what you can do with it. ;-)
 * <p>
 * NOTE: A whole agent isn't used here because otherwise this piece couldn't 
 * be embedded into a PieceCreator, as it would involve escaping to two 
 * levels. So just the name is used.
 *
 */
public class Head extends AbstractItem {

    private final String name;
    
    /**
     * Constructor.
     * @param name
     */
    public Head(String name) {
        super(name+" Head", MEAT, new Symbol("&#9787;", WHITE, null, BLACK, null));
        this.name = name;
    }
    @Override
    public void onThrow(Event event, Cell cell) {
        event.cancel("Please, show some respect!");
    }
    @Override
    public void onUse(Event event) {
        event.cancel("That makes no family-friendly sense whatsoever.");
    }
    @Override
    public void onSteppedOn(Event event, Cell agentLoc, Agent agent) {
        if (agent.is(CARNIVORE)) {
            if (Random.nextInt(100) < 15) {
                agentLoc.getBag().remove(this);
                agentLoc.getBag().add((Bone)Registry.get().getPiece(Bone.class));
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Head> SERIALIZER = new BaseSerializer<Head>() {
        public Head create(String[] args) {
            return new Head(args[1]);
        }
        public Head example() {
            return new Head("Agent");
        }
        public String getTag() {
            return "Artifacts";
        }
        public String store(Head h) {
            return "Head|"+h.name;
        }
        public String template(String type) {
            return "Head|{agentName}";
        }
    };
}
