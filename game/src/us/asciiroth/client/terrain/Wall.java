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

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A wall. Cannot be traversed in any way, ever.
 *
 */
public class Wall extends AbstractTerrain {
    
    /** Constructor. This constructor is public so that it can be 
     * used to assembly decorator examples for the map editor.
     */
    public Wall() {
        super("Wall", 0, new Symbol("&emsp;", NONE, NEARBLACK, NONE, BUILDING_WALL));
    }
    /** Type serializer. */
    public static final Serializer<Wall> SERIALIZER = new TypeOnlySerializer<Wall>() {
        public Wall create(String[] args) {
            return new Wall();
        }
        public String getTag() {
            return "Terrain";
        }
    };
}
