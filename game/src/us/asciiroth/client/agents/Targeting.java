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

import static us.asciiroth.client.core.Flags.CARNIVORE;
import us.asciiroth.client.core.Agent;

/**
 * A parameter object that describes how an agent wants to find a target. 
 * The only reason this class is public is that Java has lame packaging 
 * mechanics.
 *
 */
public class Targeting {
    
    private int range;
    private int distance;
    private int dodgeBullets;
    private boolean fleePlayer;
    private boolean attackPlayer;
    private boolean moveRandomly;
    private boolean trackPlayer;
    
    /**
     * Constructor.
     */
    public Targeting() {
        range = 1;
        distance = -1; // doesn't care
    }
    /** 
     * Does this targeting specification only includes moving toward the player?
     * If so, we can use a faster algorithm for targeting. 
     * @param agent
     * @return  true if this targeting specification only includes directly 
     *          moving toward the player and no other targeting strategy
     */
    public boolean targetsPlayerDirectly(Agent agent) {
        return (attackPlayer && agent.not(CARNIVORE) && !trackPlayer);
    }
    /**
     * Run away from the player.
     * @param range
     * @return      Targeting
     */
    public Targeting fleePlayer(int range) {
        this.fleePlayer = true;
        this.range = range;
        return this;
    }
    /**
     * Attack the player. This determines whether the agent will try and move
     * toward and into the player. 
     * @param range     the number of squares within which attack behavior applies
     * @return          Targeting
     */
    public Targeting attackPlayer(int range) {
        this.attackPlayer = true;
        this.range = range;
        return this;
    }
    /**
     * Avoid any cell that has a direct line-of-fire between the player and the
     * agent. Generally agents that shoot things need to get directly in the 
     * LOF to hit the agent, at the cost of being easier to hit by the player, 
     * while agents that approach the player to do direct damage usually don't 
     * want to walk into the LOF of the player.
     * @param       percentage 
     * @return      Targeting
     */
    public Targeting dodgeBullets(int percentage) {
        this.dodgeBullets = percentage;
        return this;
    }
    /**
     * Track the player. If the agent moves to within one square of a cell that
     * the player has visited during the current load of the board, and no 
     * better target presents itself, the agent will start following the player's
     * breadcrumb.
     * @return          Targeting
     */
    public Targeting trackPlayer() {
        this.trackPlayer = true;
        return this;
    }
    /**
     * Set the distance (in cells) that an agent should keep from the player.
     * @param distance  the distance in cells to stay away from player
     * @return          Targeting
     */
    public Targeting keepDistance(int distance) {
        this.distance = distance;
        return this;
    }
    /**
     * Set the agent to move randomly if a target cannot be acquired.
     * @return          Targeting.
     */
    public Targeting moveRandomly() {
        this.moveRandomly = true;
        return this;
    }
    boolean getFleePlayer() {
        return fleePlayer;
    }
    boolean getAttackPlayer() {
        return attackPlayer;
    }
    boolean getMoveRandom() {
        return moveRandomly;
    }
    boolean getTrackPlayer() {
        return trackPlayer;
    }
    int getDodgeBullets() {
        return dodgeBullets;
    }
    int getRange() {
        return range;
    }
    int getDistance() {
        return distance;
    }
}
