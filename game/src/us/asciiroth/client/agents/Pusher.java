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
 * A pusher is an object that moves slowly in one and only one direction on
 * the board. It can sit dormant until triggered by a color event, or it 
 * can be placed active on the board but held in place by another piece like
 * a boulder. (However, once it is started, it cannot be stopped again.) It 
 * will push sliders in its path and blow up bombs when it hits them. 
 */
public class Pusher extends AbstractAgent implements Animated, ColorListener {

    // Smaller: Up &#9652;, Down, &#9662;, West, &#9664;, East &#9654;
    // Larger: Up 9650, Down, 9660, West, 9668, East 9658
    private final Direction direction;
    private final State state;
    
    /**
     * Constructor.
     * @param dir   the direction the pusher will move
     * @param color the color event that will turn pusher on/off
     * @param state the current state of this pusher instance
     */
    public Pusher(Direction dir, Color color, State state) {
        super("Pushable", 0, color, CombatStats.INDESTRUCTIBLE,
            new Symbol(
                (dir == Direction.NORTH) ? "&#9650;" :
                (dir == Direction.SOUTH) ? "&#9660;" :
                (dir == Direction.EAST) ? "&#9658;" :
                (dir == Direction.WEST) ? "&#9668;" :
                null, Color.LIGHTSTEELBLUE, null, Color.MIDNIGHTBLUE, null));
        if (state == null) {
            throw new RuntimeException("Pusher state cannot be null");
        }
        if (dir == null) {
            throw new RuntimeException("Pusher direction cannot be null");
        }
        this.direction = dir;
        this.state = state;
    }
    
    public boolean randomSeed() {
        return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOn() && (frame%6==0)) {
            Event event = Game.get().createEvent();
            Game.get().agentMove(event, cell, this, direction);
        }
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.PUSHER_DAMAGE);
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        if (state.isOff()) {
            cell.removeAgent(this);
            cell.setAgent(AgentUtils.getAgentOtherState(this, state));
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Pusher> SERIALIZER = new BaseSerializer<Pusher>() {
        public Pusher create(String[] args) {
            return new Pusher(Direction.byName(args[1]), Color.byName(args[2]), State.byName(args[3]));
        }
        public Pusher example() {
            return new Pusher(Direction.NORTH, Color.NONE, State.ON);
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(Pusher p) {
            return Util.format("Pusher|{0}|{1}|{2}", 
                p.direction.getName(), p.color.getName(), p.state.getName());
        }
        public String template(String type) {
            return "Pusher|{direction}|{color}|{state}";
        }
    };
}
