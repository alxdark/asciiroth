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
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Key;

/**
 * A box that can only be opened with a key of the appropriate color.
 * However, I would not make the key white, or the chest will be 
 * indistinguishable from a crate, which just has to be smashed 
 * open.
 * 
 */
public class Chest extends AbstractTerrain {

    protected final Item item;
    protected final int count;
    
    protected Chest(Item item, int count, Color color) {
        super(color.getName() + " Chest", PENETRABLE, color, 
            new Symbol("x", "&#8864;", color, null, color, BUILDING_FLOOR));
        this.item = item;
        this.count = count;
        if (color == NONE || color == null) {
            throw new RuntimeException("Chest must have a color");
        }
    }
    protected Chest(Item item, Color color) {
        this(item, 1, color);
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (cell.isBagEmpty()) {
            Item sel = player.getBag().getSelected();
            if (sel instanceof Key) {
                if (sel.getColor() == this.color) {
                    cell.openContainer("chest", item, count, EmptyChest.class);
                    player.getBag().remove(sel);
                } else {
                    Events.get().fireMessage(cell, "The key is not the right color");
                }
            } else {
                Events.get().fireMessage(cell, "A large locked chest blocks your way");
            }
        }
        super.onEnter(event, player, cell, dir);
    }
    /** Type serializer. */
    public static final Serializer<Chest> SERIALIZER = new BaseSerializer<Chest>() {
        public Chest create(String[] args) {
            if (args.length == 2) {
                return new Chest(null, Color.byName(args[1]));
            } else if (args.length == 3) {
                return new Chest(unescItem(args[1]), Color.byName(args[2]));
            }
            return new Chest(unescItem(args[1]), Integer.parseInt(args[2]), Color.byName(args[3]));
        }
        public Chest example() {
            return new Chest(null, Color.STEELBLUE);
        }
        public String store(Chest c) {
            return (c.item == null) ?
                "Chest|" + c.color.getName() :
                Util.format("Chest|{0}|{1}|{2}", esc(c.item), Integer.toString(c.count), c.color.getName());
        }
        public String template(String type) {
            return "Chest|{item?}|{count?}|{color}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
