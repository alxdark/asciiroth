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
import static us.asciiroth.client.core.Color.BURLYWOOD;
import static us.asciiroth.client.core.Color.BURNTWOOD;
import static us.asciiroth.client.core.Color.LIMEGREEN;
import static us.asciiroth.client.core.Flags.DETECT_HIDDEN;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;

/**
 * A bookshelf. It blocks movement, but it can be searched for contents, 
 * such as scrolls. It shows blue if the player is has the detect 
 * hidden capability.
 */
public class Bookshelf extends AbstractTerrain {
    
    private static final Symbol MARKED = 
        new Symbol("&equiv;", "&#8803;", LIMEGREEN, BLACK, LIMEGREEN, BUILDING_FLOOR);
    private final Item item;
    
    /**
     * Constructor.
     */
    public Bookshelf(Item item) {
        super("Bookshelf", 0, new Symbol("&equiv;", "&#8803;", BURLYWOOD, BLACK, BURNTWOOD, BUILDING_FLOOR));
        this.item = item;
        
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal()) {
            event.cancel("It's a little too far to reach.");
            return;
        } else {
            super.onEnter(event, player, cell, dir);
        }
        if (item != null) {
            Events.get().fireModalMessage(item.getIndefiniteNoun("Searching the bookshelf, you find {0}"));
            player.getBag().add(item);
            cell.setTerrain((Terrain)Registry.get().getPiece("Bookshelf"));
        } else {
            Events.get().fireMessage(cell, "You find nothing on the bookshelf.");
        }
    }
    @Override
    public void onAdjacentTo(Context context, Cell cell) {
        if (item != null && context.getPlayer().is(DETECT_HIDDEN)) {
            Events.get().fireRerender(cell, this, MARKED);
        }
    }
    @Override
    public void onNotAdjacentTo(Context context, Cell cell) {
        Events.get().fireRerender(cell, this, symbol);
    }
    /** Type serializer. */
    public static final Serializer<Bookshelf> SERIALIZER = new BaseSerializer<Bookshelf>() {
        public Bookshelf create(String[] args) {
            return (args.length == 2) ?
                new Bookshelf(unescItem(args[1])) : new Bookshelf(null);
        }
        public String getTag() {
            return "Room Features";
        }
        public Bookshelf example() {
            return new Bookshelf(null);
        }
        public String store(Bookshelf b) {
            return (b.item != null) ? "Bookshelf|"+esc(b.item) : "Bookshelf";
        }
        public String template(String type) {
            return "Bookshelf|{item?}";
        }
    };
}
