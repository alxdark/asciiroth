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

import static us.asciiroth.client.core.Color.DARKKHAKI;
import static us.asciiroth.client.core.Color.SIENNA;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * A raft. When an agent moves onto a raft (the player or any other agent), 
 * they can use it to move over water or ocean. They can only leave it when 
 * it touches a non-water terrain they can enter. However, it's not capable 
 * of figuring out areas where there is both water and ocean pieces, it will 
 * mix these up (though the player is unlikely to notice); generally on a 
 * map you'd have either ocean or water, and they wouldn't be mixed together, 
 * but nothing enforces this. 
 * 
 */
public class Raft extends AbstractTerrain {

    private final Water water;
    private final Ocean ocean;
    
    /**
     * Constructor
     */
    protected Raft() {
        super("Raft", (TRAVERSABLE | PENETRABLE), new Symbol("&equiv;", "&#8803;", SIENNA, DARKKHAKI));
        this.water = (Water)Registry.get().getPiece("Water");
        this.ocean = (Ocean)Registry.get().getPiece("Ocean");
    }
    
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        moveTheRaft(event, player, cell, dir);
        super.onExit(event, player, cell, dir);
    }
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        moveTheRaft(event, agent, cell, dir);
        super.onAgentExit(event, agent, cell, dir);
    }
    private void moveTheRaft(Event event, Agent agent, Cell cell, Direction dir) {
        Cell next = cell.getAdjacentCell(dir);
        Terrain nextTerrain = next.getTerrain();
        if (nextTerrain instanceof Water) {
            next.setTerrain(this);
            cell.setTerrain(this.water);
        } else if (nextTerrain instanceof Ocean) {
            next.setTerrain(this);
            cell.setTerrain(this.ocean);
        } else if (nextTerrain instanceof Waterfall) {
            event.cancel(cell, "The water is too turbulent for a raft");
        } else if (nextTerrain instanceof ShallowWater || nextTerrain instanceof Surf) {
            event.cancel(cell, "It's too shallow for the raft here.");
        }
    }

    /** Type serializer. */
    public static final Serializer<Raft> SERIALIZER = new TypeOnlySerializer<Raft>() {
        public Raft create(String[] args) {
            return new Raft();
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
