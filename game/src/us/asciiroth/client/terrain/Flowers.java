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

import static us.asciiroth.client.core.Color.GRASS;
import static us.asciiroth.client.core.Color.PINK;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * For my daughter, a purely decorative terrain indicating flowers. 
 * They can be created in many colors. Actually, they are kind of cool (but 
 * only vaguely resemble tulips... if I find a better symbol I'll use it). 
 *
 */
public class Flowers extends AbstractTerrain {

    private Flowers(Color color) {
        super("Flowers", (TRAVERSABLE | PENETRABLE), color, 
            new Symbol("&#970;", "&#10049;", color, GRASS));
    }
    /** Type serializer. */
    public static final Serializer<Flowers> SERIALIZER = new Serializer<Flowers>() {
        public Flowers create(String[] args) {
            return new Flowers(Color.byName(args[1]));
        }
        public Flowers example() {
            return new Flowers(PINK);
        }
        public String store(Flowers f) {
            return "Flowers|" + f.color.getName();
        }
        public String template(String type) {
            return "Flowers|{color}";
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
