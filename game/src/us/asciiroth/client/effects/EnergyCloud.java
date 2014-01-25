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
import static us.asciiroth.client.core.Color.YELLOWGREEN;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * An energy field that will weaken the player.   
 *
 */
public class EnergyCloud extends AbstractEffect implements Animated {

    private Symbol currentSymbol;
    
    private EnergyCloud() {
        super("Energy Cloud", TRANSIENT, new Symbol("&emsp;", NONE, YELLOWGREEN));
    }
    public boolean randomSeed() {
    	return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        boolean outside = ctx.getBoard().isOutside();
        Color bg = cell.getTerrain().getSymbol().getBackground(outside);
        Color newBg = Util.oscillate(bg, symbol.getBackground(outside), 7, frame);
        
        Agent agent = cell.getAgent();
        if (frame < 7) {
            this.currentSymbol = new Symbol(symbol.getAdjustedEntity(), NONE, newBg);
            Events.get().fireRerender(cell, this, currentSymbol);            
            if (agent != null && agent.is(PLAYER)) {
                ctx.getPlayer().weaken(cell);
            }
        } else {
            cell.getEffects().remove(this);
        }
    }
    /** Type serializer. */
    public static final Serializer<EnergyCloud> SERIALIZER = new TypeOnlySerializer<EnergyCloud>() {
        public EnergyCloud create(String[] args) {
            return new EnergyCloud();
        }
        public String getTag() {
            return "Ammunition";
        }
    };

}
