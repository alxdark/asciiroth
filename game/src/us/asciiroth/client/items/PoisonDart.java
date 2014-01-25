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

import static us.asciiroth.client.core.Color.SADDLEBROWN;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.NOT_EDITABLE;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.POISONED;
import static us.asciiroth.client.core.Flags.POISON_RESISTANT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * If this dart hits the player, the player is poisoned, and cannot heal
 * until the poisoning is removed. 
 */
public class PoisonDart extends AbstractItem {

    /** Constructor. */
    private PoisonDart() {
        super("Poison Dart", AMMUNITION | NOT_EDITABLE, new Symbol("&#39;", SADDLEBROWN));
    }
    @Override
    public void onHit(Event event, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.DART_DAMAGE);
        if (agent.is(PLAYER)) {
            Player player = event.getPlayer();
            if (!player.testResistance(POISON_RESISTANT)) {
                player.add(POISONED);
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<PoisonDart> SERIALIZER = new TypeOnlySerializer<PoisonDart>() {
        public PoisonDart create(String[] args) {
            return new PoisonDart();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
