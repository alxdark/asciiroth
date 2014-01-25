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
package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * When wielded by the player, the mirror shield reflects paralyzing ammunition 
 * like <code>Parabullet</code>s and <code>Stoneray</code>s (a kind of 
 * ammunition that instantly turns the target into a statue).
 */
public class MirrorShield extends AbstractItem {

    /** Constructor. */
    public MirrorShield() {
        super("The Mirror Shield", 0, 
            new Symbol("&oslash;", WHITE, null, BLACK, null));
    }
    /** Type serializer. */
    public static final Serializer<MirrorShield> SERIALIZER = new TypeOnlySerializer<MirrorShield>() {
        public MirrorShield create(String[] args) {
            return new MirrorShield();
        }
        public String getTag() {
            return "Artifacts";
        }
    };
}
