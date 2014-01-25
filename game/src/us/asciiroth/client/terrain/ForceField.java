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

import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Color.STEELBLUE;
import static us.asciiroth.client.core.Color.VIOLET;
import static us.asciiroth.client.core.Color.YELLOW;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.EmptyHanded;

/**
 * A force field will not allow the player to pass through it while keeping any 
 * items in his or her inventory. They are all dropped on the cell from which the
 * player entered. The purpose of the piece is simply to allow for sub-regions of
 * the board where existing inventory doesn't apply, to arrange a certain puzzle 
 * for example. 
 *
 */
public class ForceField extends AbstractTerrain implements ColorListener, Animated {
    private static final Color[] COLORS = new Color[] {
        RED, YELLOW, ORANGE, VIOLET
    };
    
    private final Direction direction;
    private final State state;
    private final Terrain terrain;
    
    public ForceField(Direction direction, Color color, State state) {
        this(((Terrain)Registry.get().getPiece(Floor.class)), direction, color, state);
    }
    public ForceField(Terrain terrain, Direction direction, Color color, State state) {
        // Only show the off-state because that's simpler, to start. This is 
        // immediately re-rendered correctly by the animation timeline.
        super("Force Field", (TRAVERSABLE | PENETRABLE), color, state.isOff() ? 
            terrain.getSymbol(): 
            (direction == Direction.NORTH) ? 
                new Symbol("|", RED, terrain.getSymbol().getBackground(false), RED, terrain.getSymbol().getBackground(true)) : 
                new Symbol("&mdash;", RED, terrain.getSymbol().getBackground(false), RED, terrain.getSymbol().getBackground(true))
        );
        this.direction = direction;
        this.terrain = terrain;
        this.state = state;
    }

    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        TerrainUtils.toggleCellState(cell, this, state);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (state.isOff()) {
            return;
        }
        boolean outside = ctx.getBoard().isOutside();
        Symbol s = new Symbol((direction == Direction.NORTH) ? "|" : "&mdash;",
            COLORS[frame%4], terrain.getSymbol().getBackground(outside));
        Events.get().fireRerender(cell, this, s);
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (state.isOff() || player.getBag().size() == 1) {
            return;
        }
        PlayerBag bag = player.getBag();
        if (bag.size() == 1) { // empty-handed
            return;
        }
        for (int i = bag.size()-1; i >= 0; i--) {
            Item item = bag.get(i);
            if (!(item instanceof EmptyHanded)) {
                int count = bag.getCount(item);
                for (int j=0; j < count; j++) {
                    cell.getAdjacentCell(dir.getReverseDirection()).getBag().add(item);
                    bag.remove(item);
                }
            }
        }
        Events.get().fireModalMessage( 
            "All your stuff gets flung from your body. It's kind of embarassing");
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (state.isOn()) {
            event.cancel();
        }
    }
    
    /** Type serializer. */
    public static final Serializer<ForceField> SERIALIZER = new BaseSerializer<ForceField>() {
        public ForceField create(String[] args) {
            return (args.length == 4) ? 
                new ForceField(Direction.byName(args[1]), Color.byName(args[2]), State.byName(args[3])) : 
                new ForceField(unescTerrain(args[1]), Direction.byName(args[2]), Color.byName(args[3]), State.byName(args[4]));
        }
        public ForceField example() {
            return new ForceField(Direction.NORTH, STEELBLUE, State.ON);
        }
        public String store(ForceField ff) {
            return Util.format("ForceField|{0}|{1}|{2}|{3}", esc(ff.terrain), ff.direction.getName(), 
                ff.color.getName(), ff.state.getName());
        }
        public String template(String type) {
            return "ForceField|{terrain?}|{direction}|{color}|{state}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
