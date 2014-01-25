package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.SIENNA;
import static us.asciiroth.client.core.Flags.HIDES_ITEMS;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A pile of trash which will hide items on this cell. You have to 
 * walk up to (actually, walk onto) the trash pile in order to 
 * see what might be there.
 * 
 * @author alxdark
 *
 */
public class TrashPile extends AbstractTerrain {

    protected TrashPile() {
        super("Trash Pile", TRAVERSABLE | PENETRABLE | HIDES_ITEMS, 
            new Symbol("%", SIENNA, BLACK, SIENNA, BUILDING_FLOOR));
    }
    /** Type serializer. */
    public static final Serializer<TrashPile> SERIALIZER = new TypeOnlySerializer<TrashPile>() {
        public TrashPile create(String[] args) {
            return new TrashPile();
        }
        public String getTag() {
            return "Room Features";
        }        
    };
}
