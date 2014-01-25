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
package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.STEELBLUE;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.Boulder;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.items.Key;

/**
 * A door that can be open (unlocked) or closed (locked). In the latter case,
 * it can only be opened by a color event for the color of the door, or by 
 * the use of a key of the same color as the door. 
 */
public class Door extends AbstractTerrain implements ColorListener {
    
    private final State state;
    
    /**
     * Constructor.
     * @param color
     * @param state
     */
    private Door(Color color, State state) {
        super(color.getName() + " Door", 0, color, 
            (state == State.ON) ? new Symbol("&middot;", color, null, color, BUILDING_FLOOR) :
            (state == State.OFF) ? new Symbol("&middot;", WHITE, color, BUILDING_FLOOR, color) : 
            null
        );
        this.state = state;
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        TerrainUtils.toggleCellState(cell, this, state);
    }
    /** Get state of door
     * 
     * @return  state of door (on for open, off for closed)
     */
    public State getState() {
        return state;
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return (state.isOn() && !direction.isDiagonal());
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return (!direction.isDiagonal());
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal() || dir.isVertical()) {
            event.cancel();
        } else {
            PlayerBag bag = player.getBag();
            Item item = bag.getSelected();
            if (state.isOff()) {
                enterClosedDoor(event, cell, bag, item);
            } else {
                enterOpenDoor(event, cell, bag, item);        
            }
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal() || dir.isVertical()) {
            event.cancel();
        }
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (agent instanceof Boulder) {
            event.cancel(cell, "It's too big.");
        } else if (dir.isDiagonal() || dir.isVertical() || state == State.OFF) {
            event.cancel();
        }
    }
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        if (dir.isDiagonal() || dir.isVertical()) {
            event.cancel();
        }
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (state.isOff() || flier.getDirection().isDiagonal()) {
            event.cancel();
        }
    }
    private void enterOpenDoor(Event event, Cell to, PlayerBag bag, Item item) {
        if (item instanceof Key && item.getColor() == color && to.getAgent() == null) {
            onColorEvent(event, to, to);
            bag.remove(item);
            event.cancel(to, "You lock the door.");
        }
    }
    private void enterClosedDoor(Event event, Cell to, PlayerBag bag, Item item) {
        if (item instanceof Key && item.getColor() == color && to.getAgent() == null) {
            onColorEvent(event, to, to);
            bag.remove(item);
            event.cancel(to, "Door unlocks.");
        } else {
            event.cancel(to, "The door is locked.");
        }
    }
    /** Type serializer. */
    public static final Serializer<Door> SERIALIZER = new Serializer<Door>() {
        public Door create(String[] args) {
            return new Door(Color.byName(args[1]), State.byName(args[2]));
        }
        public String store(Door d) {
            return Util.format("Door|{0}|{1}", d.color.getName(), d.state.getName());
        }
        public Door example() {
            return new Door(STEELBLUE, State.OFF);
        }
        public String template(String key) {
            return "Door|{color}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
    };    
}
