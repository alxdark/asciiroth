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
package us.asciiroth.client.effects;

import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.YELLOW;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.Registry;
import us.asciiroth.client.agents.trees.Stump;
import us.asciiroth.client.agents.trees.Tree;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Bomb;

/**
 * Fire that usually comes as a side-effect of an exploding fireball.
 * As well as doing a lot of damage to most agents, including the player
 * (exceptions include LavaWorms and Thermadons), fire will also melt snow
 * and ice, and burn up trees into tree stumps. 
 *
 */
public class Fire extends AbstractEffect implements Animated {

    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&omega;", RED),
        new Symbol("&omega;", ORANGE),
        new Symbol("&omega;", RED),
        new Symbol("&omega;", YELLOW)
    };
    
    private Fire() {
        super("Fire", TRANSIENT, SYMBOLS[0]);
    }
    @Override
    public boolean isAboveAgent() {
        return true;
    }
    public boolean randomSeed() {
    	return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Agent agent = cell.getAgent();
        if (frame < 5) {
            if (agent != null && agent.is(ORGANIC) && agent.not(FIRE_RESISTANT)) {
                Game.get().damage(cell, agent, CombatStats.FIRE_DAMAGE);
            }
            Events.get().fireRerender(cell, this, SYMBOLS[frame%4]);
        } else {
            cell.getEffects().remove(this);
            // Fire causes some further changes to terrain.
            if (agent instanceof Tree) {
                cell.removeAgent(agent);
                cell.setAgent((Stump)Registry.get().getPiece(Stump.class));
            }
            Bomb bomb = (Bomb)Registry.get().getPiece(Bomb.class);
            if (cell.getBag().contains(bomb)) {
                cell.getBag().remove(bomb);
                cell.explosion(ctx.getPlayer());
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Fire> SERIALIZER = new TypeOnlySerializer<Fire>() {
        public Fire create(String[] args) {
            return new Fire();
        }
        public String getTag() {
            return "Ammunition";
        }
    };

}
