package us.asciiroth.client.terrain;

import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Floor that has rubble on it. 
 */
public class Rubble extends OpeningMarker {

    /** Constructor. */
    public Rubble() {
        super("Rubble-Strewn Floor", "&#4347;", "&there4;");
    }
    /** Type serializer. */
    public static final Serializer<Rubble> SERIALIZER = new TypeOnlySerializer<Rubble>() {
        public Rubble create(String[] args) {
            return new Rubble();
        }
        public String getTag() {
            return "Room Features";
        }        
    };
}
