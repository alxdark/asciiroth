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

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.NEARBLACK;
import static us.asciiroth.client.core.Color.SALMON;
import static us.asciiroth.client.core.Color.STEELBLUE;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.items.Bullet;

/**
 * A terrain type that shoots some form of ammunition (actually, it'll shoot 
 * anything... for what that's worth), in a given direction, or in a random 
 * direction if the direction <code>None</code> is specified. 
 *
 */
public class Shooter extends AbstractTerrain implements Animated, ColorListener {

    private final Direction direction;
    private final State state;
    private final Item ammo;
    
    /**
     * Constructor.
     * @param ammo  the item to shoot
     * @param direction
     * @param color
     * @param state
     */
    private Shooter(Item ammo, Direction direction, Color color, State state) {
        super("Shooter", 0, color, 
            new Symbol("&#1078;", "&#8903;", SALMON, NEARBLACK, SALMON, BUILDING_WALL));
        if (state == null) {
            throw new RuntimeException("Shooter created without a state");
        }
        if (ammo == null) {
            throw new RuntimeException("Shooter ammo cannot be null");
        }
        this.direction = direction;
        this.state = state;
        this.ammo = ammo;
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        TerrainUtils.toggleCellState(cell, this, state);
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        // Allow this so bullets can leave.
        if (flier.getItem() != ammo) {
            event.cancel();
        }
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 5 == 0 && state.isOn()) {
            Direction dir = (direction == Direction.NONE) ? 
                TerrainUtils.getRandomDirection() : direction;
            Game.get().shoot(Game.get().createEvent(), cell, this, this.ammo, dir);
        }
    }
    /** Type serializer. */
    public static final Serializer<Shooter> SERIALIZER = new BaseSerializer<Shooter>() {
        public Shooter create(String[] args) {
            return new Shooter(unescItem(args[1]), Direction.byName(args[2]), 
                Color.byName(args[3]), State.byName(args[4]));
        }
        public String store(Shooter s) {
            return Util.format("Shooter|{0}|{1}|{2}|{3}", 
                esc(s.ammo), s.direction.getName(), s.color.getName(), s.state.getName());
        }
        public Shooter example() {
            return new Shooter(new Bullet(), Direction.NONE, STEELBLUE, State.ON);
        }
        public String template(String key) {
            return "Shooter|{ammo}|{direction}|{color}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
