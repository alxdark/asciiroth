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

import static us.asciiroth.client.core.Color.DARKGOLDENROD;
import static us.asciiroth.client.core.Color.GOLD;
import static us.asciiroth.client.core.Color.STEELBLUE;

import java.util.List;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.KillerBee;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellFilter;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.effects.InFlightItem;

/**
 * A special kind of <code>AgentCreator</code> that creates bees when it receives 
 * a color event, presumably fired from the death of another <code>KillerBee</code>
 * on the board. 
 *
 */
public class BeeHive extends AbstractTerrain implements ColorListener {

    private final State state;
    private final KillerBee bee;
    
    /**
     * Constructor.
     * @param color
     */
    public BeeHive(Color color, State state) {
        super("Bee Hive", 0, color, new Symbol("&#8962;", "&#8780;", DARKGOLDENROD, GOLD));
        this.state = state;
        this.bee = (KillerBee)Registry.get().getPiece("KillerBee|"+color.getName());
    }
    public BeeHive(Color color) {
        this(color, State.OFF);
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        if (cell.getAgent() == null) {
            cell.setAgent(this.bee);
        }
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return (agent instanceof KillerBee);
    }
    /**
     * You really shouldn't walk into a bee hive. These bees fight to the death.
     */
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        annoyHive(cell);
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        event.cancel();
        annoyHive(cell);
    }
    private void annoyHive(final Cell to) {
        if (state.isOff()) {
            List<Cell> cells = to.getAdjacentCells(new CellFilter() {
                public boolean matches(Cell cell, Direction dir) {
                    return cell.canEnter(to, bee, dir, false);
                }
            });
            for (Cell cell : cells) {
                cell.setAgent(this.bee);
            }
            TerrainUtils.toggleCellState(to, this, state);
        }
    }
    /**
     * The bees can get out, however.
     */
    @Override
    public void onAgentExit(Event event, Agent agent, Cell cell, Direction dir) {
    }
    public static final Serializer<BeeHive> SERIALIZER = new BaseSerializer<BeeHive>() {
        public BeeHive create(String[] args) {
            return (args.length == 3) ?
                new BeeHive(Color.byName(args[1]), State.byName(args[2])) :
                new BeeHive(Color.byName(args[1]));
        }
        public BeeHive example() {
            return new BeeHive(STEELBLUE, State.OFF);
        }
        public String store(BeeHive bh) {
            return Util.format("BeeHive|{0}|{1}", bh.color.getName(), bh.state.getName());
        }
        public String template(String type) {
            return "BeeHive|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
