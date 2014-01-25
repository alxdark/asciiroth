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

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.items.Bow;
import us.asciiroth.client.terrain.Wall;

/**
 * A utility terrain. When it receives a color event, it removes one instance of 
 * the indicated item from the player's inventory.  
 *
 */
public class Unequipper extends Decorator {

    private final Item item;

    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param item
     */
    public Unequipper(Terrain terrain, Color color, Item item) {
        super(terrain, 0, color, terrain.getSymbol());
        this.item = item;
    }
    public Terrain proxy(Terrain terrain) {
        return new Unequipper(terrain, color, item);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        ctx.getPlayer().getBag().remove(item);
    }
    /** Type serializer. */
    public static final Serializer<Unequipper> SERIALIZER = new BaseSerializer<Unequipper>() {
        public Unequipper create(String[] args) {
            return new Unequipper(unescTerrain(args[1]), Color.byName(args[2]), unescItem(args[3]));
        }
        public Unequipper example() {
            return new Unequipper(new Wall(), Color.STEELBLUE, new Bow());
        }
        public String store(Unequipper u) {
            return Util.format("Unequipper|{0}|{1}|{2}", esc(u.terrain), u.color.getName(), esc(u.item));
        }
        public String template(String type) {
            return "Unequipper|{terrain}|{color}|{item}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
