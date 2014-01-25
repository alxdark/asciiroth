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

import static us.asciiroth.client.core.Color.FOREST;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Forest.
 *
 */
public class Forest extends AbstractTerrain {
    /** Constructor. */
    private Forest() {
        super("Forest", (TRAVERSABLE | PENETRABLE), new Symbol("&emsp;", NONE, FOREST));
    }
    /** Type serializer. */
    public static final Serializer<Forest> SERIALIZER = new TypeOnlySerializer<Forest>() {
        public Forest create(String[] args) {
            return new Forest();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };

}
