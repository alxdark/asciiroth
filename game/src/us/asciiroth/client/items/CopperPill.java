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

import static us.asciiroth.client.core.Flags.PARALYSIS_RESISTANT;
import static us.asciiroth.client.core.Color.PERU;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * A consumable item that gives the player resistance to paralysis. This form of 
 * resistance has a small chance of wearing off every time it is used.
 *
 */
public class CopperPill extends AbstractItem {

    /**
     * Constructor.
     */
    public CopperPill() {
        super("Copper Pill", 0, new Symbol("&theta;", PERU));
    }
    
    @Override
    public void onUse(Event event) {
        Events.get().fireMessage(event.getBoard().getCurrentCell(),
            "Is it healthy to swallow this much copper? (You can't be paralyzed, but the effect can wear off when used.)");
        event.getPlayer().add(PARALYSIS_RESISTANT);
        event.getPlayer().getBag().remove(this);
    }
    /** Type serializer. */
    public static final Serializer<CopperPill> SERIALIZER = new TypeOnlySerializer<CopperPill>() {
        public CopperPill create(String[] args) {
            return new CopperPill();
        }
        public String getTag() {
            return "Power-Ups";
        }
    };
}
