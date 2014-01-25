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
package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.ChalkedFloor;
import us.asciiroth.client.terrain.Floor;

/**
 * The player can use the chalk to mark <code>ChalkedFloor</code> terrain (and only 
 * <code>ChalkedFloor</code> terrain). Presumably this could be big help in 
 * navigating large, maze-like areas of a scenario.  
 */
public class Chalk extends AbstractItem {

    private final ChalkedFloor chalkedFloor;
    private final Floor floor;
    
    /**
     * Constructor.
     */
    public Chalk() {
        super("Chalk", 0, new Symbol("-", WHITE, null, BLACK, null));
        this.chalkedFloor = (ChalkedFloor)Registry.get().getPiece(ChalkedFloor.class);
        this.floor = (Floor)Registry.get().getPiece(Floor.class);
    }
    
    @Override
    public void onUse(Event event) {
        Cell current = event.getBoard().getCurrentCell();
        if (current.getApparentTerrain() instanceof Floor) {
            current.setTerrain(chalkedFloor);
            Events.get().fireMessage(current, "You mark the floor");
        } else if (current.getApparentTerrain() instanceof ChalkedFloor) {
            current.setTerrain(floor);
            Events.get().fireMessage(current, "You wipe the chalk off the floor");
        }
    }
    /** Type serializer. */
    public static final Serializer<Chalk> SERIALIZER = new TypeOnlySerializer<Chalk>() {
        public Chalk create(String[] args) {
            return new Chalk();
        }
        public String getTag() {
            return "Mundane Items";
        }
    };
}
