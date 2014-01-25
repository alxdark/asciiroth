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

import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.PURPLE;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.POISONED;
import static us.asciiroth.client.core.Flags.POISON_RESISTANT;
import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * A cloud of poison, if the player is within it, it's very, very
 * likely he or she will become poisoned. 
 *
 */
public class PoisonCloud extends AbstractEffect implements Animated {

    private Symbol currentSymbol;
    
    private PoisonCloud() {
        super("Poison Cloud", TRANSIENT, new Symbol("&emsp;", NONE, PURPLE));
    }
    public boolean randomSeed() {
    	return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        boolean outside = ctx.getBoard().isOutside();
        Color bg = cell.getTerrain().getSymbol().getBackground(outside);
        Color newBg = Util.oscillate(bg, symbol.getBackground(outside), 5, frame);
        
        Agent agent = cell.getAgent();
        if (frame < 5) {
            this.currentSymbol = new Symbol(symbol.getAdjustedEntity(), NONE, newBg);
            Events.get().fireRerender(cell, this, currentSymbol);            
            if (agent != null && agent.is(PLAYER)) {
                Player player = ctx.getPlayer();
                if (!player.testResistance(POISON_RESISTANT)) {
                    player.add(POISONED);
                }
            }
        } else {
            cell.getEffects().remove(this);
        }
    }
    /** Type serializer. */
    public static final Serializer<PoisonCloud> SERIALIZER = new TypeOnlySerializer<PoisonCloud>() {
        public PoisonCloud create(String[] args) {
            return new PoisonCloud();
        }
        public String getTag() {
            return "Ammunition";
        }
    };

}
