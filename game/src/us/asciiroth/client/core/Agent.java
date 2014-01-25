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

/**
 * Interface for pieces that exclusively occupy a cell. There can only be 
 * one agent in a cell at a time, although not all agents are animate and 
 * move around. Things that are usefully placed "on" different kinds of terrain
 * can be modeled as agents. 
 *  
 */
public interface Agent extends Piece {

    /**
     * Change health. For the player, this method indicates an amount of change
     * to health, and returns the current health of the player after being 
     * changed. For other agents, the value an adjustment to the percentage chance 
     * that the agent will be hit by an attack. 
     * @param value     the damage to take or the adjustment to the percentage 
     *                  chance to be hit.
     * @return          returns the current health of the agent, 0 for non-player
     *                  agents when they have been killed.
     */
    public int changeHealth(int value);
    
    /**
     * Can the agent enter this terrain? This method mirrors the same method in Terrain, 
     * and both most return true for the agent to be able to move. You can put the logic
     * for preventing agent movement in either place, depending on where it makes sense
     * in order to centralize it. NOTE: There is currently no <code>canExit</code> method
     * for agents, but it can be added if any piece ever needs it.  
     * @param direction
     * @param from
     * @param to
     * @return  true if the agent can enter the terrain moving between the 
     *          two cells using the indicated direction
     * @see us.asciiroth.client.core.Terrain#canEnter(Agent, Cell, Direction)
     */
    public boolean canEnter(Direction direction, Cell from, Cell to);
    /**
     * Fired when this agent collides with the player, or if this agent is the player,
     * when the player collides with another agent (in other words, this method will 
     * not fire if one non-player agent collides with another non-player agent). 
     * 
     * @param event
     * @param attackerCell
     * @param agentLoc
     * @param agent
     */
    public void onHit(Event event, Cell attackerCell, Cell agentLoc, Agent agent);
    
    /**
     * Called when this agent is hit by another agent (another agent collides 
     * with it). Typically the move event will be cancelled in this method, 
     * unless this agent moves itself out of the way, such as boulders attempt to do. 
     *  
     * @param event
     * @param agentLoc
     * @param agent
     * @param dir
     */
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir);
    
    /**
     * Called when this agent is hit by another item, either by being thrown, 
     * or by being wielded by the player.
     * @param event
     * @param itemLoc
     * @param item
     * @param dir
     */
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir);
    
    /**
     * Called when the agent dies on a given cell.
     * @param event
     * @param cell
     */
    public void onDie(Event event, Cell cell);
}
