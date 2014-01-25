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

import us.asciiroth.client.Util;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * A door key. The key's color must match the color of the door in order to open it. 
 */
public class Key extends AbstractItem {
    
    /**
     * Constructor.
     * @param color
     */
    private Key(Color color) {
        super(color.getName()+" Key", 0, color, new Symbol("&#126;", color));
    }
    @Override
    public void onUse(Event event) {
        event.cancel("Try walking toward a door while holding the key.");
    }
    /** Type serializer. */
    public static final Serializer<Key> SERIALIZER = new Serializer<Key>() {
        public Key create(String[] args) {
            return new Key(Color.byName(args[1]));
        }
        public String store(Key k) {
            return Util.format("Key|{0}", k.color.getName());
        }
        public Key example() {
            return new Key(Color.STEELBLUE);
        }
        public String template(String key) {
            return "Key|{color}";
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
