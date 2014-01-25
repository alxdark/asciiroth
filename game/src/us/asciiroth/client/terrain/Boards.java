package us.asciiroth.client.terrain;

import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Floor that has crate boards on it. 
 */
public class Boards extends OpeningMarker {
    /** Constructor. */
    public Boards() {
        super("Board-Strewn Floor", "&ne;");
    }
    /** Type serializer. */
    public static final Serializer<Boards> SERIALIZER = new TypeOnlySerializer<Boards>() {
        public Boards create(String[] args) {
            return new Boards();
        }
        public String getTag() {
            return "Room Features";
        }        
    };
}
