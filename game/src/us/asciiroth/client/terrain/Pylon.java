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
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Crystal;
import us.asciiroth.client.items.Key;
import us.asciiroth.client.ui.Log;

/**
 * A pylon is a teleporter that must be activated with a crystal of the
 * same color. The design for this is that you are teleported onto a 
 * pylon of the same color, which can be used to return. Since the other 
 * pylon may be off (perhaps you can activate the pair from either side), 
 * a pylon will activate when the player leaves it. So one crystal, and 
 * one transport, and then both pylons will be activated. Of course, you
 * don't have to pair them this way.
 */
public class Pylon extends AbstractTerrain implements Animated {
    
    protected final State state;
    protected final String boardID;
    protected final int x;
    protected final int y;
    
    /**
     * Constructor.
     * @param color
     * @param state
     */
    protected Pylon(Color color, State state, String boardID, int x, int y) {
        super(color.getName() + " Pylon", 0, color, 
            new Symbol("&Delta;", "&and;", color, null, color, BUILDING_FLOOR));
        this.state = state;
        this.boardID = boardID;
        this.x = x;
        this.y = y;
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (state.isOff()) {
            event.cancel();
            PlayerBag bag = player.getBag();
            Item item = bag.getSelected();
            if (item instanceof Key && item.getColor() == color) {
                event.cancel("Pylons cannot be activated with keys.");
            } else if (item instanceof Crystal && item.getColor() == color) {
                bag.remove(item);
                TerrainUtils.toggleCellState(cell, this, state);
            } else {
                event.cancel("The pylon must be activated.");
            }
        } else if (dir != null){
            // dir is null when landing on the pylon, which is when we don't
            // want to teleport, of course.
            player.teleport(event, dir, boardID, x, y);
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (state.isOff()) {
            Terrain other = TerrainUtils.getTerrainOtherState(cell.getTerrain(), State.OFF);
            cell.setTerrain(other, false);
            Log.get().debug(SERIALIZER.store(this));
        }
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOn()) {
            boolean outside = ctx.getBoard().isOutside();
            Color bg = cell.getTerrain().getSymbol().getBackground(outside);
            Color nfg = Util.oscillate(color, null, 15, frame);
            Color nbg = Util.oscillate(bg, color, 15, frame);
            Symbol s = new Symbol(symbol.getAdjustedEntity(), nfg, nbg);
            Events.get().fireRerender(cell, this, s);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Pylon> SERIALIZER = new Serializer<Pylon>() {
        public Pylon create(String[] args) {
            return new Pylon(Color.byName(args[1]), State.byName(args[2]), args[3], Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        }
        public Pylon example() {
            return new Pylon(Color.STEELBLUE, State.OFF, "", 0, 0);
        }
        public String store(Pylon p) {
            return Util.format("Pylon|{0}|{1}|{2}|{3}|{4}", p.color.getName(), p.state.getName(),
                p.boardID, Integer.toString(p.x), Integer.toString(p.y));
        }
        public String template(String type) {
            return "Pylon|{color}|{state}|{boardID}|{x}|{y}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
