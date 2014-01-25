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
package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.SADDLEBROWN;
import static us.asciiroth.client.core.Flags.ORGANIC;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.terrain.TerrainUtils;

/**
 * A decorative piece that tumbles across the board in the indicated direction. 
 * It doesn't really do anything but lend color to desert boards. By the gods,
 * this piece was complicated to make... 
 *
 */
public class Tumbleweed extends AbstractAnimatedAgent {
    
    private static Targeting TARGETING = new Targeting();
    
    private final Direction direction;
    
    /**
     * Constructor.
     * @param direction the direction the tumbleweed will tumble
     */
    public Tumbleweed(Direction direction) {
        super("Tumbleweed", ORGANIC, NONE, CombatStats.INDESTRUCTIBLE, new Symbol("*", SADDLEBROWN));
        if (direction == null) {
            throw new RuntimeException("Tumbleweed needs a direction");
        }
        this.direction = direction;
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        Direction newDir = AgentUtils.findPathInDirection(agentLoc, agent, null, dir, TARGETING);
        if (newDir != null) {
            Cell next = agentLoc.getAdjacentCell(dir);
            Game.get().agentMove(event, next, this, newDir);
        } else {
            event.cancel();
        }
    }
    
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%20==0) {
            // Need to test this first because findPathInDirection will automatically try and
            // compensate for the board edge by finding a direction going sideways along it, 
            // and that's not how we want to adjust in this situation.
            Cell next = cell.getAdjacentCell(direction);
            if (next != null) {
                // Now allow normal path-finding around obstacles to occur.
                Direction dir = AgentUtils.findPathInDirection(cell, this, null, direction, TARGETING);
                next = cell.getAdjacentCell(dir);
                if (next != null) {
                    if (next.canEnter(cell, this, dir, false)) {
                        Event e = Game.get().createEvent();
                        Game.get().agentMove(e, cell, this, dir);
                    }
                }
            } else {
                next = TerrainUtils.getCellOnOppositeSide(cell, direction);
                if (next.canEnter(cell, this, direction, false)) {
                    cell.removeAgent(this);
                    next.setAgent(this);
                    return;
                }
                // I don't understand this... I guess it forces a lateral move -2 or 2 
                // directions from head on? I don't believe this will work anymore.
                
                // Now in theory, this method will move the piece laterally, because it can 
                // tell that the direction in question leads to a null cell... in fact it 
                // doesn't seem to work.
                Direction dir = AgentUtils.findPathInDirection(cell, this, null, direction, TARGETING);
                // Then use that direction to move the tumbleweed...
                next = cell.getAdjacentCell(dir);
                // And use that position to find hopefully unblocked position on board
                next = TerrainUtils.getCellOnOppositeSide(next, direction);
                if (next.canEnter(cell, this, direction, false)) {
                    cell.removeAgent(this);
                    next.setAgent(this);
                    return;
                }
                // but if that remains untrue, remove the tumbleweed from the board. it looks
                // funny traveling on the edge of the board to find an opening.
                cell.removeAgent(this);
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Tumbleweed> SERIALIZER = new BaseSerializer<Tumbleweed>() {
        public Tumbleweed create(String[] args) {
            return new Tumbleweed(Direction.byName(args[1]));
        }
        public Tumbleweed example() {
            return new Tumbleweed(Direction.WEST);
        }
        public String store(Tumbleweed t) {
            return "Tumbleweed|"+t.direction.getName();
        }
        public String template(String type) {
            return "Tumbleweed|{direction}";
        }
        public String getTag() {
            return "Outside Terrain";
        }
    };
}
