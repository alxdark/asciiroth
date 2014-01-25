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
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Chalk converts the floor piece to the chalked floor piece. Players can use it
 * to mark the board to solve mazes, etc. It might also have a use along the lines
 * of "Elbereth" at some point.
 *
 */
public class ChalkedFloor extends AbstractTerrain {

    /** Constructor. */
    public ChalkedFloor() {
        super("Chalked Floor", (PENETRABLE | TRAVERSABLE),
            new Symbol("&#183;", WHITE, BLACK, BLACK, BUILDING_FLOOR));
    }
    /** Type serializer. */
    public static final Serializer<ChalkedFloor> SERIALIZER = new TypeOnlySerializer<ChalkedFloor>() {
        public ChalkedFloor create(String[] args) {
            return new ChalkedFloor();
        }
        public String getTag() {
            return "Terrain";
        }
    };
}
