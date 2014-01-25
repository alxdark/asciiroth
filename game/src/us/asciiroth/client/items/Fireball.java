package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.YELLOW;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.NOT_EDITABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

import com.google.gwt.user.client.Random;

/**
 * A good old exploding fireball that does massive amounts of damage.
 * This is a grenade that implements ammunition (and thus is transient),
 * as well as some further animation.
 */
public class Fireball extends Grenade {

    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("*", RED),
        new Symbol("*", ORANGE),
        new Symbol("*", RED),
        new Symbol("*", YELLOW)
    };
    
    /** Constructor. */
    public Fireball() {
        super("Fireball", AMMUNITION | NOT_EDITABLE, SYMBOLS[0]);
    }
    @Override
    public Symbol getSymbol() {
        return SYMBOLS[Random.nextInt(4)];
    }
    /** Type serializer. */
    public static final Serializer<Fireball> SERIALIZER = new TypeOnlySerializer<Fireball>() {
        public Fireball create(String[] args) {
            return new Fireball();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
