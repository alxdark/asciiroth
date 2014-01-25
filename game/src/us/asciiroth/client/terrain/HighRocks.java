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

import static us.asciiroth.client.core.Color.HIGH_ROCKS;
import static us.asciiroth.client.core.Color.LOW_ROCKS;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.effects.InFlightItem;

/**
 * High rocks can also be traversed, but nothing can fly over them. 
 *
 */
public class HighRocks extends AbstractTerrain {

    /** Constructor. */
    private HighRocks() {
        super("High Rocks", (TRAVERSABLE | PENETRABLE), new Symbol("^", HIGH_ROCKS, LOW_ROCKS));
    }
    /**
     * Although the player can pass over this terrain, he or she cannot
     * throw or shoot through it. 
     */
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        event.cancel();
    }
    /** Type serializer. */
    public static final Serializer<HighRocks> SERIALIZER = new TypeOnlySerializer<HighRocks>() {
        public HighRocks create(String[] args) {
            return new HighRocks();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
