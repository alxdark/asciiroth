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
import static us.asciiroth.client.core.Color.SURF;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Water an agent can walk through. It does not subclass <code>WaterBase</code>
 * because it is inherently different in this respect.
 *
 */
public class ShallowWater extends AbstractTerrain {
    
    ShallowWater(String name) {
        super(name, (TRAVERSABLE | PENETRABLE), new Symbol("&emsp;", NONE, SURF));
    }
    /** Constructor. */
    public ShallowWater() {
        this("Shallow Water");
    }
    
    /** Type serializer. */
    public static final Serializer<ShallowWater> SERIALIZER = new TypeOnlySerializer<ShallowWater>() {
        public ShallowWater create(String[] args) {
            return new ShallowWater();
        }
        public String getTag() {
            return "Outside Terrain";
        }
};
}
