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

import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.event.Events;

/**
 * A bag representing the items in a cell. Following the simple 
 * C --> M --> V design of the application, this decorates a bag 
 * and adds updates to the map/view.
 */
public class EffectBag extends Bag<Effect> {
    
    private Cell cell;
    
    /**
     * Constructor
     * @param cell
     */
    public EffectBag(Cell cell) {
        super();
        if (cell == null) {
            throw new RuntimeException("Cell is null on EffectBag");
        }
        this.cell = cell;
    }
    /**
     * Add an in-flight item effect. It works out that it's much simpler to
     * start on the shooter's square and fly from there, but visually this
     * looks wrong, as the thrown/shot item momentarily appears on top of 
     * the agent doing the shooting. This eliminates the event that causes
     * this, and all is just right. Also adds the flier to the bottom of 
     * the stack. I don't know why... this has never apparently fixed the 
     * "sometimes I don't see the hit effect when an agent dies" issue.
     * 
     * @param effect
     */
    public void addInFlightItem(Effect effect) {
        super.add(0, effect);
        if (effect instanceof Animated) {
            cell.getBoard().addAnimated(cell.getX(), cell.getY(), (Animated)effect);
        }
    }
    @Override
    public void add(int index, Effect effect) {
        super.add(index, effect);
        if (effect instanceof Animated) {
            cell.getBoard().addAnimated(cell.getX(), cell.getY(), (Animated)effect);
        }
        Events.get().fireCellChanged(cell);
    }
    @Override
    public void add(Effect effect) {
        super.add(effect);
        if (effect instanceof Animated) {
            cell.getBoard().addAnimated(cell.getX(), cell.getY(), (Animated)effect);
        }
        Events.get().fireCellChanged(cell);
    }
    @Override
    public void remove(Effect effect) {
        super.remove(effect);
        if (effect instanceof Animated) {
            cell.getBoard().removeAnimated(cell.getX(), cell.getY(), (Animated)effect);
        }
        Events.get().fireCellChanged(cell);
    }
    /** An optimization to avoid creating animation proxies for effects.
     * 
     * @param next
     * @param effect
     */
    public void moveEffectTo(Cell next, Effect effect) {
        cell.getBoard().moveAnimated(cell.x, cell.y, effect, next.x, next.y);
        // If we just call add/remove, the animation manipulation goes around in 
        // circles. 
        
        removeEffect(effect);
        next.getEffects().addEffect(next, effect);
    }
    private void removeEffect(Effect effect) {
        super.remove(effect);
        Events.get().fireCellChanged(cell);
    }
    private void addEffect(Cell next, Effect effect) {
        super.add(0, effect); // adding underneath *still* doesn't hide bullets under effects.
        Events.get().fireCellChanged(next);
    }
}
