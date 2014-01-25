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

import static us.asciiroth.client.core.Color.RED;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * An apple.  
 */
public class Apple extends AbstractItem {

    Apple() {
        super("Apple", 0, new Symbol("&otilde;", RED));
    }
    @Override
    public void onUse(Event event) {
        event.getPlayer().heal(event, this, 5);
    }
    /** Type serializer. */
    public static final Serializer<Apple> SERIALIZER = new TypeOnlySerializer<Apple>() {
        public Apple create(String[] args) {
            return new Apple();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
