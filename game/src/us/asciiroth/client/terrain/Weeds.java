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
import static us.asciiroth.client.core.Color.TAN;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Weeds. Decorative.
 *
 */
public class Weeds extends AbstractTerrain {

    /** Constructor. */
    public Weeds() {
        super("Weeds", (TRAVERSABLE | PENETRABLE), new Symbol("&#8230;", GRASS, TAN));
    }
    /** Type serializer. */
    public static final Serializer<Weeds> SERIALIZER = new TypeOnlySerializer<Weeds>() {
        public Weeds create(String[] args) {
            return new Weeds();
        }
        public String getTag() {
            return "Grasses";
        }
    };
}
