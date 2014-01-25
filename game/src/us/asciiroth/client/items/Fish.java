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

import static us.asciiroth.client.core.Color.DARKKHAKI;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.MEAT;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

import com.google.gwt.user.client.Random;

/**
 * A fish. You can eat it and it will heal you, or throw it and 
 * it will attract monsters, who will eat it until it turns into a 
 * bone, then eat the bone, etc.  
 */
public class Fish extends AbstractItem {

    Fish() {
        super("Fish", MEAT, new Symbol("&alpha;", "&prop;", DARKKHAKI));
    }
    @Override
    public void onUse(Event event) {
        event.getPlayer().heal(event, this, 25);
    }
    @Override
    public void onSteppedOn(Event event, Cell agentLoc, Agent agent) {
        if (agent.is(CARNIVORE)) {
            if (Random.nextInt(100) < 15) {
                agentLoc.getBag().remove(this);
                agentLoc.getBag().add((Bone)Registry.get().getPiece(Bone.class));
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Fish> SERIALIZER = new TypeOnlySerializer<Fish>() {
        public Fish create(String[] args) {
            return new Fish();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
