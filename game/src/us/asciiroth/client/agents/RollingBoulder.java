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

import static us.asciiroth.client.core.Color.BROWN;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;

/**
 * A large rock that rolls across the board and injures the player (or 
 * even kills the player, if he or she is trapped between the boulder 
 * and a wall or similar). The boulder can be activated with a color 
 * event. 
 */
public class RollingBoulder extends AbstractAgent implements Animated, ColorListener {

    private final Direction direction;
    private final State state;
    
    /** Constructor. */
    private RollingBoulder(Direction direction, Color color, State state) {
        super("Rolling Boulder", 0, color, CombatStats.INDESTRUCTIBLE, new Symbol("O", BROWN));
        this.direction = direction;
        this.state = state;
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.BOULDER_DAMAGE);
        Direction dir = Direction.inferDirection(attackerLoc, agentLoc);
        Cell next = agentLoc.getAdjacentCell(dir);
        if (next.canEnter(next, agent, dir, false)) {
            Game.get().move(direction);
        } else {
            Game.get().damage(agentLoc, agent, 500);
        }
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOn() && frame % 5 == 0) {
            Event event = Game.get().createEvent();
            Game.get().agentMove(event, cell, this, direction);
        }
    }
    public boolean randomSeed() {
        return true;
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        if (state.isOff()) {
            cell.removeAgent(this);
            cell.setAgent(AgentUtils.getAgentOtherState(this, state));
        }
    }
    /** Type serializer. */
    public static final Serializer<RollingBoulder> SERIALIZER = new BaseSerializer<RollingBoulder>() {
        public RollingBoulder create(String[] args) {
            return new RollingBoulder(Direction.byName(args[1]), Color.byName(args[2]), State.byName(args[3]));
        }
        public String getTag() {
            return "Room Features";
        }
        public RollingBoulder example() {
            return new RollingBoulder(Direction.NONE, Color.NONE, State.ON);
        }
        public String store(RollingBoulder rb) {
            return Util.format("RollingBoulder|{0}|{1}|{2}",
                rb.direction.getName(), rb.color.getName(), rb.state.getName());
        }
        public String template(String type) {
            return "RollingBoulder|{direction}|{color}|{state}";
        }
    };
}
