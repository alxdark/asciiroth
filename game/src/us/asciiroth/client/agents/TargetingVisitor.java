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
import static us.asciiroth.client.core.Flags.PLAYER;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellVisitor;
import us.asciiroth.client.core.Agent;

/**
 * For more intensive searches for targets, this visitor identifies the
 * closest viable option based on the <code>Targeting</code> parameters
 * supplied, in one pass through a region of the board. 
 * 
 * @see us.asciiroth.client.agents.AgentUtils
 *
 */
class TargetingVisitor implements CellVisitor {

    private Cell targeted;
    private Cell mostRecentlyVisited;
    private int distance;
    private Targeting targeting;
    private Agent targeter;
    
    void init(Targeting targeting, Agent targeter) {
        this.targeted = null;
        this.mostRecentlyVisited = null;
        this.distance = 0;
        this.targeting = targeting;
        this.targeter = targeter;
    }
    Cell getTargetedCell() {
        return this.targeted;
    }
    int getTargetedCellDistance() {
        return distance;
    }
    Cell getAdjacentVisitedCell() {
        return this.mostRecentlyVisited;
    }
    public boolean visit(Cell cell, int range) {
        // Are we using the boolean to bail out of visiting entire range once we've 
        // found a target? That should be the goal.
        if (targeted != null) {
            return false;
        }
        // isBagEmpty() check is important to prevent making item bags until needed
        if (targeter.is(CARNIVORE) && !cell.isBagEmpty() && cell.getBag().containsMeat()) {
            targeted = cell;
            distance = range;
            return false;
        }
        // If the target is the player (either to attack or flee), you've found it
        else if ((targeting.getFleePlayer() || targeting.getAttackPlayer()) && 
            cell.getAgent() != null && cell.getAgent().is(PLAYER)) {
            targeted = cell;
            distance = range;
            return false;
        }
        // You do have to check for zero, or this will always end up matching a cell (the first
        // visited 0)
        else if (targeting.getTrackPlayer() && range == 1 && cell.getVisited() != 0) {
            // First time around any positive visited number matches against null MRV cell
            if (mostRecentlyVisited == null || cell.getVisited() > mostRecentlyVisited.getVisited()) {
                mostRecentlyVisited = cell;
                distance = range;
                return true;
            }
        }
        return true;
    }
}
