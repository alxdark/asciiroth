package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.DARKGOLDENROD;
import static us.asciiroth.client.core.Color.GOLDENROD;
import static us.asciiroth.client.core.Color.TAN;
import static us.asciiroth.client.core.Flags.HIDES_ITEMS;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A haystack which will hide items on this cell. You have to 
 * walk up to (actually, walk onto) the haystack in order to 
 * see what might be there.
 * 
 * @author alxdark
 *
 */
public class Haystack extends AbstractTerrain {

    protected Haystack() {
        super("Haystack", TRAVERSABLE | PENETRABLE | HIDES_ITEMS, 
            new Symbol("&#955;", "&#1002;", GOLDENROD, BLACK, DARKGOLDENROD, TAN));
    }
    /** Type serializer. */
    public static final Serializer<Haystack> SERIALIZER = new TypeOnlySerializer<Haystack>() {
        public Haystack create(String[] args) {
            return new Haystack();
        }
        public String getTag() {
            return "Outside Terrain";
        }        
    };
}
