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
package us.asciiroth.client.terrain.decorators;

import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.Boulder;
import us.asciiroth.client.agents.Pusher;
import us.asciiroth.client.agents.Slider;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.Floor;
import us.asciiroth.client.terrain.Pit;

import com.google.gwt.user.client.Timer;

/**
 * A hidden pit. It behaves like a pit in most respects and if anything
 * falls into it, the pit will become apparent to the player. However, 
 * if the player walks onto the pit while it is still hidden, he or she 
 * will fall through to the map below the current board (so there has 
 * to be a "down" map or the hidden pit will be the source of errors). 
 * Like other mimics, it can be detected with the detect hidden ability.
 * <p>
 * Yes, agents walk right over these things...
 */
public class PitTrap extends Mimic {
    
    /** Constructor. */
    public PitTrap() {
        super(
            (Terrain)Registry.get().getPiece(Floor.class), 
            (Terrain)Registry.get().getPiece(Pit.class), NONE);
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return false;
    }
    @Override
    public void onEnter(Event event, final Player player, final Cell cell, Direction dir) {
        Events.get().fireModalMessage("You fall through a pit!");
        cell.setTerrain(terrain);   
            
        Game.get().damage(cell, player, 20);
        Timer timer = new Timer() {
            @Override
            public void run() {
                if (player.changeHealth(0) > 0) {
                    String boardID = cell.getBoard().getAdjacentBoard(Direction.DOWN);
                    Game.get().teleport(boardID, cell.getX(), cell.getY());
                }
            }
        };
        timer.schedule(200);
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (agent instanceof Boulder || agent instanceof Slider || agent instanceof Pusher) {
            cell.getAdjacentCell(dir.getReverseDirection()).removeAgent(agent);
            cell.setTerrain(terrain); // now should look like pit
            if (agent instanceof Boulder) {
                Events.get().fireMessage(cell, "The boulder fills a hidden pit!");
                Terrain floor = (Terrain)Registry.get().getPiece(Floor.class);
                cell.setTerrain(floor, true);
            } else {
                Events.get().fireMessage(cell,
                    Util.format("The {0} falls through a hidden pit", 
                        agent.getName().toLowerCase())); 
            }
        }
    }
    
    /** Type serializer. */
    public static final Serializer<PitTrap> SERIALIZER = new TypeOnlySerializer<PitTrap>() {
        public PitTrap create(String[] args) {
            return new PitTrap();
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
