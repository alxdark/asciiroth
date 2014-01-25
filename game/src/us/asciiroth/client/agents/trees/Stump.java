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
package us.asciiroth.client.agents.trees;

import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Color.BURNTWOOD;
import static us.asciiroth.client.core.Color.SADDLEBROWN;
import us.asciiroth.client.agents.ImmobileAgent;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * When trees get set on fire they turn into stumps. They still block 
 * movement but they're no longer considered to be trees.
 */
public class Stump extends ImmobileAgent {

    public Stump() {
        super("Stump", (ORGANIC | PENETRABLE), 
            new Symbol("&#9576;", SADDLEBROWN, null, BURNTWOOD, null));
    }

    /** Type serializer. */
    public static final Serializer<Stump> SERIALIZER = new TypeOnlySerializer<Stump>() {
        public Stump create(String[] args) {
            return new Stump();
        }
        public String getTag() {
            return "Trees";
        }
    };
}
