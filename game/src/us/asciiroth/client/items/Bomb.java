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

import static us.asciiroth.client.core.Flags.PLAYER;
import us.asciiroth.client.Util;
import us.asciiroth.client.agents.Pusher;
import us.asciiroth.client.agents.Slider;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * An item that can be left on the ground that all agents will obligingly 
 * walk right into, causing an explosion that will wipe many of them out.
 * This piece is about the only safe way to get rid of certain agents, 
 * like the Cephalid. It will not explode when the player walks over it.
 *
 */
public class Bomb extends AbstractItem implements Animated {
    
    /**
     * Constructor.
     */
    public Bomb() {
        super("Bomb", 0, new Symbol("&delta;", Color.WHITE, null, Color.BLACK, null));
    }
    public boolean randomSeed() {
        return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Color nfg = Util.oscillate(ctx.getBoard().isOutside() ? 
            Color.BLACK : Color.WHITE, Color.RED, 5, frame);
        Symbol s = new Symbol(symbol.getAdjustedEntity(), nfg, null);
        Events.get().fireRerender(cell, this, s);
    }
    @Override
    public void onSteppedOn(final Event event, Cell agentLoc, Agent agent) {
        if (agent.is(PLAYER)) {
            return;
        } else if (agent instanceof Slider || agent instanceof Pusher) {
            agentLoc.removeAgent(agent);
        }
        agentLoc.getBag().remove(this);
        agentLoc.explosion(event.getPlayer());
    }
    /** Type serializer. */
    public static final Serializer<Bomb> SERIALIZER = new TypeOnlySerializer<Bomb>() {
        public Bomb create(String[] args) {
            return new Bomb();
        }
        public String getTag() {
            return "Weapons";
        }
    };
}
