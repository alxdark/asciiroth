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

import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Shallow ocean water that the player can traverse.
 *
 */
public class Surf extends ShallowWater {
    
    /** Constructor. */
    private Surf() {
        super("Surf");
    }
    /** Type serializer. */
    public static final Serializer<Surf> SERIALIZER = new TypeOnlySerializer<Surf>() {
        public Surf create(String[] args) {
            return new Surf();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
