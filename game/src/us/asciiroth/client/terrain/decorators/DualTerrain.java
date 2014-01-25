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
package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.terrain.Wall;

/**
 * Dual terrain is a composite terrain that holds two other terrains,
 * only one of which is active at any given time. It is not a decorator,
 * insofar as it doesn't change the behavior of either terrain (except for 
 * the <code>ColorListener</code> interface which it implements: color events
 * will switch the dual terrain, and if the newly activated terrain implements 
 * the <code>ColorListener</code> interface, it will fire that as well). The 
 * inactive terrain is simply ignored, but knowledge of its presence at the 
 * given cell continues to exist. 
 * <p>
 * The DualTerrain exposes a method, <code>switchTerrains()</code>, that will 
 * change the current terrain, and the <code>onTrigger()</code> method will 
 * do this as well.
 * <p>
 * <code>DualTerrain</code> does implement <code>TerrainProxy</code> so that 
 * in the map editor, it is correctly marked as a compound-style terrain 
 * (otherwise it would be indistinguishable from its active terrain).
 * <p>
 * NOTE: If one of the included terrains transforms a cell, it will 
 * wipe out the DualTerrain. 
 * 
 */
public class DualTerrain implements TerrainProxy, Terrain, ColorListener {

    protected final Terrain terrain1;
    protected final Terrain terrain2;
    protected final State state;
    protected final Color color;
    
    /**
     * 
     * @param terrain1  the first terrain added is the terrain that is active when 
     *                  the state is on
     * @param terrain2  the second terrain added is the terrain that is active when
     *                  the state is off 
     * @param color
     */
    protected DualTerrain(Terrain terrain1, Terrain terrain2, State state, Color color) {
        this.terrain1 = terrain1;
        this.terrain2 = terrain2;
        this.state = state;
        this.color = color;
    }
    /**
     * Will create a proxy with the terrain replacing the currently active
     * terrain.
     */
    public Terrain proxy(Terrain terrain) {
        if (state.isOn()) {
            return new DualTerrain(terrain, terrain2, state, color);
        }
        return new DualTerrain(terrain1, terrain, state, color);
    }
    public boolean is(int flag) {
        return (state.isOn()) ? terrain1.is(flag) : terrain2.is(flag);
    }
    public boolean not(int flag) {
        return (state.isOn()) ? terrain1.not(flag) : terrain2.not(flag);
    }
    public Color getColor() {
        return color;
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        // First, trigger the current cell, then flip them
        DualTerrain t = new DualTerrain(terrain2, terrain1, state, color);
        cell.setTerrain( Registry.get().cache(t) );
        // This will turn off an embedded piece with a state string like "on/off"
        // as well...  
        // TerrainUtils.toggleCellState(cell, state);
        // The board then handles examining the currently visible piece
        // as a separate step
    }
    public Terrain getProxiedTerrain() {
        return (state.isOn()) ? terrain1 : terrain2;
    }
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        if (state.isOn()) {
            return terrain1.canEnter(agent, cell, direction);
        }
        return terrain2.canEnter(agent, cell, direction);
    }
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        if (state.isOn()) {
            return terrain1.canExit(agent, cell, direction);
        }
        return terrain2.canExit(agent, cell, direction);
    }
    public void onDrop(Event event, Cell cell, Item item) {
        if (state.isOn()) {
            terrain1.onDrop(event, cell, item);
        } else {
            terrain2.onDrop(event, cell, item);
        }
    }
    public void onPickup(Event event, Cell loc, Agent agent, Item item) {
        if (state.isOn()) {
            terrain1.onPickup(event, loc, agent, item);
        } else {
            terrain2.onPickup(event, loc, agent, item);
        }
    }
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (state.isOn()) {
            terrain1.onEnter(event, player, cell, dir);
        } else {
            terrain2.onEnter(event, player, cell, dir);
        }   
    }
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (state.isOn()) {
            terrain1.onExit(event, player, cell, dir);
        } else {
            terrain2.onExit(event, player, cell, dir);
        }
    }
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (state.isOn()) {
            terrain1.onAgentEnter(event, agent, cell, dir);
        } else {
            terrain2.onAgentEnter(event, agent, cell, dir);
        }
    }
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        if (state.isOn()) {
            terrain1.onAgentExit(event, agent, cell, dir);
        } else {
            terrain2.onAgentExit(event, agent, cell, dir);
        }
    }
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (state.isOn()) {
            terrain1.onFlyOver(event, cell, flier);
        } else {
            terrain2.onFlyOver(event, cell, flier);
        }
    }
    public void onAdjacentTo(Context context, Cell cell) {
        if (state.isOn()) {
            terrain1.onAdjacentTo(context, cell);
        } else {
            terrain2.onAdjacentTo(context, cell);
        }
    }
    public void onNotAdjacentTo(Context context, Cell cell) {
        if (state.isOn()) {
            terrain1.onNotAdjacentTo(context, cell);
        } else {
            terrain2.onNotAdjacentTo(context, cell);
        }
    }
    public String getName() {
        if (state.isOn()) {
            return terrain1.getName();
        }
        return terrain2.getName();
    }
    public Symbol getSymbol() {
        if (state.isOn()) {
            return terrain1.getSymbol();
        }
        return terrain2.getSymbol();
    }
    /** Type serializer. */
    public static final Serializer<DualTerrain> SERIALIZER = new BaseSerializer<DualTerrain>() {
        public DualTerrain create(String[] args) {
            return new DualTerrain(unescTerrain(args[1]), unescTerrain(args[2]),
                State.byName(args[3]), Color.byName(args[4]));
        }
        public DualTerrain example() {
            return new DualTerrain(new Wall(), new Wall(), State.ON, Color.STEELBLUE);
        }
        public String store(DualTerrain dt) {
            return Util.format("DualTerrain|{0}|{1}|{2}|{3}",
                esc(dt.terrain1), esc(dt.terrain2), dt.state.getName(), dt.color.getName());
        }
        public String template(String type) {
            return "DualTerrain|{terrain}|{terrain}|{state}|{color}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
