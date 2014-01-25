package us.asciiroth.client.items;

import static us.asciiroth.client.core.Flags.MELEE_WEAPON;
import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A sword.
 *
 */
public class Sword extends AbstractItem {

    /** Constructor. */
    private Sword() {
        super("Sword", MELEE_WEAPON, new Symbol("&dagger;", "&#1006;", WHITE, null, BLACK, null));
    }

    @Override
    public void onHit(Event event, Cell cell, Agent agent) {
        Game.get().damage(cell, agent, CombatStats.SWORD_DAMAGE);
    }
    /** Type serializer. */
    public static final Serializer<Sword> SERIALIZER = new TypeOnlySerializer<Sword>() {
        public Sword create(String[] args) {
            return new Sword();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
