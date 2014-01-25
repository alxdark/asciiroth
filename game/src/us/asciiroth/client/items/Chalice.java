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
package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.GOLD;
import static us.asciiroth.client.core.Color.GREEN;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Color.YELLOW;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * An artifact from the Atari 2600 game "Adventure". The chalice has
 * no unique capabilities, it is purely an object that is suitable 
 * as a goal for a quest. 
 */
public class Chalice extends AbstractItem implements Animated {

    private static final Symbol[] SYMBOLS = {
        new Symbol("Y", GOLD),
        new Symbol("Y", RED),
        new Symbol("Y", YELLOW, null, BLACK, null),
        new Symbol("Y", GREEN),
        new Symbol("Y", WHITE)
    };
    /** Constructor. */
    public Chalice() {
        super("The Chalice", 0, SYMBOLS[0]);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Events.get().fireRerender(cell, this, SYMBOLS[frame%5]);
    }
    /** Type serializer. */
    public static final Serializer<Chalice> SERIALIZER = new TypeOnlySerializer<Chalice>() {
        public Chalice create(String[] args) {
            return new Chalice();
        }
        public String getTag() {
            return "Artifacts";
        }
    };
}
