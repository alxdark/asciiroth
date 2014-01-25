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

import static us.asciiroth.client.core.Color.SURF;
import static us.asciiroth.client.core.Flags.AQUATIC;

import java.util.List;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellFilter;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Fish;
import us.asciiroth.client.items.FishingPole;

import com.google.gwt.user.client.Random;

/** 
 * Uncrossable water that can yield a fish, and that evolves the terrain
 * to move this capability around any nearby water. Must be a decorator to 
 * work for both water and ocean; implements the same behavior as <code>
 * LowImpassableTerrain</code>.  
 * 
 */
public class FishPool extends AbstractTerrain implements Animated {
    
    private final Terrain terrain;
    private final State state;
    private final Fish fish;

    private int chanceToGoOn = 20;
    private int chanceToGoOff = 40;
    private int chanceToReplenish = 80;
    
    /**
     * Constructor
     * @param terrain   the terrain this fishing spot mimics
     * @param state     the state of this instance
     */
    private FishPool(Terrain terrain, State state) {
        super(
            state.isOn() ?
                terrain.getName()+" with Fish" : 
                terrain.getName(),
            AQUATIC,
            state.isOn() ? 
                new Symbol("&alpha;", "&prop;", SURF, terrain.getSymbol().getBackground(false)) :
                new Symbol("&emsp;", SURF, terrain.getSymbol().getBackground(false))
        );
        this.terrain = terrain;
        this.state = state;
        this.fish = (Fish)Registry.get().getPiece(Fish.class);
    }

    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        Item item = player.getBag().getSelected();
        if (state.isOn() && item instanceof FishingPole) {
            catchFish(event, player, cell);
        }
        super.onEnter(event, player, cell, dir);
    }
    
    private void catchFish(Event event, Player player, Cell to) {
        player.getBag().add(fish);
        Events.get().fireMessage(to, "You caught a fish!");
        if ((Random.nextInt(100) < chanceToReplenish)) {
            turnOff(to);
        } else {
            to.setTerrain(terrain);
        }
    }
    public boolean randomSeed() {
    	return true;
    }
    /**
     * When off, the fish pool periodically tests to see if it should go 
     * "on". When on, after a period, it turns off and moves.
     */
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%8 != 0) {
            return;
        }
        boolean changeState = (state.isOn()) ?
            (Random.nextInt(100) < chanceToGoOff) :
            (Random.nextInt(100) < chanceToGoOn);
        if (!changeState) {
            return;
        }
        if (state.isOn()) {
            turnOff(cell);
        } else {
            turnOn(cell);
        }
    }
    
    private void turnOff(Cell cell) {
        // Turning off, the piece also moves.
        List<Cell> cells = cell.getAdjacentCells(new CellFilter() {
            public boolean matches(Cell cell, Direction dir) {
                // We want the real terrain here, not the perceived terrain, because FishPool
                // destroys the terrain when it turns off, and that would delete any decorator
                // on the terrain itself.
                return (cell.getTerrain() == terrain);
            }
        });
        Cell to = TerrainUtils.getRandomCell(cells);
        if (to != null) {
            // Turn this cell into water or ocean. 
            cell.setTerrain(terrain);
            
            Terrain off = TerrainUtils.getTerrainOtherState(this, state);
            to.setTerrain(off);
        }
    }
    
    private void turnOn(Cell cell) {
        TerrainUtils.toggleCellState(cell, this, state);
    }
    
    /** Type serializer. */
    public static final Serializer<FishPool> SERIALIZER = new BaseSerializer<FishPool>() {
        public FishPool create(String[] args) {
            return new FishPool(unescTerrain(args[1]), State.byName(args[2]));
        }
        public FishPool example() {
            return new FishPool(new Water(), State.ON);
        }
        public String store(FishPool fp) {
            return Util.format("FishPool|{0}|{1}", esc(fp.terrain), fp.state.getName());
        }
        public String template(String type) {
            return "FishPool|{terrain}|{state}";
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
