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
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.AbstractPiece;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.client.effects.InFlightItem;

/**
 * A base class for terrain types that can augment the behavior of another terrain, 
 * looking like that terrain but behaving differently. Decorators receive color events, 
 * and if the wrapped terrain implements <code>ColorListener</code>, the wrapped piece 
 * will receive the color event as well.
 * <p>
 * TODO: Unlike agent proxy, this piece cannot augment the flags of the underlying piece...
 * it doesn't look like they would work at all.
 *  
 */
public abstract class Decorator extends AbstractPiece implements Terrain, TerrainProxy, ColorListener {
    
    /** The terrain that is decorated. */
    protected final Terrain terrain;
    
    public Decorator(Terrain terrain, String name, int flags, Color color, Symbol symbol) {
        super(name, flags, color, symbol);
        this.terrain = terrain;
    }
    public Decorator(Terrain terrain, int flags, Color color, Symbol symbol) {
        super(terrain.getName(), flags, color, symbol);
        this.terrain = terrain;
    }
    public Decorator(Terrain terrain, int flags, Symbol symbol) {
        this(terrain, flags, NONE, symbol);
    }
    public Decorator(Terrain terrain, int flags, Color color) {
        this(terrain, flags, color, terrain.getSymbol());
    }
    public Decorator(Terrain terrain, int flags) {
        this(terrain, flags, NONE, terrain.getSymbol());
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        if (terrain instanceof ColorListener) {
            ((ColorListener)terrain).onColorEvent(ctx, cell, origin);
        }
        onColorEventInternal(ctx, cell, origin);
    }
    public Terrain getProxiedTerrain() {
        return terrain;
    }
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return terrain.canEnter(agent, cell, direction);
    }
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return terrain.canExit(agent, cell, direction);
    }
    @Override
    public boolean is(int flag) {
        return terrain.is(flag);
    }
    @Override
    public boolean not(int flag) {
        return terrain.not(flag);
    }
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        terrain.onEnter(event, player, cell, dir);
        onEnterInternal(event, player, cell, dir);    
    }
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        terrain.onExit(event, player, cell, dir);
        onExitInternal(event, player, cell, dir);
    }
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        terrain.onAgentEnter(event, agent, cell, dir);
        onAgentEnterInternal(event, agent, cell, dir);    
    }
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        terrain.onAgentExit(event, agent, cell, dir);
        onAgentExitInternal(event, agent, cell, dir);
    }
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        terrain.onFlyOver(event, cell, flier);
        onFlyOverInternal(event, cell, flier);
    }
    public void onDrop(Event event, Cell cell, Item item) {
        terrain.onDrop(event, cell, item);
        onDropInternal(event, cell, item);
    }
    public void onPickup(Event event, Cell loc, Agent agent, Item item) {
        terrain.onPickup(event, loc, agent, item);
        onPickupInternal(event, loc, agent, item);
    }
    public void onAdjacentTo(Context context, Cell cell) {
        terrain.onAdjacentTo(context, cell);
        onAdjacentToInternal(context, cell);
    }
    public void onNotAdjacentTo(Context context, Cell cell) {
        terrain.onNotAdjacentTo(context, cell);
        onNotAdjacentToInternal(context, cell);
    }
    /**
     * Override this method in subclasses in order to augment the onEnter 
     * behavior of the decorated terrain.
     * @param event
     * @param player
     * @param cell
     * @param dir
     */
    protected void onEnterInternal(Event event, Player player, Cell cell, Direction dir) {
    }
    /**
     * Override this method in subclasses in order to augment the onExit 
     * behavior of the decorated terrain.
     * @param event
     * @param player
     * @param cell
     * @param dir
     */
    protected void onExitInternal(Event event, Player player, Cell cell, Direction dir) {
    }
    /**
     * Override this method in subclasses in order to augment the onAgentEnter 
     * behavior of the decorated terrain.
     * @param event
     * @param agent
     * @param cell
     * @param dir
     */
    protected void onAgentEnterInternal(Event event, Agent agent, Cell cell, Direction dir) {
    }
    /**
     * Override this method in subclasses in order to augment the onExit 
     * behavior of the decorated terrain.
     * @param event
     * @param agent
     * @param cell
     * @param dir
     */
    protected void onAgentExitInternal(Event event, Agent agent, Cell cell, Direction dir) {
    }
    /**
     * Override this method in subclasses in order to augment the onFlyOver 
     * behavior of the decorated terrain.
     * @param event
     * @param cell
     * @param flier
     */
    protected void onFlyOverInternal(Event event, Cell cell, InFlightItem flier) {
    }
    /**
     * Override this method in subclasses in order to augment the onDrop
     * behavior of the decorated terrain.
     * @param event
     * @param cell
     * @param item
     */
    protected void onDropInternal(Event event, Cell cell, Item item) {
    }
    /**
     * Override this method in subclasses in order to augment the onPickup
     * behavior of the decorated terrain.
     * @param event
     * @param loc
     * @param agent
     * @param item
     */
    protected void onPickupInternal(Event event, Cell loc, Agent agent, Item item) {
    }
    /**
     * Override this method in subclasses in order to augment the onTrigger
     * behavior of the decorated terrain.
     * @param ctx
     * @param cell
     * @param origin
     */
    protected void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
    }
    /**
     * Override this method in subclasses in order to augment the onAdjacentTo
     * behavior of the decorated terrain.
     * @param context
     * @param cell
     */
    protected void onAdjacentToInternal(Context context, Cell cell) {
    }
    /**
     * Override this method in subclasses in order to augment the onNotAdjacentTo
     * behavior of the decorated terrain.
     * @param context
     * @param cell
     */
    protected void onNotAdjacentToInternal(Context context, Cell cell) {
    }
}
