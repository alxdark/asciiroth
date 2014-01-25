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

import static us.asciiroth.client.core.Color.LOW_ROCKS;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.SILVER;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import static us.asciiroth.client.core.Flags.VERTICAL;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * A cave entrance, that acts like either up stairs or down stairs depending on whether
 * it is outside or inside (being a cave entrance, it must be the means to move between
 * an outside and an inside map).  
 * <p>
 * Cave entrance uses the color of low rocks when placed outside and is presumed to 
 * be something that you would place near rocks.
 *
 */
public class CaveEntrance extends AbstractTerrain {

    /** Constructor. */
    private CaveEntrance() {
        super("Cave Entrance", (PENETRABLE | TRAVERSABLE | VERTICAL),
            new Symbol("&#8745;", SILVER, null, NEARBLACK, LOW_ROCKS));
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        // Don't allow movement up/down that's not in the right direction given 
        // orientation of the cave.
        if ((event.getBoard().isOutside() && dir == Direction.UP) || 
           (!event.getBoard().isOutside() && dir == Direction.DOWN)) {
            event.cancel();
        }
    }
    @Override
    public void onEnter(final Event event, Player player, final Cell cell, final Direction dir) {
        if (event.getBoard().isOutside()) {
            Events.get().fireMessage(cell, "Use 'z' to enter the cave");    
        } else {
            Events.get().fireMessage(cell, "Use 'z' to exit the cave");
        }
    }
    /** Type serializer. */
    public static final Serializer<CaveEntrance> SERIALIZER = new TypeOnlySerializer<CaveEntrance>() {
        public CaveEntrance create(String[] args) {
            return new CaveEntrance();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
