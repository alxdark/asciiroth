package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.GOLD;
import static us.asciiroth.client.core.Flags.FLIER;
import static us.asciiroth.client.core.Flags.ORGANIC;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * Killer bee. They have a high range, a small sting, and they die afterward.
 * They fire a color event when they die that can be linked to a BeeHive to 
 * supply a steady stream of bees.
 * 
 * @see us.asciiroth.client.terrain.BeeHive
 *
 */
public class KillerBee extends AbstractAnimatedAgent {

    private static final Targeting TARGETING = 
        new Targeting().attackPlayer(7).moveRandomly();
    
    private KillerBee(Color color) {
        super("Killer Bee", (FLIER | ORGANIC), color, 
            CombatStats.KILLER_BEE_CTBH, new Symbol("a", GOLD));
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 3 == 0) {
            Game.get().agentMove(cell, this, TARGETING);
        }
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.KILLER_BEE_DAMAGE);
        Game.get().die(attackerLoc, this);
    }
    /** Type serializer. */
    public static final Serializer<KillerBee> SERIALIZER = new BaseSerializer<KillerBee>() {
        public KillerBee create(String[] args) {
            return new KillerBee(Color.byName(args[1]));
        }
        public KillerBee example() {
            return new KillerBee(Color.NONE);
        }
        public String store(KillerBee kb) {
            return "KillerBee|"+kb.color.getName();
        }
        public String template(String type) {
            return "KillerBee|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
