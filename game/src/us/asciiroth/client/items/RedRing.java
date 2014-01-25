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

import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.LAVITIC;
import static us.asciiroth.client.core.Color.RED;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * When this ring is worn, the player can move through lava. However, 
 * the player cannot take the ring off while in the lava. That would
 * just be foolish. 
 */
public class RedRing extends AbstractItem {

    private RedRing() {
        super("Red Ring", 0, new Symbol("o", RED));
    }
    @Override
    public void onSelect(Context context, Cell cell) {
        context.getPlayer().add(FIRE_RESISTANT);
        // context.getPlayer().getFlags().add(Flags.FIRE_RESISTANT, true);
    }
    @Override
    public void onDeselect(Event event, Cell cell) {
        if (cell.getTerrain().is(LAVITIC)) {
            event.cancel("Are you kidding? You'd fry!");
        } else {
            event.getPlayer().remove(FIRE_RESISTANT);
            // event.getPlayer().getFlags().remove(Flags.FIRE_RESISTANT);
        }
    }
    @Override
    public void onThrow(Event event, Cell cell) {
        if (cell.getTerrain().is(LAVITIC)) {
            event.cancel("That would be suicide");
        }
    }
    @Override
    public void onDrop(Event event, Cell cell) {
        if (cell.getTerrain().is(LAVITIC)) {
            event.cancel("That would be suicide");
        }
    }
    
    @Override
    public void onUse(Event event) {
        event.cancel("To wear the ring, just keep it selected");
    }

    /** Type serializer. */
    public static final Serializer<RedRing> SERIALIZER = new TypeOnlySerializer<RedRing>() {
        public RedRing create(String[] args) {
            return new RedRing();
        }
        public String getTag() {
            return "Power-Ups";
        }
    };

}
