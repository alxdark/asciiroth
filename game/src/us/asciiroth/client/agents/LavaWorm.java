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

import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.LAVITIC;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
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
 * An agent that lives and fights in lava (it should not be placed outside
 * of lava, or it won't be able to move). Fires a color event when it dies.
 *
 */
public class LavaWorm extends AbstractAnimatedAgent implements Animated {
    
    private static final Targeting TARGETING = 
        new Targeting().attackPlayer(8).moveRandomly().trackPlayer();

    /**
     * Constructor
     * @param color     color of event fired when this agent dies
     */
    public LavaWorm(Color color) {
        super("Lava Worm", LAVITIC | CARNIVORE | FIRE_RESISTANT, color, 
            CombatStats.LAVA_WORM_CTBH, new Symbol("z", RED));
    }
    @Override
    public boolean canEnter(Direction direction, Cell from, Cell to) {
        return (to.getTerrain().is(LAVITIC));
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.LAVA_WORM_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 3 == 0) {
            Game.get().agentMove(cell, this, TARGETING);
        }
    }
    /** Type serializer. */
    public static final Serializer<LavaWorm> SERIALIZER = new BaseSerializer<LavaWorm>() {
        public LavaWorm create(String[] args) {
            return new LavaWorm(Color.byName(args[1]));
        }
        public LavaWorm example() {
            return new LavaWorm(Color.NONE);
        }
        public String store(LavaWorm piece) {
            return "LavaWorm|"+piece.color.getName();
        }
        public String template(String type) {
            return "LavaWorm|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
