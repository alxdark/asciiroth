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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
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
 * Great old ones are indestructible and do enormous amounts of 
 * damage, but they are not super-fast and have no projectile 
 * weapon. They are best immobilized or trapped, and then avoided. 
 * They fire a color event when killed.
 *
 */
public class GreatOldOne extends AbstractAnimatedAgent {

    private static final Targeting TARGETING = 
        new Targeting().attackPlayer(12).moveRandomly().trackPlayer();
    
    /**
     * Constructor.
     * @param color
     */
    public GreatOldOne(Color color) {
        super("Great Old One", 0, color, CombatStats.INDESTRUCTIBLE, 
                new Symbol("&xi;", WHITE, null, BLACK, null)); 
    }

    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.GREAT_OLD_ONE_DAMAGE);
    }    
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 5 == 0) {
            Game.get().agentMove(cell, this, TARGETING);
        }
    }
    /** Type serializer. */
    public static final Serializer<GreatOldOne> SERIALIZER = new BaseSerializer<GreatOldOne>() {
        public GreatOldOne create(String[] args) {
            return new GreatOldOne(Color.byName(args[1]));
        }
        public GreatOldOne example() {
            return new GreatOldOne(Color.NONE);
        }
        public String store(GreatOldOne goo) {
            return "GreatOldOne|"+goo.color.getName();
        }
        public String template(String type) {
            return "GreatOldOne|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
