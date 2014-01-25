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

import static us.asciiroth.client.core.Flags.PLAYER;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Wall;

/**
 * An agent gate decorates another terrain type and changes its behavior
 * so that all agents except the player consider that cell impassable, 
 * and will not move through it. This can be used to keep agents in certain
 * areas, if desirable.
 *
 */
public class AgentGate extends Decorator {

    /**
     * Constructor.
     * @param terrain
     */
    public AgentGate(Terrain terrain) {
        super(terrain, 0);
    }
    public Terrain proxy(Terrain terrain) {
        return new AgentGate(terrain);
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        // If the to cell has the player in it, the move is an attack, so do it, otherwise don't
        return (cell.getAgent() != null && cell.getAgent().is(PLAYER));
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return false;
    }
    /** Type serializer. */
    public static final Serializer<AgentGate> SERIALIZER = new BaseSerializer<AgentGate>() {
        public AgentGate create(String[] args) {
            return new AgentGate(unescTerrain(args[1]));
        }
        public AgentGate example() {
            return new AgentGate(new Wall());
        }
        public String store(AgentGate piece) {
            return "AgentGate|"+esc(piece.terrain);
        }
        public String template(String type) {
            return "AgentGate|{terrain}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
