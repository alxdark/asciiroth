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

import static us.asciiroth.client.core.Color.DARK_PIER;
import static us.asciiroth.client.core.Color.PIER;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;

/**
 * A bridge you can walk over. We used to use the Pier piece in early maps to 
 * create bridges, but it was ugly even by the standards of ASCII art. This is
 * a little more bridgish.
 *
 */
public class Bridge extends AbstractTerrain {
    
    private final Terrain terrain;
    
    /** Constructor. */
    private Bridge(Terrain terrain) {
        super("Bridge", (TRAVERSABLE | PENETRABLE), 
            new Symbol("&equiv;", "&#8803;", DARK_PIER, terrain.getSymbol().getBackground(false),
                PIER, terrain.getSymbol().getBackground(true)));
        this.terrain = terrain;
    }
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return !direction.isDiagonal();
    }
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return !direction.isDiagonal();
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal() || dir.isVertical()) {
            event.cancel();
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (dir.isDiagonal() || dir.isVertical()) {
            event.cancel();
        }
    }
    
    public static final Serializer<Bridge> SERIALIZER = new BaseSerializer<Bridge>() {
        public Bridge create(String[] args) {
            return new Bridge(unescTerrain(args[1]));
        }
        public String getTag() {
            return "Outside Terrain";
        }
        public Bridge example() {
            return new Bridge(new Water());
        }
        public String store(Bridge b) {
            return "Bridge|"+esc(b.terrain);
        }
        public String template(String type) {
            return "Bridge|{terrain}";
        }
    };
}
