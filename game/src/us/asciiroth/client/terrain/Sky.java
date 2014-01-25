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
import static us.asciiroth.client.core.Color.SKYBLUE;
import static us.asciiroth.client.core.Flags.ETHEREAL;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Blue sky. Items can be thrown across it but it is not traversable.
 *
 */
public class Sky extends AbstractTerrain {

    private Sky() {
        super("Sky", (ETHEREAL | PENETRABLE), 
            new Symbol("&emsp;", NONE, SKYBLUE));
    }
    /** Type serializer. */
    public static final Serializer<Sky> SERIALIZER = new TypeOnlySerializer<Sky>() {
        public Sky create(String[] args) {
            return new Sky();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };

}
