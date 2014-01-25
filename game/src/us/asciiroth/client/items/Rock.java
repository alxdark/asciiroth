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

import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.WHITE;
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
 * A rock. It is a weapon, but more useful as a throwable. 
 *
 */
public class Rock extends AbstractItem implements ProvidesAmmo {

    /** Constructor. */
    public Rock() {
        super("Rock", 0, new Symbol("&bull;", WHITE, null, NEARBLACK, null));
    }

    @Override
    public void onHit(Event event, Cell cell, Agent agent) {
        Game.get().damage(cell, agent, CombatStats.ROCK_DAMAGE);
    }
    public Item providesAmmoFor() {
        return (Item)Registry.get().getPiece(AmmoSling.class);
    }
    /** Type serializer. */
    public static final Serializer<Rock> SERIALIZER = new TypeOnlySerializer<Rock>() {
        public Rock create(String[] args) {
            return new Rock();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
