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
package us.asciiroth.client.core;

import us.asciiroth.client.board.Cell;
import us.asciiroth.client.effects.InFlightItem;

/**
 * Terrain is a piece type that describes the behavior and appearance of 
 * a cell on the board. There is only one terrain piece per cell, although 
 * some terrain types nest other terrain pieces (actually this nesting can 
 * go several levels deep). Items and agents can be placed on terrain, 
 * and must move through terrain. 
 *
 */
public interface Terrain extends Piece {
    /**
     * Can the (non-player) agent enter this terrain? Without regard to side 
     * effects, this method should determine if the agent can actually move into 
     * the cell. Error on the side of caution and return false if the answer
     * is ambiguous. 
     * @param agent
     * @param cell
     * @param direction
     * @return  true if the agent can enter the terrain moving between the 
     *          two cells using the indicated direction
     * @see us.asciiroth.client.board.Cell#canEnter(Cell, Agent, Direction, boolean)
     */
    public boolean canEnter(Agent agent, Cell cell, Direction direction);
    /**
     * Can the (non-player) agent exit this terrain? Without regard to side 
     * effects, this method should determine if the agent can actually move into 
     * the cell. Error on the side of caution and return false if the answer
     * is ambiguous. 
     * @param agent
     * @param cell
     * @param direction
     * @return  true if the agent can leave this cell going in the indicated 
     *          direction
     * @see us.asciiroth.client.board.Cell#canEnter(Cell, Agent, Direction, boolean)
     */
    public boolean canExit(Agent agent, Cell cell, Direction direction);
    
    /**
     * Agent attempting to leave the <code>from</code> cell, heading 
     * toward the <code>to</code> cell. The terrain being used is the 
     * terrain of the <code>to</code> cell. The agent is still in the 
     * <code>from</cell cell. If the event is canceled at this point, 
     * the agent will not be able enter this cell. The move will be 
     * canceled. This event is fired for the player as well as all 
     * other agents.
     * @param event
     * @param player
     * @param cell
     * @param dir
     */
    public void onEnter(Event event, Player player, Cell cell, Direction dir);
    /**
     * Agent attempting to exit the <code>from</code> cell, heading 
     * toward the <code>to</code> cell. The terrain being used is the 
     * <code>from</code> cell terrain, and the agent is still in the <code>from</code> 
     * cell. If the event is canceled at this point, the agent will not be able 
     * leave this cell. The move will be canceled. This event is fired for the 
     * player as well as all other agents
     * @param event
     * @param player
     * @param cell
     * @param dir
     */
    public void onExit(Event event, Player player, Cell cell, Direction dir);
    
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir);
    
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir);
    
    /**
     * Terrain has an item flying over it. If the event is canceled
     * at this point, the item will fall to the ground on this cell.
     * @param event
     * @param cell
     * @param flier
     */
    public void onFlyOver(Event event, Cell cell, InFlightItem flier);
    /**
     * Item is going to drop onto the terrain at this point. If the event
     * if canceled at this point, then the item will simply disappear
     * from the board.
     * @param event
     * @param itemLoc
     * @param item
     */
    public void onDrop(Event event, Cell itemLoc, Item item);
    /**
     * Item is going to be picked up. If the event is canceled at this
     * point, the item will not be picked up and will remain on the cell. 
     * @param event
     * @param loc
     * @param agent
     * @param item
     */
    public void onPickup(Event event, Cell loc, Agent agent, Item item);
    /**
     * This terrain is adjacent to the player. It may render differently
     * under these circumstances.
     * @param context
     * @param cell
     */
    public void onAdjacentTo(Context context, Cell cell);
    
    /**
     * This terrain is no longer adjacent to the player. It may render 
     * differently under these circumstances.
     * @param context
     * @param cell
     */
    public void onNotAdjacentTo(Context context, Cell cell);
}
