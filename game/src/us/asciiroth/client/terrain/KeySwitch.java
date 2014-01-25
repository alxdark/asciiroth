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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.STEELBLUE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Key;

/**
 * A kind of switch that can only be flipped through the use of 
 * a key that matches the <code>KeySwitch</code>'s color.
 */
public class KeySwitch extends AbstractTerrain {
    
    private final State state;
    
    /**
     * Constructor.
     * @param color
     * @param state
     */
    private KeySwitch(Color color, State state) {
        super(color.getName() + " Key Switch", 0, color, (state.isOn()) ?
            new Symbol("?", BLACK, color) :
            new Symbol("&#191;", BLACK, color)    
        );
        this.state = state;
    }
    /**
     * Get the state of this key switch. Moving from one state to the
     * other triggers a color event on the board
     * @return  on or off
     */
    public State getState() {
        return state;
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        if (dir.isDiagonal()) {
            Events.get().fireMessage(cell, "You must use it square on");
            return;
        }
        Item item = player.getBag().getSelected();
        if (item instanceof Key) {
            if (item.getColor() == color) {
                TerrainUtils.toggleCellState(cell, this, state);
                event.getBoard().fireColorEvent(event, color, cell);
                player.getBag().remove(item);
            } else {
                Events.get().fireMessage(cell, "That key is the wrong color");
            }
        } else {
            Events.get().fireMessage(cell, "This switch requires a key to operate");
        }
    }
    /** Type serializer. */
    public static final Serializer<KeySwitch> SERIALIZER = new Serializer<KeySwitch>() {
        public KeySwitch create(String[] args) {
            return new KeySwitch(Color.byName(args[1]), State.byName(args[2]));
        }
        public String store(KeySwitch s) {
            return Util.format("KeySwitch|{0}|{1}", s.color.getName(), s.state.getName());
        }
        public KeySwitch example() {
            return new KeySwitch(STEELBLUE, State.ON);
        }
        public String template(String key) {
            return "KeySwitch|{color}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
