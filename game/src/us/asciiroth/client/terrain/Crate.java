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
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Crowbar;

/**
 * A large box that can be broken open with a large hammer, to reveal an item. 
 * This essentially hides items on the board.
 * 
 */
public class Crate extends AbstractTerrain {

    protected final Item item;
    protected final int count;
    
    public Crate(Item item) {
        this(item, 1);
    }
    
    public Crate(Item item, int count) {
        super("Crate", PENETRABLE, 
            new Symbol("x", "&#8864;", WHITE, null, BLACK, BUILDING_FLOOR));
        this.item = item;
        this.count = count;
    }
    
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (cell.isBagEmpty()) {
            Item sel = player.getBag().getSelected();
            if (sel instanceof Crowbar) {
                cell.openContainer("crate", item, count, Boards.class);
            } else {
                Events.get().fireMessage(cell, 
                    "A large crate. It can't be moved, but try prying it open");
            }
        }
        super.onEnter(event, player, cell, dir);
    }
    
    /** Type serializer. */
    public static final Serializer<Crate> SERIALIZER = new BaseSerializer<Crate>() {
        public Crate create(String[] args) {
            if (args.length == 1) {
                return new Crate(null);
            } else if (args.length == 2) {
                return new Crate(unescItem(args[1]));
            }
            return new Crate(unescItem(args[1]), Integer.parseInt(args[2]));
        }
        public Crate example() {
            return new Crate(null);
        }
        public String store(Crate c) {
            return (c.item == null) ? 
                "Crate" : Util.format("Crate|{0}|{1}", esc(c.item), Integer.toString(c.count)); 
        }
        public String template(String type) {
            return "Crate|{item}|{count?}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
