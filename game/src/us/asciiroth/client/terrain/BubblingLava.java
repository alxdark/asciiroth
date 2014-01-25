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

import static us.asciiroth.client.core.Color.LAVA;
import static us.asciiroth.client.core.Color.LIGHTPINK;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.LAVITIC;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellFilter;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/** 
 * A decorative terrain to spice up lava, that hopefully makes it look
 * like the lava is "spouting". 
 * 
 */
public class BubblingLava extends AbstractTerrain implements Animated {
    
    private Lava lava;
    private CellFilter filter = new CellFilter() {
        public boolean matches(Cell cell, Direction dir) {
            // Use real terrain, or we could destroy a trigger
            return (cell.getTerrain() == lava);
        }
    };
    
    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol(".", LIGHTPINK, LAVA),
        new Symbol(":", LIGHTPINK, LAVA),
        new Symbol("&#39;", LIGHTPINK, LAVA),
        new Symbol("&#957;", LIGHTPINK, LAVA),
        new Symbol("&#8756;", LIGHTPINK, LAVA),
        new Symbol("&#8901;", LIGHTPINK, LAVA),
        new Symbol(".", LIGHTPINK, LAVA),
        new Symbol("&emsp;", NONE, LAVA)
    };
    
    /** Constructor. */
    private BubblingLava() {
        super("Lava", LAVITIC, SYMBOLS[0]);
        this.lava = (Lava)Registry.get().getPiece(Lava.class);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%4==0) { // 4, 8, 12, 16, 20, 24, 28, 32, 36
            int t = (frame%36)/4;
            if (t < 8) {
                Events.get().fireRerender(cell, this, SYMBOLS[t]);
            } else {
                Cell to = findAdjacentLava(cell);
                to = findAdjacentLava(to);
                if (to != null) {
                    cell.setTerrain(lava);
                    to.setTerrain(this);
                }
            }
        }
    }
    
    private Cell findAdjacentLava(Cell cell) {
        if (cell != null) {
            return TerrainUtils.getRandomCell(cell.getAdjacentCells(filter));
        }
        return null;
    }
    
    /** Type serializer. */
    public static final Serializer<BubblingLava> SERIALIZER = new TypeOnlySerializer<BubblingLava>() {
        public BubblingLava create(String[] args) {
            return new BubblingLava();
        }
        public String getTag() {
            return "Terrain";
        }
    };
}
