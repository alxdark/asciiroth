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

import static us.asciiroth.client.core.Color.GHOSTWHITE;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * White, fluffy, rectangular clouds you can walk on. If you drop an item
 * on a cloud, it'll fall right through and be lost.
 *
 */
public class Cloud extends AbstractTerrain {
    private Cloud() {
        super("Cloud", (PENETRABLE | TRAVERSABLE), new Symbol("&emsp;", NONE, GHOSTWHITE));
    }
    @Override
    public void onDrop(Event event, Cell cell, Item item) {
        event.cancel("The "+item.getName()+" falls through the clouds...");
    }
    /** Type serializer. */
    public static final Serializer<Cloud> SERIALIZER = new TypeOnlySerializer<Cloud>() {
        public Cloud create(String[] args) {
            return new Cloud();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
