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
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Color.DARKSEAGREEN;
import static us.asciiroth.client.core.Color.MEDIUMSEAGREEN;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.SEAGREEN;

import java.util.List;

import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellFilter;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

import com.google.gwt.user.client.Random;

/**
 * A creature that divides when killed through 3 generations before 
 * finally dying. Because of this exponential creation of critters, 
 * the Tetrite does not have a color and does not fire a color event
 * when it is killed.
 */
public class Tetrite extends AbstractAnimatedAgent {
    
    private static final Targeting TARGETING = 
        new Targeting().dodgeBullets(50).attackPlayer(14).moveRandomly();
    
    private final int generation;
    
    /**
     * Constructor.
     * @param generation    what generation is this Tetrite? (0-2)
     */
    public Tetrite(int generation) {
        super("Tetrite", (CARNIVORE | ORGANIC), NONE, CombatStats.TETRITE_CTBH,
            (generation == 0) ? new Symbol("&part;", SEAGREEN) :
            (generation == 1) ? new Symbol("&part;", MEDIUMSEAGREEN) :
            new Symbol("&part;", DARKSEAGREEN));
        this.generation = generation;
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.TETRITE_DAMAGE);
    }
    @Override
    public void onDie(Event event, Cell agentLoc) {
        if (generation == 0) {
            createTwoMore(agentLoc, Registry.get().cache(new Tetrite(1)));
        } else if (generation == 1) {
            createTwoMore(agentLoc, Registry.get().cache(new Tetrite(2)));
        }
    }
    private void createTwoMore(Cell center, Tetrite child) {
        final Tetrite tetrite = this;
        List<Cell> adj = center.getAdjacentCells(new CellFilter() {
            public boolean matches(Cell cell, Direction dir) {
                return cell.canEnter(cell, tetrite, dir, false);
            }
        });
        // Find the largest: 2 or the number of adjacent cells that can be filled
        int i = (adj.size() > 2) ? 2 : adj.size();
        while (i > 0) {
            Cell cell = adj.remove(Random.nextInt(adj.size()));
            cell.setAgent(child);
            i--;
        }
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 5 == 0) {
            Game.get().agentMove(cell, this, TARGETING);
        }
    }
    /** Type serializer. */
    public static final Serializer<Tetrite> SERIALIZER = new BaseSerializer<Tetrite>() {
        public Tetrite create(String[] args) {
            return (args.length == 1) ?
                new Tetrite(0) : new Tetrite(Integer.parseInt(args[1]));
        }
        public Tetrite example() {
            return new Tetrite(0);
        }
        public String store(Tetrite t) {
            return "Tetrite|"+Integer.toString(t.generation);
        }
        public String template(String type) {
            return "Tetrite";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
