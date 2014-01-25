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

import static us.asciiroth.client.core.Flags.PUSHABLE;
import static us.asciiroth.client.core.Color.DARKGOLDENROD;
import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A large rock that blocks movement onto a square. It can be pushed by the 
 * player, and will fill pits, Sudoko-style. 
 */
public class Boulder extends AbstractAgent {

    /** Constructor. */
    private Boulder() {
        super("Boulder", PUSHABLE, NONE, CombatStats.INDESTRUCTIBLE, new Symbol("O", DARKGOLDENROD));
    }
    /** Type serializer. */
    public static final Serializer<Boulder> SERIALIZER = new TypeOnlySerializer<Boulder>() {
        public Boulder create(String[] args) {
            return new Boulder();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
