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
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.PARALYSIS_RESISTANT;
import static us.asciiroth.client.core.Flags.PARALYZED;
import static us.asciiroth.client.core.Flags.PLAYER;
import us.asciiroth.client.Registry;
import us.asciiroth.client.agents.AgentProxy;
import us.asciiroth.client.agents.Paralyzed;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A bullet that will paralyze an agent when hit, if the agent isn't resistant
 * to paralysis. 
 */
public class Parabullet extends AbstractItem implements ProvidesAmmo {

    /** Constructor. */
    Parabullet() {
        super("Paralysis Bullet", AMMUNITION, new Symbol("*", DARKVIOLET));
    }
    @Override
    public void onHit(Event event, Cell agentLoc, Agent agent) {
    	if (agent instanceof AgentProxy) {
    		return;
    	}
        if (agent.is(PLAYER)) {
            Player player = event.getPlayer();
            if (player.testResistance(PARALYSIS_RESISTANT)) {
                return;
            }
            player.add(PARALYZED);
        }
        agentLoc.setAgent(new Paralyzed(agent));
    }
    public Item providesAmmoFor() {
        return (Item)Registry.get().getPiece(AmmoParalyzer.class);
    }

    /** Type serializer. */
    public static final Serializer<Parabullet> SERIALIZER = new TypeOnlySerializer<Parabullet>() {
        public Parabullet create(String[] args) {
            return new Parabullet();
        }
        public String getTag() {
            return "Ammunition";
        }
    };
}
