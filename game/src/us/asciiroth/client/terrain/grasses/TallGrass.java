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

import static us.asciiroth.client.core.Color.BUSHES;
import static us.asciiroth.client.core.Color.GRASS;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Tall grass (decorative).
 *
 */
public class TallGrass extends Grass {
    /** Constructor. */
    protected TallGrass() {
        super("Tall Grass", new Symbol("&#8230;", BUSHES, GRASS));
    }
    /** Type serializer. */
    public final static Serializer<TallGrass> SERIALIZER = new TypeOnlySerializer<TallGrass>() {
        public TallGrass create(String[] args) {
            return new TallGrass();
        }
        public String getTag() {
            return "Grasses";
        }
    };
}
