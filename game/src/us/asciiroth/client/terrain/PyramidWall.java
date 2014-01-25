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
import static us.asciiroth.client.core.Color.STEELBLUE;
import us.asciiroth.client.Util;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * A wall that can be colored, just for an effect. Cannot be traversed in any way, ever.
 *
 */
public class PyramidWall extends AbstractTerrain {
    
    protected final Color color;
    
    /** Constructor. This constructor is public so that it can be 
     * used to assembly decorator examples for the map editor.
     */
    protected PyramidWall(Color color) {
        super("Wall", 0, new Symbol("&emsp;", NONE, color, NONE, color));
        this.color = color;
    }
    
    /** Type serializer. */
    public static final Serializer<PyramidWall> SERIALIZER = new Serializer<PyramidWall>() {
        public PyramidWall create(String[] args) {
            return new PyramidWall(Color.byName(args[1]));
        }
        public String store(PyramidWall d) {
            return Util.format("PyramidWall|{0}", d.color.getName());
        }
        public PyramidWall example() {
            return new PyramidWall(STEELBLUE);
        }
        public String template(String key) {
            return "PyramidWall|{color}";
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };    
}
