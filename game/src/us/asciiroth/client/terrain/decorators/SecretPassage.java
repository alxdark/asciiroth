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
package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.terrain.Floor;
import us.asciiroth.client.terrain.Wall;

/**
 * A very common kind of mimic, it looks like wall but actually it's a corridor. 
 * In addition, agents are fooled by its appearance and they will not walk into
 * a secret passage. 
 */
public class SecretPassage extends Mimic {

    /**
     * Constructor.
     * @param color
     */
    private SecretPassage() {
        super(
            (Terrain)Registry.get().getPiece(Wall.class),
            (Terrain)Registry.get().getPiece(Floor.class),
            Color.NONE
        );
    }
    /**
     * Agents are fooled and treat terrain as it appears.
     */
    @Override
    public boolean canEnter(Agent agent, Cell cell, Direction direction) {
        return appearsAs.canEnter(agent, cell, direction);
    }
    /**
     * Agents are fooled and treat terrain as it appears.
     */
    @Override
    public boolean canExit(Agent agent, Cell cell, Direction direction) {
        return appearsAs.canExit(agent, cell, direction);
    }
    /** Type serializer. */
    public static final Serializer<SecretPassage> SERIALIZER = new TypeOnlySerializer<SecretPassage>() {
        public SecretPassage create(String[] args) {
            return new SecretPassage();
        }
        public String getTag() {
            return "Terrain";
        }
    };
}
