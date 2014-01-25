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

import static us.asciiroth.client.core.Color.TAN;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A fish. You can eat it and it will heal you, or throw it and 
 * it will attract monsters, who will eat it until it turns into a 
 * bone, then eat the bone, etc.  
 */
public class Bread extends AbstractItem {

    Bread() {
        super("Bread", 0, new Symbol("&infin;", TAN));
    }
    @Override
    public void onUse(Event event) {
        event.getPlayer().heal(event, this, 10);
    }
    /** Type serializer. */
    public static final Serializer<Bread> SERIALIZER = new TypeOnlySerializer<Bread>() {
        public Bread create(String[] args) {
            return new Bread();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
