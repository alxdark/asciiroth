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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.RANGED_WEAPON;
import static us.asciiroth.client.core.Flags.REQUIRES_AMMO;
import us.asciiroth.client.Registry;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/** 
 * A gun that shoots bullets. (Well, yes, most guns do shoot bullets.)
 */
public class AmmoGun extends AbstractItem implements ConsumesAmmo {

    private Bullet bullet;
    
    /** Constructor. */
    private AmmoGun() {
        super("Gun", RANGED_WEAPON | REQUIRES_AMMO, new Symbol("&not;", WHITE, null, BLACK, null));
        this.bullet = (Bullet)Registry.get().getPiece(Bullet.class);
    }
    @Override
    public Item onFire(Event event) {
        return AgentUtils.assessAmmo(event, this, bullet);
    }
    @Override
    public void onUse(Event event) {
        event.cancel("Use shift-direction to fire at something (requires bullet ammo)");
    }
    public Item getAmmoType() {
        return (Item)Registry.get().getPiece(Bullet.class);
    }
    /** Type serializer. */
    public static final Serializer<AmmoGun> SERIALIZER = new TypeOnlySerializer<AmmoGun>() {
        public AmmoGun create(String[] args) {
            return new AmmoGun();
        }
        public String getTag() {
            return "Weapons";
        }
    };

}
