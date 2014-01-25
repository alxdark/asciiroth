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

import static us.asciiroth.client.core.Flags.MELEE_WEAPON;
import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A crowbar, can be used to open crates, and can also be used as a hammer.
 *
 */
public class Crowbar extends AbstractItem {

    private Crowbar() {
        super("Crowbar", MELEE_WEAPON, new Symbol("]", "&int;", WHITE, null, BLACK, null));
    }

    @Override
    public void onHit(Event event, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.CROWBAR_DAMAGE);
    }

    /** Type serializer. */
    public static final Serializer<Crowbar> SERIALIZER = new TypeOnlySerializer<Crowbar>() {
        public Crowbar create(String[] args) {
            return new Crowbar();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
