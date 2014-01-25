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
package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.Boulder;
import us.asciiroth.client.agents.Pusher;
import us.asciiroth.client.agents.Slider;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Grenade;

/**
 * Impassable but items can be thrown across it. If an item lands such that 
 * it should fall into a pit, it'll disappear. Boulders can be used to "plug" 
 * pits and turn them into passable <code>Floor</code> terrain. 
 */
public class Pit extends AbstractTerrain {

    /** Constructor. */
    private Pit() {
        super("Pit", PENETRABLE, 
            new Symbol("U", NEARBLACK, BLACK, BLACK, BUILDING_FLOOR));
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return (agent instanceof Boulder);
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel(cell, "You'd fall into the pit");
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (agent instanceof Boulder) {
            // This is actually necessary, I think because the piece hasn't been moved yet.
            // This is what makes it all so error prone.
            cell.getAdjacentCell(dir.getReverseDirection()).removeAgent(agent);
            Terrain floor = (Terrain)Registry.get().getPiece(Floor.class);
            cell.setTerrain(floor);
            Events.get().fireMessage(cell, "The pit is filled by the boulder");    
        } else if (agent instanceof Slider || agent instanceof Pusher) {
            cell.getAdjacentCell(dir.getReverseDirection()).removeAgent(agent);
            String s = Util.format("The {0} falls through the pit", agent.getName().toLowerCase());
            Events.get().fireMessage(cell,s); 
        } else {
            event.cancel();
        }
    }
    @Override
    public void onDrop(Event event, Cell cell, Item item) {
        // Grenades just blow up.
        if (!(item instanceof Grenade)) {
            event.cancel(cell, item.getDefiniteNoun("{0} falls into the pit"));
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Pit> SERIALIZER = new TypeOnlySerializer<Pit>() {
        public Pit create(String[] args) {
            return new Pit();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
