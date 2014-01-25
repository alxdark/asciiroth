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
import static us.asciiroth.client.core.Color.HIGH_ROCKS;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Cliffs that cannot be traversed by any kind of agent. These are a little
 * different from the cliff compound terrain type, which makes it impossible
 * to leave the cell except to another cell of the same terrain.
 *
 */
public class ImpassableCliffs extends AbstractTerrain {

    /** Constructor. */
    private ImpassableCliffs() {
        super("Impassable Cliffs", 0, new Symbol("&Delta;", "&and;", BLACK, HIGH_ROCKS));
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        // Just adding an explanatory message here.
        event.cancel(cell, "The rocks are too steep to climb here.");
    }
    /** Type serializer. */
    public static final Serializer<ImpassableCliffs> SERIALIZER = new TypeOnlySerializer<ImpassableCliffs>() {
        public ImpassableCliffs create(String[] args) {
            return new ImpassableCliffs();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
