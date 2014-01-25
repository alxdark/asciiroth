/**
 * Copyright 2008 Alx Dark
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.    
 */
package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.BLUE;
import static us.asciiroth.client.core.Color.DARKBLUE;
import us.asciiroth.client.Registry;
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
import us.asciiroth.client.items.MirrorShield;
import us.asciiroth.client.items.Parabullet;

/**
 * A beholder, or floating eye, similar to Nethack. It shoots a paralyzing bullet 
 * that will freeze the player, then goes in for the kill. It fires a color event 
 * when it dies.
 *
 */
public class Optilisk extends AbstractAnimatedAgent {

    private static final Targeting FIRING_TARGETING = 
        new Targeting().attackPlayer(10);
    private static final Targeting MOVE_TARGETING = 
        new Targeting().attackPlayer(14).moveRandomly().trackPlayer();
    
    private final Parabullet bullet;
    
    /**
     * Constructor.
     * @param color
     */
    public Optilisk(Color color) {
        super("Optilisk", 0, color, CombatStats.OPTILISK_CTBH, 
            new Symbol("e", BLUE, null, DARKBLUE, null));
        this.bullet = (Parabullet)Registry.get().getPiece(Parabullet.class);
    }
    
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.OPTILISK_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 6 == 0) {
            boolean disabled = (ctx.getBoard().getCurrentCell().getAgent() instanceof Paralyzed);
            // Smart enough to stop shooting at player when holding mirror shield, or already disabled
            if (!disabled && !(ctx.getPlayer().getBag().getSelected() instanceof MirrorShield)) {
                Game.get().shoot(cell, this, bullet, FIRING_TARGETING);
            }
        } else if (frame % 8 == 0) {
            Game.get().agentMove(cell, this, MOVE_TARGETING);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Optilisk> SERIALIZER = new BaseSerializer<Optilisk>() {
        public Optilisk create(String[] args) {
            return new Optilisk(Color.byName(args[1]));
        }
        public Optilisk example() {
            return new Optilisk(Color.NONE);
        }
        public String store(Optilisk o) {
            return "Optilisk|"+o.color.getName();
        }
        public String template(String type) {
            return "Optilisk|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };

}
