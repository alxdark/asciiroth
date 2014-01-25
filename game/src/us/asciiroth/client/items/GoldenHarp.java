package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.DARKGOLDENROD;
import static us.asciiroth.client.core.Color.GOLD;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * A harp. Doesn't do anything.
 */
public class GoldenHarp extends AbstractItem {

    /**
     * Constructor.
     */
    public GoldenHarp() {
        super("Golden Harp", 0, new Symbol("&#1513;", GOLD, null, DARKGOLDENROD, null));
    }
    @Override
    public void onUse(Event event) {
        Events.get().fireMessage(event.getBoard().getCurrentCell(),"Twang!");
    }
    /** Type serializer. */
    public static final Serializer<GoldenHarp> SERIALIZER = new TypeOnlySerializer<GoldenHarp>() {
        public GoldenHarp create(String[] args) {
            return new GoldenHarp();
        }
        public String getTag() {
            return "Artifacts";
        }
    };
}
