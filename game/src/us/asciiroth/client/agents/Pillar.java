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
package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.LESSNEARBLACK;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A pillar. Player cannot move through a pillar.
 *
 */
public class Pillar extends ImmobileAgent {
    
    /** Constructor. */
    private Pillar() {
        super("Pillar", 0, 
            new Symbol("&#182;", LESSNEARBLACK, null, BUILDING_WALL, BUILDING_FLOOR));
    }
    /** Type serializer. */
    public static final Serializer<Pillar> SERIALIZER = new TypeOnlySerializer<Pillar>() {
        public Pillar create(String[] args) {
            return new Pillar();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
