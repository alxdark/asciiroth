package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.SILVER;
import static us.asciiroth.client.core.Flags.MELEE_WEAPON;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A hammer (warhammer, perhaps).
 *
 */
public class Hammer extends AbstractItem {

    /** Constructor. */
    private Hammer() {
        super("Hammer", MELEE_WEAPON, new Symbol("&tau;", SILVER, null, NEARBLACK, null));
    }

    @Override
    public void onHit(Event event, Cell cell, Agent agent) {
        Game.get().damage(cell, agent, CombatStats.HAMMER_DAMAGE);
    }
    /** Type serializer. */
    public static final Serializer<Hammer> SERIALIZER = new TypeOnlySerializer<Hammer>() {
        public Hammer create(String[] args) {
            return new Hammer();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
