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

import static us.asciiroth.client.core.Color.DARKSLATEBLUE;
import static us.asciiroth.client.core.Color.LIGHTSTEELBLUE;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A bullet.
 */
public class Bullet extends AbstractItem implements ProvidesAmmo {

    /** Constructor. */
    public Bullet() {
        super("Bullet", AMMUNITION, new Symbol("&bull;", LIGHTSTEELBLUE, null, DARKSLATEBLUE, null));
    }
    @Override
    public void onHit(Event event, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.BULLET_DAMAGE);
    }
    public Item providesAmmoFor() {
        return (Item)Registry.get().getPiece(AmmoGun.class);
    }
    /** Type serializer. */
    public static final Serializer<Bullet> SERIALIZER = new TypeOnlySerializer<Bullet>() {
        public Bullet create(String[] args) {
            return new Bullet();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
