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

import static us.asciiroth.client.core.Color.CORAL;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.PARALYSIS_RESISTANT;
import static us.asciiroth.client.core.Flags.PLAYER;
import static us.asciiroth.client.core.Flags.POISON_RESISTANT;
import static us.asciiroth.client.core.Flags.STONING_RESISTANT;
import static us.asciiroth.client.core.Flags.THEFT_RESISTANT;
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
 * An energy field that will weaken the player.   
 *
 */
public class ResistancesCloud extends AbstractEffect implements Animated {

    private Symbol currentSymbol;
    
    private ResistancesCloud() {
        super("Resistances Cloud", TRANSIENT, new Symbol("&emsp;", NONE, CORAL));
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
                Player player = ctx.getPlayer();
                
                if (player.is(THEFT_RESISTANT)) {
                    player.remove(THEFT_RESISTANT);
                    Events.get().fireMessage(cell, "Theft resistance is gone. ");
                }
                if (player.is(POISON_RESISTANT)) {
                    player.remove(POISON_RESISTANT);
                    Events.get().fireMessage(cell, "Poison resistance is gone. ");
                }
                if (player.is(PARALYSIS_RESISTANT)) {
                    player.remove(PARALYSIS_RESISTANT);
                    Events.get().fireMessage(cell, "Paralysis resistance is gone. ");
                }
                if (player.is(STONING_RESISTANT)) {
                    player.remove(STONING_RESISTANT);
                    Events.get().fireMessage(cell, "Resistance to stoning gone. ");
                }
            }
        } else {
            cell.getEffects().remove(this);
        }
    }
    /** Type serializer. */
    public static final Serializer<ResistancesCloud> SERIALIZER = new TypeOnlySerializer<ResistancesCloud>() {
        public ResistancesCloud create(String[] args) {
            return new ResistancesCloud();
        }
        public String getTag() {
            return "Ammunition";
        }
    };

}
