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
import static us.asciiroth.client.core.Color.WAVES;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/** 
 * A waterfall. This type of water is not passable, even with the blue ring. 
 * 
 */
public class Waterfall extends AbstractTerrain implements Animated {
    
    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("&uml;", SURF, OCEAN),
        new Symbol("&uml;", WAVES, OCEAN),
        // new Symbol("&sim;", SURF, OCEAN),
        // new Symbol("&asymp;", WAVES, OCEAN),
        new Symbol("&asymp;", SURF, OCEAN),
        new Symbol("&hellip;", SURF, OCEAN),
        new Symbol("&hellip;", WAVES, OCEAN)
    };
    
    /** Constructor. */
    private Waterfall() {
        super("Waterfall", 0, SYMBOLS[0]);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%3==0) { // So it's 3, 6, 9, 12, 15, 18
            Events.get().fireRerender(cell, this, SYMBOLS[(frame%15)/3]);    
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Waterfall> SERIALIZER = new TypeOnlySerializer<Waterfall>() {
        public Waterfall create(String[] args) {
            return new Waterfall();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
