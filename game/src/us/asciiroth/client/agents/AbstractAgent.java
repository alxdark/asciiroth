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

import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.PUSHABLE;
import static us.asciiroth.client.core.Flags.WEAK;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.AbstractPiece;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Symbol;

import com.google.gwt.user.client.Random;

/**
 * Abstract class for agents. It handles the use of health as a percentage 
 * chance to kill an agent.
 *
 */
public abstract class AbstractAgent extends AbstractPiece implements Agent {

    private int chanceToHit;
    
    /**
     * Constructor.
     * @param name
     * @param flags
     * @param color
     * @param chanceToHit
     * @param symbol
     */
    public AbstractAgent(String name, int flags, Color color, int chanceToHit, Symbol symbol) {
        super(name, flags, color, symbol);
        this.chanceToHit = chanceToHit;
    }
    
    /**
     * For agents, "health" is the chance of destroying an agent. The 
     * higher the number, the harder to kill.
     */
    public int changeHealth(int delta) {
        int test = this.chanceToHit + delta;
        // The actual test number can be anything, even 0 or less, and all we 
        // need in the case of agents is an indication of dead or not dead. 0 
        // is dead, 1 is not.
        // Log.get().debug(Util.format("Chance to hit {0}: {1}",name,Integer.toString(test)));
        return (Random.nextInt(100) <= test) ? 0 : 1;
    }
    public boolean canEnter(Direction direction, Cell from, Cell to) {
        return true;
    }
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
    }
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        if (agent.is(PLAYER) && is(PUSHABLE)) {
            if (agent.is(WEAK)) {
                event.cancel("You're too weak to push anything");
            } else {
                Cell cell = agentLoc.getAdjacentCell(dir);
                Game.get().agentMove(event, cell, this, dir);
            }
        } else if (agent instanceof RollingBoulder && !(this instanceof ImmobileAgent)) {
            Cell cell = agentLoc.getAdjacentCell(dir);
            Game.get().agentMove(event, cell, this, dir);
            if (event.isCancelled()) {
                Game.get().damage(cell, this, 500);
            }
        } else {
            event.cancel();
        } 
    }
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir) {
        if (not(PENETRABLE)) {
            event.cancel();
        }
    }
    public void onDie(Event event, Cell cell) {
        event.getBoard().fireColorEvent(event, color, cell);
    }
}
