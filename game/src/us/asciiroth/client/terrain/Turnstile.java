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
package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.STEELBLUE;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.event.Events;

/**
 * Creates a one-way passage to either the west or the east. Agent must enter 
 * straight on, like a door, but once inside, you must move east or west, and 
 * you can't go back the way you came. Pushables can be pushed through a 
 * turnstile, but they follow the same rules. Flashes its trigger color.
 *
 */
public class Turnstile extends AbstractTerrain implements Animated, ColorListener {

    private final Direction direction;
    private final Symbol coloredSymbol;
    
    /**
     * Constructor.
     * @param direction
     * @param color
     */
    private Turnstile(Direction direction, Color color) {
        super("Turnstile", 0, color, 
            (direction == Direction.WEST) ? 
                new Symbol("&#171;", WHITE, BLACK, BLACK, BUILDING_FLOOR) :
                new Symbol("&#187;", WHITE, BLACK, BLACK, BUILDING_FLOOR)
        );
        if (direction != Direction.WEST && direction != Direction.EAST) {
            throw new RuntimeException("Turnstiles must specify west/east travel");
        }
        this.coloredSymbol = (direction == Direction.WEST) ?
            new Symbol("&#171;", color, null, color, BUILDING_FLOOR) :
            new Symbol("&#187;", color, null, color, BUILDING_FLOOR);
        this.direction = direction;
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        Terrain newTerrain = TerrainUtils.getTerrainWithReverseDirection(cell, direction);
        cell.setTerrain(newTerrain);
    }
    /**
     * Get the direction this turnstile allows an agent to move (east or west), 
     * without the ability to return. 
     * @return  the one-way direction of the turnstile.
     */
    public Direction getDirection() {
        return direction;
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        // this.direction must be EAST or WEST
        return (this.direction == direction);
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        // this.direction must be EAST or WEST
        return (this.direction == direction);
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (!canEnter(player, cell, dir)) {
            event.cancel();
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (!canExit(player, cell, dir)) {
            event.cancel();
        }
    }
    @Override
    public void onAgentEnter(Event event, Agent agent, Cell cell, Direction dir) {
        if (!canEnter(agent, cell, dir)) {
            event.cancel();
        }
    }
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
        if (!canExit(agent, cell, dir)) {
            event.cancel();
        }
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (!canEnter(null, null, flier.getDirection())) {
            event.cancel();
        }
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 30 == 0) {
            Events.get().fireRerender(cell, this, symbol);
        } else if (frame % 15 == 0) {
            Events.get().fireRerender(cell, this, coloredSymbol);
        }
    }
    /** Type serializer. */
    public static final Serializer<Turnstile> SERIALIZER = new Serializer<Turnstile>() {
        public Turnstile create(String[] args) {
            return new Turnstile(Direction.byName(args[1]), Color.byName(args[2]));
        }
        public String store(Turnstile t) {
            return Util.format("Turnstile|{0}|{1}", t.direction.getName(), t.color.getName());
        }
        public Turnstile example() {
            return new Turnstile(Direction.WEST, STEELBLUE);
        }
        public String template(String key) {
            return "Turnstile|{direction}|{color}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
