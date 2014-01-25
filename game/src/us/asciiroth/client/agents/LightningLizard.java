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

import static us.asciiroth.client.core.Color.DARKORANGE;
import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.ORGANIC;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * These are fun. They do not roam but sit still until the player 
 * approaches, then attack as fast as a bullet. Their damage is low,
 * however. In numbers they can be a real nuisance if you have no 
 * corner to retreat to. Fires a color event when killed.
 *
 */
public class LightningLizard extends AbstractAnimatedAgent {

    private static final Targeting MOVE_TARGETING = 
        new Targeting().attackPlayer(12).dodgeBullets(90);
    
    /**
     * Constructor.
     * @param color
     */
    public LightningLizard(Color color) {
        super("Lightning Lizard", (CARNIVORE | ORGANIC), color, CombatStats.LIGHTNING_LIZARD_CTBH, 
            new Symbol("&pound;", ORANGE, null, DARKORANGE, null));
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.LIGHTNING_LIZARD_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Game.get().agentMove(cell, this, MOVE_TARGETING);
    }
    /** Type serializer. */
    public static final Serializer<LightningLizard> SERIALIZER = new BaseSerializer<LightningLizard>() {
        public LightningLizard create(String[] args) {
            return new LightningLizard(Color.byName(args[1]));
        }
        public LightningLizard example() {
            return new LightningLizard(Color.NONE);
        }
        public String store(LightningLizard s) {
            return "LightningLizard|"+s.color.getName();
        }
        public String template(String type) {
            return "LightningLizard|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
