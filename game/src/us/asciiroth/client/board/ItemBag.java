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
package us.asciiroth.client.board;

import static us.asciiroth.client.core.Flags.MEAT;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.event.Events;

/**
 * A bag representing the items in a cell. Following the simple 
 * C --> M --> V design of the application, this decorates a bag 
 * and adds updates to the map/view.
 */
public class ItemBag extends Bag<Item> {
    
    private int meatCount;
    private Cell cell;
    
    /**
     * Constructor.
     * @param cell
     */
    public ItemBag(Cell cell) {
        super();
        if (cell == null) {
            throw new RuntimeException("Cell is null on ItemBag");
        }
        this.cell = cell;
    }
    /**
     * Does this bag contain a bone, fish, or similar meat-type item? 
     * Carnivores (dumb ones) will chase after these rather than the player.
     * 
     * @return  true if there's at least one bone or fish on the cell
     */
    public boolean containsMeat() {
        return (meatCount > 0);
    }
    @Override
    public void add(int index, Item item) {
        super.add(index, item);
        if (item.is(MEAT)) {
            meatCount++;
        } else if (item instanceof Animated) {
            cell.getBoard().addAnimated(cell.getX(), cell.getY(), (Animated)item);
        }
        Events.get().fireCellChanged(cell);
    }
    @Override
    public void add(Item item) {
        super.add(item);
        if (item.is(MEAT)) {
            meatCount++;
        } else if (item instanceof Animated) {
            cell.getBoard().addAnimated(cell.getX(), cell.getY(), (Animated)item);
        }
        Events.get().fireCellChanged(cell);
    }
    @Override
    public void remove(Item item) {
        super.remove(item);
        if (item.is(MEAT)) {
            meatCount--;
        } else if (item instanceof Animated) {
            cell.getBoard().removeAnimated(cell.getX(), cell.getY(), (Animated)item);
        }
        Events.get().fireCellChanged(cell);
    }
    /** 
     * An optimization to avoid creating animation proxies for items, and to keep
     * their timelines intact while moving
     * 
     * @param next
     * @param item
     */
    /* Not sure we can use this: currently items can't be moved except by the player,
     * because there are no lifecycle hooks to figure out where they can go.
    public void moveItemTo(Cell next, Item item) {
        cell.getBoard().moveAnimated(cell.x, cell.y, item, next.x, next.y);
        super.remove(item);
        next.getBag().quietlyAdd(item);
        Events.get().fireCellChanged(cell);
        Events.get().fireCellChanged(next);
    }
    private void quietlyAdd(Item item) {
        super.add(item);
    }
    */
}
