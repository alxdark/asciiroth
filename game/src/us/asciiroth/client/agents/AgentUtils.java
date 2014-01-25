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

import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Flags.PARALYSIS_RESISTANT;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.RANGED_WEAPON;
import static us.asciiroth.client.core.Flags.STONING_RESISTANT;
import static us.asciiroth.client.core.Flags.TURNED_TO_STONE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellVisitor;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.State;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Grenade;
import us.asciiroth.client.terrain.TerrainUtils;

import com.google.gwt.user.client.Random;

/**
 * Function library for various movement and related algorithms.
 *
 */
public class AgentUtils {
    
    private static TargetingVisitor visitor = new TargetingVisitor();
    
    // SFAICT, you never really get to the 3s.
    private static int[] LEFT_FIRST = new int[] {-1, 1, -2, 2, -3, 3};
    private static int[] RIGHT_FIRST = new int[] {1, -1, 2, -2, 3, -3};
    
    public static Item assessAmmo(Event event, Item weaponType, Item returnType) {
        PlayerBag inv = event.getPlayer().getBag();
        Bag.Entry<Item> weaponEntry = inv.findEntry(weaponType);
        int ammoCount = weaponEntry.getAmmo();
        if (ammoCount > 0) {
            weaponEntry.changeAmmo(-1);
            Events.get().fireInventoryChanged(inv);
            return returnType;
        }
        Events.get().fireMessage(event.getBoard().getCurrentCell(), "Out of ammo");
        return null;    
    }
    
    /**
     * The player has done something that will change perception of the
     * adjacent cells. This is essentially like "moving in place".
     * 
     * @param context
     */
    public static void updateAdjacent(final Context context, int range) {
        context.getBoard().visitRange(context.getBoard().getCurrentCell(), range, true, new CellVisitor() {
            public boolean visit(Cell cell, int range) {
                cell.getTerrain().onNotAdjacentTo(context, cell);
                cell.getTerrain().onAdjacentTo(context, cell);
                return true;
            }
        });
    }
    
    /**
     * Turn the agent to stone. Player resistances may prevent this from 
     * occurring (currently both paralysis and stoning resistance apply). 
     * The statue generated will use the provided color, which indicates 
     * the color event that would cause the statue to revert to a living
     * agent.
     * @param agentLoc
     * @param agent
     * @param color
     */
    public static void turnToStone(Cell agentLoc, Agent agent, Color color) {
        if (agent.is(ORGANIC) && !(agent instanceof AgentProxy)) {
            if (agent.is(PLAYER)) {
                Player player = (Player)agent;
                if (player.testResistance(PARALYSIS_RESISTANT) ||
                    player.testResistance(STONING_RESISTANT)) {
                    return;
                }
                // Wonder what this is used for...
                player.add(TURNED_TO_STONE);
            }
        	// NPC or something else turned to stone
            Statue s = new Statue(agent, color);
            agentLoc.setAgent(s);
        }
    }
    /**
     * Convert the agent to the opposite state of the state that is provided
     * to this method.
     * @param agent
     * @param state 
     * @return      the new agent instance (cached)
     */
    public static Agent getAgentOtherState(Agent agent, State state) {
        String key = Registry.get().getKey(agent);
        if (state.isOn()) {
            key = key.replaceAll("\\bon\\b", "off");
        } else {
            key = key.replaceAll("\\boff\\b", "on");
        }
        return (Agent)Registry.get().getPiece(key);
    }
    
    /**
     * Given targeting information, devise the best possible direction to move. The 
     * targeting parameter object contains many flags for altering both the target
     * and the strategy for alternate targets, etc. 
     * @param agentLoc
     * @param agent
     * @param targeting
     * @return  best possible direction (can return null if no direction is appropriate
     *          given the targeting strategy of the agent)
     */
    public static Direction findPathToTarget(final Cell agentLoc, final Agent agent, final Targeting targeting) {
        Direction dir = null;
        Cell targetCell = null;
        double actualDistance = 0;
        
        if (targeting.targetsPlayerDirectly(agent)) {
            // This is presumably faster, though I've never clocked it.
            targetCell = agentLoc.getBoard().getCurrentCell();
            dir = getDirectionToCellRangeLimited(agentLoc, targetCell, targeting);
            actualDistance = getDistance(agentLoc, targetCell);
        } else {
            visitor.init(targeting, agent);
            agentLoc.getBoard().visitRange(agentLoc, targeting.getRange(), false, visitor);
            dir = getDirectionToCell(agentLoc, visitor.getTargetedCell());
            targetCell = visitor.getTargetedCell();
            actualDistance = visitor.getTargetedCellDistance();
        }
        if (dir != null) {
            // Agent is either trying to flee target or is trying to stay at least a 
            // certain number of cells away, so reverse direction from target. Further, 
            // in the "find player quick" case, tcd and distance are 0 and -1, respectively.
            if (targeting.getFleePlayer() || actualDistance <= targeting.getDistance()) {
                dir = dir.getReverseDirection();
            }
            // Now find a viable path to the target and return it
            dir = findPathInDirection(agentLoc, agent, targetCell, dir, targeting);
            if (dir != null) {
                return dir;
            }
        }
        // Cannot find a path toward (or away from) the target. Can we track? This isn't all
        // that useful, since if the target is in range, tracking is overridden whether the 
        // target is actually accessible or not. But when out of range, an agent can "pick up
        // the scent" and relocate to the right part of the board, so that's worth keeping.
        if (targeting.getTrackPlayer()) {
            // Visitor should be safe to use here since it will have been reset above (tracking causes
            // targetsPlayerDirectly to be false.
            dir = getDirectionToCell(agentLoc, visitor.getAdjacentVisitedCell());
            if (dir != null && isMoveViable(agentLoc, agent, targetCell, dir, targeting)) {
                return dir;
            }
        }
        // If nothing else, move randomly.
        if (targeting.getMoveRandom()) {
            dir = TerrainUtils.getRandomDirection();
            if (dir != null && isMoveViable(agentLoc, agent, targetCell, dir, targeting)) {
                return dir;
            }
        }
        return null;
    }
    
    /**
     * Given targeting information and a desired direction to move in, return a 
     * direction that is the closest the agent can get to moving in the 
     * desired direction. 
     * @param agentLoc
     * @param agent
     * @param targetCell
     * @param direction
     * @param targeting
     * @return          the closest direction to the indicated direction that the agent 
     *                  is able to move 
     */
    public static Direction findPathInDirection(Cell agentLoc, Agent agent, Cell targetCell,
        Direction direction, Targeting targeting) {
        if (isMoveViable(agentLoc, agent, targetCell, direction, targeting)) {
            return direction;
        }
        int[] pathways = (Random.nextInt(2) == 0) ? LEFT_FIRST : RIGHT_FIRST;
        Direction sideways = null;
        for (int i=0; i < pathways.length; i++) {
            sideways = getDirectionToSide(agentLoc, direction, pathways[i]);
            if (isMoveViable(agentLoc, agent, targetCell, sideways, targeting)) {
                return sideways;
            }
        }
        return null;
    }
    
    /**
     * This is basically an even cleverer version of Cell.canEnter() that prevents 
     * agents from moving into bullets. If you accepted that this should always be
     * true (but some agents like tumbleweeds, I don't think that's the case), then
     * you  wouldn't need this method.
     * @param from
     * @param agent
     * @param targetCell    the ultimate target of the move. Can be null, if it is 
     *                      no attempt will be made to avoid moving into a straight
     *                      line relative to the target
     * @param dir
     * @param targeting
     * @return  true if the agent can move in the indicated direction, including a move
     *          into the player to attack, if relevant to the agent's targeting; false
     *          otherwise
     */
    private static boolean isMoveViable(Cell from, Agent agent, Cell targetCell, Direction dir, 
        Targeting targeting ) { // 
        Cell next = from.getAdjacentCell(dir);
        if (next == null) {
            return false;
        }
        /* The problem with this is that it can change the board before
         * any move has been decided. This isn't good. It's not been exhaustively 
         * tested that canEnter/canExit account for the player in all circumstances.
        if (agent.is(PLAYER)) {
            Event e = Game.get().createEvent();
            from.getTerrain().onExit(e, (Player)agent, from, dir);
            next.getTerrain().onEnter(e, (Player)agent, next, dir);
            return (!e.isCancelled());
        }
        */
        if (!from.getTerrain().canExit(agent, from, dir)) {
            return false;
        }
        if (!next.canEnter(next, agent, dir, targeting.getAttackPlayer())) {
            return false;
        }
        // If avoiding LOF, and there's a target cell (Tumbleweed doesn't provide one), 
        // and the target cell's agent is the player... this is all pretty damn complicated.
        // We do want instanceof Player here... partly because we downcast but also because
        // if the player has been turned to stone or whatever (AgentProxy), he/she isn't
        // going to attack.
        if (targetCell != null && 
            targetCell.getAgent() != null && 
            targetCell.getAgent() instanceof Player && 
            Random.nextInt(100) < targeting.getDodgeBullets()) {
            
            // If the player is wielding a ranged weapon, don't move into the line of fire.
            // Not necessarily the player, however! Could be paralyzed
            Player player = (Player)targetCell.getAgent();
            Item selected = player.getBag().getSelected();
            // Could use selected.is(WEAPON)&& selected.not(MELEE_WEAPON) if we run out of bits
            if ((selected.is(RANGED_WEAPON) || selected instanceof Grenade) &&
                isStraightPath(next, targetCell)) {
                return false;
            }
        }
        // Don't wander into a bullet...
        if (!next.hasNoEffects()) {
            for (int i=0; i < next.getEffects().size(); i++) {
                Effect e = next.getEffects().get(i);
                if (e.is(AMMUNITION)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static Direction getDirectionToSide(Cell agentLoc, Direction dir, int step) {
        int index = Direction.getAdjacentDirections().indexOf(dir);
        int side = index + step;
        if (side < 0) {
            side += 8;
        } else if (side > 7) {
            side -= 8;
        }
        return Direction.getAdjacentDirections().get(side);
    }
    /**
     * Determine a direction toward the provided target cell from the origin cell, 
     * accounting for the range specified in the targeting parameter. This method 
     * does adjust direction if there is an obstacle in the way. 
     * @param origin
     * @param target
     * @param targeting
     * @return  the direction toward the target cell, or null if the target cell 
     *          is too far away to target
     */
    public static Direction getDirectionToCellRangeLimited(Cell origin, Cell target, Targeting targeting) {
        int distance = targeting.getRange();
        double d = getDistance(origin, target);
        return (d <= distance) ? getDirectionToCell(origin, target) : null;
    }
    
    public static double getDistance(Cell cell1, Cell cell2) {
        int diffX = (cell2.getX() - cell1.getX());
        int diffY = (cell2.getY() - cell1.getY());
        return (Math.sqrt( Math.pow(diffX, 2) + Math.pow(diffY, 2)));
    }
    
    /**
     * Test if the two cells are in a "straight path" between one another
     * (horizontal, vertical, or diagonal). 
     * @param cell1
     * @param cell2
     * @return  true if there's a line of fire between the two cells
     */
    private static boolean isStraightPath(Cell cell1, Cell cell2) {
        double d = getDistance(cell1, cell2);
        if (d > 1) {
            return (cell1.getX() == cell2.getX() || 
                    cell1.getY() == cell2.getY() || 
                   (cell1.getX() - cell2.getX()) == (cell1.getY() - cell2.getY()));
        }
        // Ignore this when very close to the player, because by the point you
        // touch the player, every square is a straight line between the cells.
        return false;
    }
    
    /**
     * Given two cells, determine the direction that will take you from the 
     * origin closer to the target cell.
     * @param origin
     * @param target
     * @return      direction from origin to target cell
     */
    public static Direction getDirectionToCell(Cell origin, Cell target) {
        if (target == null) {
            return null;
        }
        int x1 = origin.getX();
        int y1 = origin.getY();
        
        int x2 = target.getX();
        int y2 = target.getY();
        
        if (x1 == x2 && y1 < y2) {
            return Direction.SOUTH;
        } else if (x1 == x2 && y1 > y2) {
            return Direction.NORTH;
        } else if (x1 < x2 && y1 == y2) {
            return Direction.EAST;
        } else if (x1 > x2 && y1 == y2) {
            return Direction.WEST;
        } else if (x1 > x2 && y1 > y2) {
            return Direction.NORTHWEST;
        } else if (x1 < x2 && y1 < y2) {
            return Direction.SOUTHEAST;
        } else if (x1 < x2 && y1 > y2) {
            return Direction.NORTHEAST;
        }
        return Direction.SOUTHWEST;
    }
}
