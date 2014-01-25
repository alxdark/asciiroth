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

import static us.asciiroth.client.core.Color.DARKGRAY;
import static us.asciiroth.client.core.Color.DARKSLATEGRAY;
import static us.asciiroth.client.core.Color.LIGHTSLATEGRAY;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * An altar. Unlike Nethack, this altar doesn't do anything, it's purely ornamental.
 * It's a good place to put an important item in the game, to give it a little more
 * pizzaz (and of course, utility terrains can be used to create an altar that 
 * actually does something).
 * <p>
 * A full altar on the map consists of three altar pieces, with a direction of WEST, 
 * NONE and EAST, arranged horizontally. The appearance is a lower-case pi symbol 
 * between two brackets facing inward toward the pi symbol.
 *
 */
public class Altar extends AbstractTerrain {

    private final Direction direction;
    
    /**
     * Constructor.
     * @param direction
     */
    public Altar(Direction direction) {
        super("Altar", (PENETRABLE | TRAVERSABLE), NONE, 
            (direction == Direction.WEST) ? new Symbol("[", DARKGRAY, DARKSLATEGRAY) :
            (direction == Direction.EAST) ? new Symbol("]", DARKGRAY, DARKSLATEGRAY) :
            (direction == Direction.NONE) ? new Symbol("&pi;", LIGHTSLATEGRAY, DARKSLATEGRAY) :
            null);
        this.direction = direction;
    }
    /** Typer serializer. */
    public static final Serializer<Altar> SERIALIZER = new BaseSerializer<Altar>() {
        public Altar create(String[] args) {
            return new Altar(Direction.byName(args[1]));
        }
        public Altar example() {
            return new Altar(Direction.WEST);
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(Altar a) {
            return "Altar|"+a.direction.getName();
        }
        public String template(String type) {
            return "Altar|{direction}";
        }
    };
}
