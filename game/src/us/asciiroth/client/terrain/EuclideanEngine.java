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
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellFilter;
import us.asciiroth.client.board.CellVisitor;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.EmptyHanded;
import us.asciiroth.client.items.EuclideanShard;

/**
 * When activated, fires a color event. This is intended to be tied in to the 
 * use of the Euclidean Transporter, a difficult-to-activate piece that is 
 * the endgame for the TOA scenario.
 */
public class EuclideanEngine extends AbstractTerrain implements Animated {
    
    private final State state;
    private Symbol[] SYMBOLS; // not created until activated
    
    /**
     * Constructor.
     * @param color
     * @param state
     */
    private EuclideanEngine(Color color, State state) {
        // It's really annoying you have to create and toss a symbol here
        super(color.getName() + " Euclidean Engine", 0, color, 
            new Symbol("&loz;", color, null, color, BUILDING_FLOOR));
        this.state = state;
        if (state.isOn()) {
            SYMBOLS = new Symbol[] {
                new Symbol("&loz;", color, null, color, BUILDING_FLOOR),
                new Symbol("&loz;", BLACK, color, BUILDING_FLOOR, color),
                new Symbol("&diams;", color, null, color, BUILDING_FLOOR)
            };
        }
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        if (state.isOff()) {
            Item item = player.getBag().getSelected();
            if (item instanceof EuclideanShard) {
                if (item.getColor() == color) {
                    player.getBag().remove(item);
                    TerrainUtils.toggleCellState(cell, this, state);
                    powerEuclideanTransporter(event, cell);
                } else {
                    Events.get().fireMessage(cell, "The shard is not the right color for this engine");
                }
            } else if (!(item instanceof EmptyHanded)) {
                Events.get().fireMessage(cell, "It isn't working, and the " + item.getName() + " doesn't seem to help");
            }
        }
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOn()) {
            Events.get().fireRerender(cell, this, SYMBOLS[frame%3]);
        }
    }
    
    private void powerEuclideanTransporter(Event event, Cell lastActivated) {
        EuclideanEngineVisitor visitor = new EuclideanEngineVisitor();
        event.getBoard().visit(visitor);
        if (visitor.count == 4) {
            Cell cell = event.getBoard().find(new CellFilter() {
                public boolean matches(Cell cell, Direction from) {
                    return (cell.getApparentTerrain() instanceof EuclideanTransporter);
                }
            });
            Events.get().fireMessage(lastActivated, 
                "As the last engine starts, the Euclidean Transporter also powers up.");
            TerrainUtils.toggleCellState(cell, cell.getTerrain(), State.OFF);
            // TerrainUtils.toggleCellState(cell, this, State.OFF);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<EuclideanEngine> SERIALIZER = new Serializer<EuclideanEngine>() {
        public EuclideanEngine create(String[] args) {
            return new EuclideanEngine(Color.byName(args[1]), State.byName(args[2]));
        }
        public EuclideanEngine example() {
            return new EuclideanEngine(Color.STEELBLUE, State.OFF);
        }
        public String store(EuclideanEngine ee) {
            return Util.format("EuclideanEngine|{0}|{1}", ee.color.getName(), ee.state.getName());
        }
        public String template(String type) {
            return "EuclideanEngine|{color}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
    
    private class EuclideanEngineVisitor implements CellVisitor {
        public int count;
        public boolean visit(Cell cell, int range) {
            if (cell.getApparentTerrain() instanceof EuclideanEngine) {
                EuclideanEngine ee = (EuclideanEngine)cell.getApparentTerrain();
                if (ee.state == State.ON) {
                    count++;
                }
            }
            return false;
        }
    }
}
