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
package us.asciiroth.client.terrain.grasses;

import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.terrain.AbstractTerrain;

/**
 * Grass that you can walk on. There are various grasses for decorative 
 * purposes but all are simple passable terrains.
 *
 */
public class Grass extends AbstractTerrain {
    
    /**
     * Constructor.
     * @param name
     * @param symbol
     */
    Grass(String name, Symbol symbol) {
        super(name, (TRAVERSABLE | PENETRABLE), symbol);
    }
    /** Constructor. */
    public Grass() {
        super("Grass", (TRAVERSABLE | PENETRABLE), new Symbol("&emsp;", NONE, Color.GRASS));
    }
    /** Type serializer. */
    public static final Serializer<Grass> SERIALIZER = new TypeOnlySerializer<Grass>() {
        public Grass create(String[] args) {
            return new Grass();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
