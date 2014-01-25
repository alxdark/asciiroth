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
import static us.asciiroth.client.core.Color.TAN;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Dirt. Has no special properties. Used for paths mostly.
 *
 */
public class Dirt extends AbstractTerrain {

    /** Constructor. */
    public Dirt() {
        super("Dirt", (TRAVERSABLE | PENETRABLE), new Symbol("&emsp;", NONE, TAN));
    }
    /** Type serializer. */
    public static final Serializer<Dirt> SERIALIZER = new TypeOnlySerializer<Dirt>() {
        public Dirt create(String[] args) {
            return new Dirt();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
