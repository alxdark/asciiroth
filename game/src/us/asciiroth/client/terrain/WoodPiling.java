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
import static us.asciiroth.client.core.Color.WOOD_PILING;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A wood piling, basically the thing that holds up a pier. Seems a little
 * too specialized, perhaps can be "Wood" or something.
 *
 */
public class WoodPiling extends AbstractTerrain {
    /** Constructor. */
    private WoodPiling() {
        super("Wood Piling", 0, new Symbol("&emsp;", NONE, WOOD_PILING));
    }
    /** Type serializer. */
    public static final Serializer<WoodPiling> SERIALIZER = new TypeOnlySerializer<WoodPiling>() {
        public WoodPiling create(String[] args) {
            return new WoodPiling();
        }
        public String getTag() {
            return "Terrain";
        }
    };
}
