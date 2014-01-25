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

import static us.asciiroth.client.core.Color.BURLYWOOD;
import static us.asciiroth.client.core.Color.BURNTWOOD;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import us.asciiroth.client.agents.ImmobileAgent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;

/**
 * A fence, which the player actually can't jump, but you can fire over it.
 *
 */
public class Fence extends ImmobileAgent {

    private final Direction direction;
    
    public Fence(Direction direction) {
        super("Fence", PENETRABLE, new Symbol(
            (direction == Direction.EAST) ? "=" :
            (direction == Direction.NORTH) ? "I" :
            "&Dagger;", BURLYWOOD, null, BURNTWOOD, null));
        this.direction = direction;
        if (direction != Direction.NORTH && direction != Direction.EAST && direction != Direction.NONE) {
            throw new RuntimeException("Fence direction must be north, east or none.");
        }
    }
    /** Type serializer. */
    public static final Serializer<Fence> SERIALIZER = new BaseSerializer<Fence>() {
        public Fence create(String[] args) {
            return new Fence(Direction.byName(args[1]));
        }
        public Fence example() {
            return new Fence(Direction.EAST);
        }
        public String getTag() {
            return "Outside Terrain";
        }
        public String store(Fence f) {
            return "Fence|"+f.direction.getName();
        }
        public String template(String type) {
            return "Fence|{direction}";
        }
    };
}
