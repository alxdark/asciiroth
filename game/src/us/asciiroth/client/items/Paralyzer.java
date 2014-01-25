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

import static us.asciiroth.client.core.Color.DARKVIOLET;
import static us.asciiroth.client.core.Flags.RANGED_WEAPON;
import us.asciiroth.client.Registry;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/** 
 * A gun that shoots parabullets, a kind of bullet that paralyzes its target but does 
 * not kill it. 
 */
public class Paralyzer extends AbstractItem {

    private Parabullet bullet;
    
    /** Constructor. */
    private Paralyzer() {
        super("Paralyzer", RANGED_WEAPON, new Symbol("&not;", DARKVIOLET));
        this.bullet = (Parabullet)Registry.get().getPiece(Parabullet.class);
    }
    @Override
    public Item onFire(Event event) {
        return bullet;
    }
    @Override
    public void onUse(Event event) {
        event.cancel("Use shift-direction to fire at something (requires paralyzer bullet ammo)");
    }
    /** Type serializer. */
    public static final Serializer<Paralyzer> SERIALIZER = new TypeOnlySerializer<Paralyzer>() {
        public Paralyzer create(String[] args) {
            return new Paralyzer();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
