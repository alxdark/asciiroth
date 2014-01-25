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

import static us.asciiroth.client.core.Color.LIMEGREEN;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A kiwi.  
 */
public class Kiwi extends AbstractItem {

    Kiwi() {
        super("Kiwi", 0, new Symbol("o", LIMEGREEN));
    }
    @Override
    public void onUse(Event event) {
        event.getPlayer().heal(event, this, 5);
    }
    /** Type serializer. */
    public static final Serializer<Kiwi> SERIALIZER = new TypeOnlySerializer<Kiwi>() {
        public Kiwi create(String[] args) {
            return new Kiwi();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
