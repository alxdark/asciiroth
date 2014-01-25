package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Color.DARKSLATEBLUE;
import static us.asciiroth.client.core.Color.SILVER;
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
 * A small bug thing that moves relatively quickly and has a good bite; 
 * farthapods will also track the player. Farthapods fire a color event when 
 * they die; this should be linked to a Farthapod Nest where a new Farthapod
 * can be regenerated.
 * 
 * @see us.asciiroth.client.terrain.FarthapodNest
 *
 */
public class Farthapod extends AbstractAnimatedAgent {

    private static final Targeting TARGETING = 
        new Targeting().dodgeBullets(90).attackPlayer(7).moveRandomly().trackPlayer();

    private Farthapod(Color color) {
        super("Farthapod", (CARNIVORE | ORGANIC), color, CombatStats.FARTHAPOD_CTBH, 
            new Symbol("&curren;", SILVER, null, DARKSLATEBLUE, null));
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 5 == 0) {
            Game.get().agentMove(cell, this, TARGETING);
        }
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.FARTHAPOD_DAMAGE);
    }
    /** Type serializer. */
    public static final Serializer<Farthapod> SERIALIZER = new BaseSerializer<Farthapod>() {
        public Farthapod create(String[] args) {
            return new Farthapod(Color.byName(args[1]));
        }
        public Farthapod example() {
            return new Farthapod(Color.NONE);
        }
        public String store(Farthapod f) {
            return "Farthapod|"+f.color.getName();
        }
        public String template(String type) {
            return "Farthapod|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
