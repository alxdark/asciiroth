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

import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.OCEAN;
import static us.asciiroth.client.core.Flags.AQUATIC;
import static us.asciiroth.client.core.Flags.WATER_RESISTANT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.BlueRing;

/** Ocean. */
public class Ocean extends AbstractTerrain {
    
    /** Constructor. */
    protected Ocean() {
        super("Ocean", AQUATIC, new Symbol("&emsp;", NONE, OCEAN));
    }
    
    protected Ocean(Symbol symbol) {
        super("Ocean", AQUATIC, symbol);
    }
    
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        Item item = player.getBag().getSelected();
        if (item instanceof BlueRing) {
            Events.get().fireMessage(cell, 
                "You can breathe underwater with the ring on, but the cold, the dark and the pressure are too much to continue");
        } else if (player.is(WATER_RESISTANT)){
            Events.get().fireMessage(cell, 
                "The cold, the dark, and the pressure are too much for you.");
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Ocean> SERIALIZER = new TypeOnlySerializer<Ocean>() {
        public Ocean create(String[] args) {
            return new Ocean();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
