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

import static us.asciiroth.client.core.Color.DARKOLIVEGREEN;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * An saguaro-style cactus tree. Player cannot pass through cactii.
 */
public class Cactus extends Tree {
    /** Constructor. */
    private Cactus() {
        super("Cactus", new Symbol("&psi;", DARKOLIVEGREEN));
    }
    /** Type serializer. */
    public static final Serializer<Cactus> SERIALIZER = new TypeOnlySerializer<Cactus>() {
        public Cactus create(String[] args) {
            return new Cactus();
        }
        public String getTag() {
            return "Trees";
        }
    };
    
}
