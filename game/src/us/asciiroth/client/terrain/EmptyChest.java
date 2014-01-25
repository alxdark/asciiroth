package us.asciiroth.client.terrain;

import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * An indicator of an opened and empty chest. 
 */
public class EmptyChest extends OpeningMarker {

    /** Constructor. */
    public EmptyChest() {
        super("Empty Chest", "x", "&#8852;");
    }
    /** Type serializer. */
    public static final Serializer<EmptyChest> SERIALIZER = new TypeOnlySerializer<EmptyChest>() {
        public EmptyChest create(String[] args) {
            return new EmptyChest();
        }
        public String getTag() {
            return "Room Features";
        }        
    };
}
