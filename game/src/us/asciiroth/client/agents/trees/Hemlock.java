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

import static us.asciiroth.client.core.Color.BUSHES;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A hemlock tree. Player cannot pass through trees.
 */
public class Hemlock extends Tree {
    /** Constructor. */
    private Hemlock() {
        super("Hemlock Tree", new Symbol("&#9824;", BUSHES));
    }
    /** Type serializer. */
    public static final Serializer<Hemlock> SERIALIZER = new TypeOnlySerializer<Hemlock>() {
        public Hemlock create(String[] args) {
            return new Hemlock();
        }
        public String getTag() {
            return "Trees";
        }
    };
    
}
