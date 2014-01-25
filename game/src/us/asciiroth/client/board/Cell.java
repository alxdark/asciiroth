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
package us.asciiroth.client.board;

import static us.asciiroth.client.core.Flags.HIDES_ITEMS;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.PLAYER;

import java.util.ArrayList;
import java.util.List;

import us.asciiroth.client.Registry;
import us.asciiroth.client.agents.AgentUtils;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.ModifiableSymbol;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.client.effects.Fire;
import us.asciiroth.client.effects.Open;
import us.asciiroth.client.event.Events;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;

public class Cell {
    
    protected Agent agent;
    
    protected ItemBag bag;
    
    protected EffectBag effects;
    
    /**
     * The board.
     */
    protected Board board;
    /**
     * The terrain for this cell;
     */
    protected Terrain terrain;
    /**
     * The x/column coordinate of this cell.
     */
    protected int x;
    /**
     * The y/row coordinate of this cell.
     */
    protected int y;
    /**
     * When was this cell last visited by the player, as an 
     * incremented count. Used for breadcrumb path-finding.
     */
    protected int visited;
    
    
    private Element td;
    /**
     * Constructor for use of a cell as a map key only, where only
     * the equals and hashCode methods are used. 
     * @param x
     * @param y
     */
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Full constructor. 
     * @param renderer - a view component that can render this cell.
     * @param board
     * @param x
     * @param y
     */
    Cell(Board board, Element td, int x, int y) {
        this.board = board;
        this.td = td;
        this.x = x;
        this.y = y;
    }
    
    public Cell init(Board board) {
        this.board = board;
        this.visited = 0;
        return this;
    }
    
    public Element getTd() {
        return td;
    }
    
    /**
     * 
     * @return  the board containing this cell
     */
    public Board getBoard() {
        return board;
    }
    /**
     * 
     * @return  the x/column coordinate of this cell
     */
    public int getX() {
        return x;
    }
    /**
     * 
     * @return  the y/row coordinate of this cell
     */
    public int getY() {
        return y;
    }
    /**
     * The timestamp of the last time the player visited this 
     * cell, during the current occupation of the board by the
     * player (the information is wiped out when the player 
     * leaves the board).
     * @return  the time in millis since the epoch (the higher
     *          the number, the more recently visited)
     */
    public double getVisited() {
        return visited;
    }
    /**
     * Are there effects (thrown or shot pieces) in this cell. This method will not create 
     * a bag for the cell as a side effect of the check, and so it preferable to using 
     * <code>getEffects().isEmpty()</code>.
     * 
     * @return  true if there are effects in this cell, false otherwise
     */
    public boolean hasNoEffects() {
        return (effects == null || effects.isEmpty());
    }
    /**
     * The thrown or shot objects currently over this cell.
     * @return  the effects currently occuping this cell.
     */
    public EffectBag getEffects() {
        if (effects == null) {
            effects = new EffectBag(this);
        }
        return effects;
    }
    /**
     * Check to see if there are items in this cell. Does not create a bag for the 
     * cell as a side effect of the check, and so is preferable to <code>getBag().isEmpty()</code>.
     * 
     * @return  true if there are no items in this cell, false otherwise
     */
    public boolean isBagEmpty() {
        return (bag == null || bag.isEmpty());
    }
    /**
     * 
     * @return  the items lying on the ground in this cell
     */
    public ItemBag getBag() {
        if (bag == null) {
            bag = new ItemBag(this);
        }
        return bag;
    }
    
    public void onSteppedOn(Event event, Cell agentLoc, Agent agent) {
        ItemBag bag = getBag();
        for (Bag.Entry<Item> entry : bag.asEntryList()) {
            entry.getPiece().onSteppedOn(event, agentLoc, agent);
        }
    }
    
    /**
     * @return  the agent currently occupying this square, or null if empty
     */
    public Agent getAgent() {
        return agent;
    }
    
    /**
     * Does this cell contain the player? This method is more effective than testing 
     * the agent using the <code>instanceof</code> operator because it accounts for 
     * the fact that the player may be wrapped by an <code>AgentProxy</code>. It is 
     * analogous to getApparentTerrain() in this regard.
     * 
     * @return  true if the agent is the player or a proxy that proxies the player
     */
    public boolean containsPlayer() {
        return (this.x == board.playerX && this.y == board.playerY); 
    }
    
    /**
     * Set the agent occupying this square. To clear the cell of an agent, use
     * <code>removeAgent</code>. An existing agent on this square will be removed, 
     * so you must be careful when calling this method. The controller methods 
     * include logic for pushing agents, and similar behavior, that cannot be 
     * expressed here. 
     * 
     * @param agent
     * 
     */
    public void setAgent(Agent agent) {
        if (agent == null) {
            throw new RuntimeException("Can't set agent to null; use removeAgent");
        }
        removeAgent(this.agent);
        this.agent = agent;
        if (agent instanceof Animated) {
            board.addAnimated(x, y, (Animated)agent);
        }
        if (agent.is(PLAYER)) {
            board.playerX = x;
            board.playerY = y;
            visited = board.visitCount++; // new Date().getTime();
        }
        Events.get().fireCellChanged(this);
    }
    /**
     * Remove the agent current on this square. Safe to use outside of a controller
     * method method.
     * 
     * @param agent
     */
    public void removeAgent(Agent agent) {
        if (agent != null) {
            if (agent instanceof Animated) {
                board.removeAnimated(x, y, (Animated)agent);
            }
            this.agent = null;
            Events.get().fireCellChanged(this);
        }
    }
    /**
     * Move an agent to another cell. This method is more efficient than removing
     * and then adding the agent to another cell, and should be used for true move
     * operations.
     * 
     * @param next  the cell to move the agent to
     * @param agent the agent to be moved
     */
    public void moveAgentTo(Cell next, Agent agent) {
        if (agent != null) {
            this.agent = null;
            next.agent = agent;
            if (agent instanceof Animated) {
                board.moveAnimated(x, y, agent, next.getX(), next.getY());
            }
            if (agent.is(PLAYER)) {
                board.playerX = next.getX();
                board.playerY = next.getY();
                next.visited = board.visitCount++; // new Date().getTime();
            }
        }
        Events.get().fireCellChanged(this);
        Events.get().fireCellChanged(next);
    }
    
    public List<Cell> getAdjacentCells(CellFilter filter) {
        List<Cell> adjCells = new ArrayList<Cell>();
        for (int i=0, len = Direction.getAdjacentDirections().size(); i < len; ++i) {
            Direction dir = (Direction)Direction.getAdjacentDirections().get(i);
            Cell adj = getAdjacentCell(dir);
            if (adj != null && (filter == null || filter.matches(adj, dir))) {
                adjCells.add(adj);
            }
        }
        return adjCells;
    }
    
    /**
     * Get the cell adjacent to the current cell in the indicated direction. Returns null 
     * if the direction is up or down.
     * 
     * @param   direction - the direction in which the adjacent cell is located, 
     *                  relative to this cell
     * @return  the adjacent cell, or null if the direction is up or down
     */
    public Cell getAdjacentCell(Direction direction) {
        // 2/28/2008: Added vertical check, which means now up/down = null. 
        return (direction == null || direction.isVertical()) ? 
            null : board.getAdjacentCell(getX(), getY(), direction);
    }
    /**
     * Get the terrain of this cell.
     * @return  the terrain of this cell
     */
    public Terrain getTerrain() {
        return terrain;
    }
    
    /**
     * Get the terrain of this cell as it appears to the player. Terrain 
     * behavior is augmented through the use of the Decorator pattern, 
     * so the terrain returned from getTerrain is not always the terrain
     * as it is displayed to the user or as it behaves 
     * 
     * @return  the terrain for this cell or the proxied terrain, if the 
     *          terrain implements the TerrainProxy interface.
     */
    public Terrain getApparentTerrain() {
        if (terrain instanceof TerrainProxy) {
            return ((TerrainProxy)terrain).getProxiedTerrain();
        }
        return terrain;
    }
    
    public void setTerrain(Terrain newTerrain) {
        setTerrain(newTerrain, false);
    }
    
    /**
     * Set the terrain for this cell. If the new terrain is not a proxy, and 
     * the existing terrain is a proxy, then the final terrain will be a proxy 
     * with the same behavior, but the new terrain will be the proxied terrain.
     * From the player's perspective, the terrain will have changed but the 
     * decorated behavior will remain. If the new terrain is a proxy, then 
     * it will simply replace the existing terrain, whatever it is. But replacing 
     * one proxy with another proxy can be considered an error.
     * 
     * @param newTerrain
     * @param forceProxyReplace force any proxy in this cell to be replaced 
     *          (so far only the editor needs to do this)
     */
    public void setTerrain(Terrain newTerrain, boolean forceProxyReplace) {
        if (!forceProxyReplace && terrain instanceof TerrainProxy && !(newTerrain instanceof TerrainProxy)) {
            newTerrain = ((TerrainProxy)terrain).proxy(newTerrain);
            newTerrain = Registry.get().cache(newTerrain);
        }
        
        // Remove terrain or the terrain it wraps, if either is animated.
        if (terrain instanceof Animated) {
            board.removeAnimated(x, y, (Animated)terrain);
        }
        if (terrain instanceof TerrainProxy) {
            Terrain inner = ((TerrainProxy)terrain).getProxiedTerrain();
            if (inner instanceof Animated) {
                board.removeAnimated(x, y, (Animated)inner);
            }
        }
        // Add either terrain or the terrain it wraps, if either is animated.
        if (newTerrain instanceof Animated) {
            board.addAnimated(x, y, (Animated)newTerrain);
        }
        if (newTerrain instanceof TerrainProxy) {
            Terrain inner = ((TerrainProxy)newTerrain).getProxiedTerrain();
            if (inner instanceof Animated) {
                board.addAnimated(x, y, (Animated)inner);
            }
        }
        terrain = newTerrain;
        Events.get().fireCellChanged(this);
    }
    
    /**
     * Can the agent enter this cell, accounting both for the terrain type and the presence
     * of other agents, as well as the intent of the agent (attack player vs. just move 
     * while avoiding all other agents). This isn't absolutely perfect, but should account
     * for most situations. 
     * @param agentLoc
     * @param agent
     * @param targetPlayer
     * @return      true if the agent can move to the indicated cell
     */
    public boolean canEnter(Cell agentLoc, Agent agent, Direction dir, boolean targetPlayer) {
        // Failing as fast as possible here...
        if (!agent.canEnter(dir, agentLoc, this)) {
            return false;
        }
        if (!(getAgent() == null || (getAgent().is(PLAYER) && targetPlayer))) {
            return false;
        }
        if (!terrain.canEnter(agent, this, dir)) {
            return false;
        }
        return true;
    }

    /**
     * Animation and related behavior for opening a crate or chest.
     * @param string    the name of the container
     * @param item      the item in the container (can be null)
     * @param c         the class of terrain being opened (Chest or Crate)
     */
    /*
    public void openContainer(final String string, final Item item, Class<?> c) {
        // First we show the opening effect, then under it we immediately change 
        // the terrain so the crate cannot be triggered multiple times. The 
        // rubble terrain keeps the player out until the animation is done. 
        // Then the item and a message are shown.
        final Cell that = this;
        getEffects().add(new Open(new Command() {
            public void execute() {
                if (item != null) {
                    getBag().add(item);
                    Events.get().fireMessage(that, item.getIndefiniteNoun("You find {0} inside"));
                } else {
                    Events.get().fireMessage(that, "The "+string+" is empty"); 
                }
            }
        }));
        setTerrain((Terrain)Registry.get().getPiece(c));
    }
    */

    /**
     * Animation and related behavior for opening a crate or chest.
     * @param string    the name of the container
     * @param item      the item in the container (can be null)
     * @param count     the number of items in the container
     * @param c         the class of terrain being opened (Chest or Crate)
     */
    public void openContainer(final String string, final Item item, final int count, Class<?> c) {
        // First we show the opening effect, then under it we immediately change 
        // the terrain so the crate cannot be triggered multiple times. The 
        // rubble terrain keeps the player out until the animation is done. 
        // Then the item and a message are shown.
        final Cell that = this;
        getEffects().add(new Open(new Command() {
            public void execute() {
                if (item != null) {
                    for (int i=0; i < count; i++) {
                        getBag().add(item);    
                    }
                } else {
                    Events.get().fireMessage(that, "The "+string+" is empty"); 
                }
            }
        }));
        setTerrain((Terrain)Registry.get().getPiece(c));
    }
    
    public void explosion(final Player player) {
        final Fire fire = (Fire)Registry.get().getPiece(Fire.class);
        getAdjacentCells(new CellFilter() {
            public boolean matches(Cell cell, Direction from) {
                if (cell.getTerrain().canEnter(player, cell, from)) {
                    cell.getEffects().add(fire);   
                }
                return false;
            }
        });
        getEffects().add(fire);
    }
    
    public void createCloud(Class<? extends Effect> c) {
        final Effect cloud = (Effect)Registry.get().getPiece(c);
        final Cell origin = this;
        board.visitRange(this, 4, true, new CellVisitor() {
            public boolean visit(Cell cell, int range) {
                if (cell.getTerrain().is(PENETRABLE) && 
                    AgentUtils.getDistance(origin,cell) <= 2.5) {
                    cell.getEffects().add(cloud);
                }
                return true;
            }
        });
    }
    
    public boolean hasOpeningEffect() {
        if (!getEffects().isEmpty()) {
            for (int i=0; i < effects.size(); i++) {
                Effect e = effects.get(i);
                if (e instanceof Open) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Accounting for the layering of pieces on this cell, what would the 
     * union of the pieces be in a symbol? 
     * @param piece
     * @param symbol
     * @return  a symbol showing the union of all pieces on the cell.
     */
    public ModifiableSymbol getCurrentSymbol(Piece piece, Symbol symbol) {
        ModifiableSymbol sym = new ModifiableSymbol();
        boolean outside = board.isOutside();
        
        if (piece instanceof Terrain) {
            sym.addSymbol(symbol, outside);
        } else {
            // This used to be terrain.getApparentSymbol().getSymbol()...
            // but it should never be necessary to do that on the appearance side.
            // It's the instanceof tests that won't work without getApparentTerrain().
            sym.addSymbol(getTerrain().getSymbol(), outside);
        }
        // Some terrains (e.g. hay) will hide anything that is on them.
        if (getTerrain().not(HIDES_ITEMS)) {
            if (piece instanceof Item) {
                sym.addSymbol(symbol, outside);
            } else {
                Item item = (!isBagEmpty()) ? getBag().last() : null;
                sym.addPiece(item, outside);
            }
        }
        
        Effect effect = (!hasNoEffects()) ? getEffects().last() : null;
        
        // If the piece is an under effect or the top effect is an under effect...
        if (piece instanceof Effect && !((Effect)piece).isAboveAgent()) {
            sym.addSymbol(symbol, outside);
        } else if (effect != null && !effect.isAboveAgent()){
            sym.addPiece(effect, outside);
        }
        
        if (piece instanceof Agent) {
            sym.addSymbol(symbol, outside);
        } else {
            Agent agent = getAgent();
            sym.addPiece(agent, outside);
        }

        if (piece instanceof Effect && ((Effect)piece).isAboveAgent()) {
            sym.addSymbol(symbol, outside);
        } else if (effect != null && effect.isAboveAgent()){
            sym.addPiece(effect, outside);
        }
        return sym;
    }
    @Override
    public int hashCode() {
        int result = 97;
        result += x;
        result += y;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cell)) {
            return false;
        }
        Cell that = (Cell)obj;
        return (that.x == this.x && that.y == this.y);
    }
}    
