package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.OLIVE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.effects.Fire;
import us.asciiroth.client.event.Events;

/**
 * A grenade that can be thrown and that explodes just like a fireball. 
 * These are really quite deadly and should be available in sparse quantities.
 *
 */
public class Grenade extends AbstractItem {

    /** Fire that explodes into adjacent cells upon a hit. */
    protected final Fire fire;
    
    Grenade(String name, int flags, Symbol symbol) {
        super(name, flags, symbol);
        this.fire = (Fire)Registry.get().getPiece(Fire.class);
    }
    /** Constructor. */
    public Grenade() {
        super("Grenade", 0, new Symbol("&sigma;", OLIVE));
        this.fire = (Fire)Registry.get().getPiece(Fire.class);
    }
    @Override
    public void onUse(Event event) {
        Events.get().fireMessage(event.getBoard().getCurrentCell(), 
            "Just throw a grenade to use it, but stand back");
    }
    @Override
    public void onThrowEnd(final Event event, Cell cell) {
        event.cancel();
        cell.explosion(event.getPlayer());
    }
    /** Type serializer. */
    public static final Serializer<Grenade> SERIALIZER = new TypeOnlySerializer<Grenade>() {
        public Grenade create(String[] args) {
            return new Grenade();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
