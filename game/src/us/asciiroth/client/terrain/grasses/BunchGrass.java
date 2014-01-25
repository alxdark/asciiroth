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

import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Bunch grass (decorative).
 *
 */
public class BunchGrass extends Grass {
    /** Constructor. */
    private BunchGrass() {
        super("Bunch Grass", new Symbol("&#8230;", Color.FOREST, Color.GRASS));
    }
    /** Type serializer. */
    public final static Serializer<BunchGrass> SERIALIZER = new TypeOnlySerializer<BunchGrass>() {
        public BunchGrass create(String[] args) {
            return new BunchGrass();
        }
        public String getTag() {
            return "Grasses";
        }
    };
}
