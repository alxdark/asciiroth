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
package us.asciiroth.client.effects;

import static us.asciiroth.client.core.Flags.AMMO_ENLIVENER;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Flags;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

/**
 *  
 */
public class InFlightItem extends AbstractEffect implements Animated {

    private Item item;
    private Direction direction;
    private Piece originator;
    
    /**
     * Constructor.
     * @param item
     * @param direction
     * @param originator
     */
    public InFlightItem(Item item, Direction direction, Piece originator) {
        super(item.getName(), item.is(AMMUNITION)?TRANSIENT:0, item.getSymbol());
        this.item = item;
        this.direction = direction;
        this.originator = originator;
    }
    /**
     * Get the item that is currently in flight.
     * @return  item
     */
    public Item getItem() {
        return item;
    }
    @Override
    public boolean is(int flag) {
        return item.is(flag) || Flags.is(flag, flags);
    }
    @Override
    public boolean not(int flag) {
        return item.not(flag) && Flags.not(flag, flags);
    }
    /**
     * Set the item that is currently in flight.
     * @param item
     */
    public void setItem(Item item) {
        this.item = item;
    }
    /**
     * Get the direction this item is currently flying.
     * @return  direction
     */
    public Direction getDirection() {
        return direction;
    }
    /**
     * Set the direction this item should continue to fly.
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    /**
     * The last piece that touches a flying item is essentially the originator: a 
     * piece that can't be hurt by the flying thing because it is its own 
     * ammunition.  
     * 
     * @return  the piece that last fired or touched the flying item, for pieces
     *          that change the flight path of the item.
     */
    public Piece getOriginator() {
        return originator;
    }
    /**
     * Set the piece that originally threw or shot this item.
     * @param originator
     */
    public void setOriginator(Piece originator) {
        this.originator = originator;
    }
    @Override
    public Symbol getSymbol() {
        return item.getSymbol();
    }
    public boolean randomSeed() {
    	return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Event e = Game.get().createEvent();
        Cell next = cell.getAdjacentCell(direction);
        if (next != null) {
            // Some agents it makes sense for an item to fall next to them,
            // but it looks odd to me when you throw something like the hammer.
            // It's okay for now though.
            Agent agent = next.getAgent();
            if (agent != null) {
                // This is done first so the agent can basically determine if this is a 
                // hit or not (if it is, the event should be cancelled... although this)
                // will not stop the item's event, it will stop the flier.
                agent.onHitBy(e, next, item, direction);
                // Ammo that bounces off of a reflector becomes deadly to everyone. 
                // Agent cannot hit self, and two non-player agents cannot hit one another.
                if (originator.is(AMMO_ENLIVENER) || 
                   (agent != originator && (agent.is(PLAYER) || originator.is(PLAYER)))) {
                    item.onHit(e, next, agent);
                }
            } else {
                next.getTerrain().onFlyOver(e, next, this);
            }
        } else {
            e.cancel();
        }
        if (e.isCancelled()) {
            cell.getEffects().remove(this);
            Game.get().drop(e, cell, item);
        } else {
            cell.getEffects().moveEffectTo(next, this);
            if (originator == e.getPlayer()) {
                Events.get().fireClearCell(next);
            }
        }
    }
}
