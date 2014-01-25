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
import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.SILVER;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * A throne. Decorative.
 */
public class Throne extends AbstractTerrain {

    private final Direction direction;

    public Throne(Direction direction) { 
        super("Throne", (TRAVERSABLE | PENETRABLE), 
            new Symbol("&plusmn;",
                (direction == Direction.NORTH) ? "&#9691;" : "&#9690;",
                SILVER, BLACK, BUILDING_WALL, BUILDING_FLOOR));
        if (direction != Direction.NORTH && direction != Direction.SOUTH) {
            throw new RuntimeException("Throne must be north/south");
        }
        this.direction = direction;
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if ((direction == Direction.NORTH && dir != Direction.SOUTH) || 
            (direction == Direction.SOUTH && dir != Direction.NORTH)) {
            event.cancel();
        }
    }
    @Override
    public void onExit(Event event, Player player, Cell cell, Direction dir) {
        if (direction != dir) {
            event.cancel();
        }
    }
    public static final Serializer<Throne> SERIALIZER = new BaseSerializer<Throne>() {
        public Throne create(String[] args) {
            return new Throne(Direction.byName(args[1]));
        }
        public String getTag() {
            return "Room Features";
        }
        public Throne example() {
            return new Throne(Direction.NORTH);
        }
        public String store(Throne t) {
            return "Throne|"+t.direction.getName();
        }
        public String template(String type) {
            return "Throne|{direction}";
        }
    };
    
}
