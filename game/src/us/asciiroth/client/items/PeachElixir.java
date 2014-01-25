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

import static us.asciiroth.client.core.Flags.STONING_RESISTANT;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * Peach elixir makes the player resistant to stoning attacks. 
 * If you're turned to stone, it's Game Over.
 *
 */
public class PeachElixir extends AbstractItem {
    
    /** Constructor. */
    public PeachElixir() {
        super("Peach Elixir", 0, new Symbol("&iexcl;", Color.PEACHPUFF, null, Color.LIGHTSALMON, null));
    }
    @Override
    public void onUse(Event event) {
        Events.get().fireMessage(event.getBoard().getCurrentCell(),"Delicious! (You can't be turned to stone, but the effect can wear off when used.)");
        event.getPlayer().add(STONING_RESISTANT);
        event.getPlayer().getBag().remove(this);
    }
    /** Type serializer. */
    public static final Serializer<PeachElixir> SERIALIZER = new TypeOnlySerializer<PeachElixir>() {
        public PeachElixir create(String[] args) {
            return new PeachElixir();
        }
        public String getTag() {
            return "Power-Ups";
        }
    };
}
