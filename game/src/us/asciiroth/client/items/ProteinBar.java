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

import static us.asciiroth.client.core.Color.BURLYWOOD;
import static us.asciiroth.client.core.Color.PERU;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * An energy-type protein bar; if eaten, it will cure weakness.
 *
 */
public class ProteinBar extends AbstractItem {

    /** Constructor. */
    private ProteinBar() {
        super("Protein Bar", 0, new Symbol("=", BURLYWOOD, null, PERU, null));
    }
    @Override
    public void onUse(Event event) {
        event.getPlayer().cureWeakness(event, this);
    }
    /** Type serializer. */
    public static final Serializer<ProteinBar> SERIALIZER = new TypeOnlySerializer<ProteinBar>() {
        public ProteinBar create(String[] args) {
            return new ProteinBar();
        }
        public String getTag() {
            return "Power-Ups";
        }
    };
    
}
