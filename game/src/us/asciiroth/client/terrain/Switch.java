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
import static us.asciiroth.client.core.Flags.AMMUNITION;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.event.Events;

/**
 * A switch. If the player "touches" it, it will fire a color event
 * on the board, matching the switch's color.
 *
 */
public class Switch extends AbstractTerrain {

    private final State state;
    
    /**
     * Constructor.
     * @param color
     * @param state
     */
    private Switch(Color color, State state) {
        super(color.getName() + " Switch", 0, color, (state.isOn()) ?
            new Symbol("!", BLACK, color) :
            (state.isOff()) ? new Symbol("&#161;", BLACK, color) : null
        );
        this.state = state;
    }
    /**
     * Get the state of this switch. On/off doesn't really matter so much 
     * here as that every activation of the switch causes it to change
     * appearance and fire a color event.
     * @return  the trigger state (on or off)
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
        TerrainUtils.toggleCellState(cell, this, state);
        event.getBoard().fireColorEvent(event, color, cell);
    }
    /**
     * You can throw things at switches and trigger them, but you can't shoot
     * at them.
     * @param event
     * @param cell
     * @param flier 
     */
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        event.cancel();
        if (flier.getItem().not(AMMUNITION)) {
            TerrainUtils.toggleCellState(cell, this, state);
            event.getBoard().fireColorEvent(event, color, cell);
        }
    }
    /** Type serializer. */
    public static final Serializer<Switch> SERIALIZER = new Serializer<Switch>() {
        public Switch create(String[] args) {
            return new Switch(Color.byName(args[1]), State.byName(args[2]));
        }
        public String store(Switch s) {
            return Util.format("Switch|{0}|{1}", s.color.getName(), s.state.getName());
        }
        public Switch example() {
            return new Switch(STEELBLUE, State.ON);
        }
        public String template(String key) {
            return "Switch|{color}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
        
    };
}
