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

import static us.asciiroth.client.core.Flags.RANGED_WEAPON;
import static us.asciiroth.client.core.Color.SADDLEBROWN;
import us.asciiroth.client.Registry;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/** 
 * A bow that shoots arrows. 
 */
public class Bow extends AbstractItem {

    private final Arrow arrow;
    
    /** Constructor. Public so it can be used in example pieces. */
    public Bow() {
        super("Bow", RANGED_WEAPON, new Symbol("&#41;", SADDLEBROWN));
        this.arrow = (Arrow)Registry.get().getPiece(Arrow.class);
    }
    @Override
    public Item onFire(Event event) {
        return arrow;
    }
    @Override
    public void onUse(Event event) {
        event.cancel("Use shift-direction to aim at something");
    }
    /** Type serializer. */
    public static final Serializer<Bow> SERIALIZER = new TypeOnlySerializer<Bow>() {
        public Bow create(String[] args) {
            return new Bow();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
