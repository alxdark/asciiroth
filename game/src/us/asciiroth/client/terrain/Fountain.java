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

import static us.asciiroth.client.core.Color.OCEAN;
import static us.asciiroth.client.core.Color.SURF;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * A decorative piece that looks like a fountain spouting water. It does block
 * movement across it, however.
 *
 */
public class Fountain extends AbstractTerrain implements Animated {

    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&#183;", SURF, OCEAN),
        new Symbol("&#8226;", SURF, OCEAN),
        new Symbol("&#111;", SURF, OCEAN),
        new Symbol("&#79;", SURF, OCEAN),
        new Symbol("&#169;", SURF, OCEAN)
    };
    
    private Fountain() {
        super("Fountain", 0, SYMBOLS[4]);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%3==0) { // So it's 3, 6, 9, 12, 15
            Events.get().fireRerender(cell, this, SYMBOLS[(frame %15)/3]);    
        }
    }
    /** Type serializer. */
    public static final Serializer<Fountain> SERIALIZER = new TypeOnlySerializer<Fountain>() {
        public Fountain create(String[] args) {
            return new Fountain();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
