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

import static us.asciiroth.client.core.Flags.AQUATIC;
import static us.asciiroth.client.core.Flags.WATER_RESISTANT;
import static us.asciiroth.client.core.Color.BLUE;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * When the blue ring is worn, the player can move through water (although
 * not ocean... you can use this to bound the region the player can traverse).
 * However, the player cannot take the ring off while doing this.
 *
 */
public class BlueRing extends AbstractItem {
    private BlueRing() {
        super("Blue Ring", 0, new Symbol("o", BLUE));
    }
    @Override
    public void onThrow(Event event, Cell cell) {
        if (cell.getTerrain().is(AQUATIC)) {
            event.cancel("That would be suicide");
        }
    }
    @Override
    public void onSelect(Context context, Cell cell) {
        context.getPlayer().add(WATER_RESISTANT);
    }
    @Override
    public void onDeselect(Event event, Cell cell) {
        if (cell.getTerrain().is(AQUATIC)) {
            event.cancel("You can't swim. You've got to keep the ring on.");
        } else {
            event.getPlayer().remove(WATER_RESISTANT);
        }
    }
    @Override
    public void onDrop(Event event, Cell cell) {
        if (cell.getTerrain().is(AQUATIC)) {
            event.cancel("You'd drown. Really.");
        }
    }
    
    @Override
    public void onUse(Event event) {
        event.cancel("To wear the ring, just keep it selected");
    }
    /** Type serializer. */
    public static final Serializer<BlueRing> SERIALIZER = new TypeOnlySerializer<BlueRing>() {
        public BlueRing create(String[] args) {
            return new BlueRing();
        }
        public String getTag() {
            return "Power-Ups";
        }
    };
}
