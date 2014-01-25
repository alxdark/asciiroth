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

import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Color.DARKORCHID;
import static us.asciiroth.client.core.Color.ORCHID;
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
import us.asciiroth.client.items.Stoneray;

/**
 * Cephalids have both a ranged and melee-based ability to turn the 
 * player to stone. There's almost no hope of the player turning back
 * again once stonified, although if a color event occurs thats the 
 * same color as the <code>Cephalid</code>, the player would recover. 
 */
public class Cephalid extends AbstractAnimatedAgent {
    
    private static final Targeting SHOOT_TARGETING = 
        new Targeting().attackPlayer(14);
    private static final Targeting MOVE_TARGETING = 
        new Targeting().attackPlayer(14).moveRandomly().trackPlayer();

    private final Stoneray stoneray;
    
    /**
     * Constructor.
     * @param color
     */
    public Cephalid(Color color) {
        super("Cephalid", ORGANIC, color, CombatStats.CEPHALID_CTBH, 
            new Symbol("&euro;", ORCHID, null, DARKORCHID, null));
        this.stoneray = (Stoneray)Registry.get().getPiece(Stoneray.class);
    }

    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        AgentUtils.turnToStone(agentLoc, agent, color);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 5 == 0) {
            Game.get().agentMove(cell, this, MOVE_TARGETING);
        } else if (frame % 7 == 0) {
            // Smart enough to stop shooting when player is holding mirror shield.
            if (!(ctx.getPlayer().getBag().getSelected() instanceof MirrorShield)) {
                Game.get().shoot(cell, this, stoneray, SHOOT_TARGETING);    
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Cephalid> SERIALIZER = new BaseSerializer<Cephalid>() {
        public Cephalid create(String[] args) {
            return new Cephalid(Color.byName(args[1]));
        }
        public Cephalid example() {
            return new Cephalid(Color.NONE);
        }
        public String store(Cephalid c) {
            return "Cephalid|"+c.color.getName();
        }
        public String template(String type) {
            return "Cephalid|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
