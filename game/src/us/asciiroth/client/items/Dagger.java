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
 * A dagger or knife.
 *
 */
public class Dagger extends AbstractItem {

    /** Constructor. */
    private Dagger() {
        super("Dagger", MELEE_WEAPON, new Symbol("+", "&#1007;", SILVER, null, NEARBLACK, null));
    }

    @Override
    public void onHit(Event event, Cell cell, Agent agent) {
        Game.get().damage(cell, agent, CombatStats.DAGGER_DAMAGE);
    }
    /** Type serializer. */
    public static final Serializer<Dagger> SERIALIZER = new TypeOnlySerializer<Dagger>() {
        public Dagger create(String[] args) {
            return new Dagger();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
