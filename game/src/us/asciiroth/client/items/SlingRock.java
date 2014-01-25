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
import static us.asciiroth.client.core.Color.SILVER;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.NOT_EDITABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A rock that is used as ammunition by the sling. So it disappears
 * when used, rather than falling to the ground.
 *
 */
public class SlingRock extends AbstractItem {

    /** Constructor. */
    public SlingRock() {
        super("Rock", AMMUNITION | NOT_EDITABLE, new Symbol("&bull;", SILVER, null, NEARBLACK, null));
    }

    @Override
    public void onHit(Event event, Cell cell, Agent agent) {
        Game.get().damage(cell, agent, CombatStats.SLING_ROCK_DAMAGE);
    }
    /** Type serializer. */
    public static final Serializer<SlingRock> SERIALIZER = new TypeOnlySerializer<SlingRock>() {
        public SlingRock create(String[] args) {
            return new SlingRock();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
