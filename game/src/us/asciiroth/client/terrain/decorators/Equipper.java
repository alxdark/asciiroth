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
 * A utility terrain. When it receives a color event, it adds the indicated item 
 * to the player's inventory. (It doesn't matter if the player already has an 
 * item of this type). You do not have to use this piece to equip a player at the
 * beginning of a scenario; boards allow you to set starting items for a player if
 * he/she starts the game at that board. 
 *
 */
public class Equipper extends Decorator {

    private final Item item;

    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param item
     */
    public Equipper(Terrain terrain, Color color, Item item) {
        super(terrain, 0, color, terrain.getSymbol());
        this.item = item;
    }
    public Terrain proxy(Terrain terrain) {
        return new Equipper(terrain, color, item);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        ctx.getPlayer().getBag().add(item);
    }
    /** Type serializer. */
    public static final Serializer<Equipper> SERIALIZER = new BaseSerializer<Equipper>() {
        public Equipper create(String[] args) {
            return new Equipper(unescTerrain(args[1]), Color.byName(args[2]), unescItem(args[3]));
        }
        public Equipper example() {
            return new Equipper(new Wall(), Color.STEELBLUE, new Bow());
        }
        public String store(Equipper e) {
            return Util.format("Equipper|{0}|{1}|{2}", esc(e.terrain), e.color.getName(), esc(e.item));
        }
        public String template(String type) {
            return "Equipper|{terrain}|{color}|{item}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
