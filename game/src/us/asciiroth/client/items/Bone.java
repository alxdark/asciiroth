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
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.MEAT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

import com.google.gwt.user.client.Random;

/**
 * A bone. Many agents (bone eaters) would rather chase after bones than 
 * chase after the player, so they can be used to distract agents, until 
 * they are eaten up, that is. 
 *
 */
public class Bone extends AbstractItem {
    /**
     * Constructor.
     */
    public Bone() {
        super("Bone", MEAT, new Symbol("&iota;", WHITE, null, BLACK, null));
    }
    @Override
    public void onSteppedOn(Event event, Cell agentLoc, Agent agent) {
        if (agent.is(CARNIVORE)) {
            if (Random.nextInt(100) < 5) {
                agentLoc.getBag().remove(this);
            }
        }
    }
    /** Type serializer. */
    public static Serializer<Bone> SERIALIZER = new TypeOnlySerializer<Bone>() {
        public Bone create(String[] args) {
            return new Bone();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
