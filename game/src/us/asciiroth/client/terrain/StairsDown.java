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

/** Stairs down. */
public class StairsDown extends AbstractTerrain {
    
    /** Constructor. */
    private StairsDown() {
        super("Stairs Down", (TRAVERSABLE | PENETRABLE | VERTICAL), 
            new Symbol("&gt;", WHITE, null, BLACK, BUILDING_FLOOR));
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        Events.get().fireMessage(cell, "Use 'z' to go down");
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (dir == Direction.UP) {
            event.cancel();
        }
    }
    /** Type serializer. */
    public static final Serializer<StairsDown> SERIALIZER = new TypeOnlySerializer<StairsDown>() {
        public StairsDown create(String[] args) {
            return new StairsDown();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
