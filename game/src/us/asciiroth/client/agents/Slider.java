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

import static us.asciiroth.client.core.Flags.PUSHABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * A puzzle piece that can slide up-down or left-right when pushed. 
 * It can be pushed behind a boulder, but will not push a boulder. 
 * It will fall into a pit.  
 *
 */
public class Slider extends AbstractAgent {

    // 8661, 8660, but the easterly one looks like crap for me
    private final Direction direction;

    /**
     * Constructor.
     * @param dir   either "north" or "east"
     */
    public Slider(Direction dir) {
        super("Slider", PUSHABLE, Color.NONE, CombatStats.INDESTRUCTIBLE,
            new Symbol(
            (dir == Direction.NORTH) ? "&#8597;" :
            (dir == Direction.EAST) ? "&#8596;" :
            null, Color.VIOLET, null, Color.MAROON, null)
        );
        if (dir == null) {
            throw new RuntimeException("Pusher direction cannot be null");
        }
        this.direction = dir;
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        if ((direction == Direction.NORTH && dir.isNorthSouth()) ||
            (direction == Direction.EAST && dir.isEastWest())) {
            Cell cell = agentLoc.getAdjacentCell(dir);
            Game.get().agentMove(event, cell, this, dir);
            // Not actually a pushable, since they can push each other, which 
            // boulders can't do.
            // super.onHitBy(event, agentLoc, agent, dir);
        } else {
            event.cancel();
        }
    }

    /** Type serializer. */
    public static final Serializer<Slider> SERIALIZER = new BaseSerializer<Slider>() {
        public Slider create(String[] args) {
            return new Slider(Direction.byName(args[1]));
        }
        public Slider example() {
            return new Slider(Direction.NORTH);
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(Slider s) {
            return "Slider|"+s.direction.getName();
        }
        public String template(String type) {
            return "Slider|{direction}";
        }
    };
}
