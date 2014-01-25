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

import static us.asciiroth.client.core.Color.SADDLEBROWN;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A fishing pole can be used to catch fish if you stand at the water's edge
 * and move at a fish when it is next to the shore. There are a number of kinds
 * of fish, each with a special ability or utility to the player. 
 * 
 * @see us.asciiroth.client.terrain.FishPool
 *
 */
public class FishingPole extends AbstractItem {

    /** Constructor. */
    private FishingPole() {
        super("Fishing Pole", 0, new Symbol("&#383;", SADDLEBROWN));
    }

    /** Type serializer. */
    public static final Serializer<FishingPole> SERIALIZER = new TypeOnlySerializer<FishingPole>() {
        public FishingPole create(String[] args) {
            return new FishingPole();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
