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

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.DARKSLATEGRAY;
import static us.asciiroth.client.core.Color.LIGHTSLATEGRAY;
import static us.asciiroth.client.core.Color.NEARBLACK;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Rock;

/**
 * When the player walks into an exchanger, the item the player is holding is 
 * exchanged with the piece on the other side of the exchanger. It's 
 * placement is important; it should be placed with wall pieces so there is 
 * no way around it, otherwise the exchange won't mean very much. 
 *
 */
public class Exchanger extends AbstractTerrain {

    /** Constructor. */
    private Exchanger() {
        super("Exchanger", 0, 
            new Symbol("&#247;", LIGHTSLATEGRAY, NEARBLACK, DARKSLATEGRAY, BUILDING_WALL));
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        if (!dir.isDiagonal()) {
            Cell otherSide = cell.getAdjacentCell(dir);
            Item otherItem = (Item)otherSide.getBag().last();
            Item selected = player.getBag().getSelected();
            if (selected instanceof Rock) {
                Events.get().fireMessage(cell, "The exchanger does not accept rocks");
            } else if (selected != PlayerBag.EMPTY_HANDED && otherItem != null) {
                otherSide.getBag().remove(otherItem);
                otherSide.getBag().add(selected);
                player.getBag().exchange(otherItem);
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Exchanger> SERIALIZER = new TypeOnlySerializer<Exchanger>() {
        public Exchanger create(String[] args) {
            return new Exchanger();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
