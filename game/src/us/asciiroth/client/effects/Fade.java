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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

import com.google.gwt.user.client.Command;

/**
 * Character fading. I give up: the effect animations are stateful. You wouldn't believe
 * the complexity of dealing with one animated piece calling another animated 
 * piece's getSymbol() method, and getting the wrong animation state. No answer is 
 * better, such as storing state in the animation proxies, partial rendering based
 * on the current appearance of the cell, etc.
 *
 */
public class Fade extends AbstractEffect implements Animated {

    private Command command;
    private Symbol currentSymbol;
    
    /**
     * Constructor.
     * @param symbol
     * @param command
     */
    public Fade(Symbol symbol, Command command) {
        super("Fade", TRANSIENT, NONE, symbol);
        this.command = command;
    }
    @Override
    public boolean isAboveAgent() {
        return true;
    }
    public boolean randomSeed() {
    	return false;
    }
    @Override
    public Symbol getSymbol() {
        return currentSymbol;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        boolean outside = ctx.getBoard().isOutside();
        Color bg = cell.getTerrain().getSymbol().getBackground(outside);
        if (bg == null) {
            bg = BLACK;
        }
        Color fg = Util.oscillate(bg, symbol.getColor(outside), 10, frame);
        if (frame < 10) {
            // The reason we have to assign back to symbol is that the teleporter
            // is also part of the animation and it's looking at getSymbol() when
            // rendering the fade, right in the same loop. If getSymbol() doesn't
            // change, it appears that nothing happens, even though Fade is asking
            // to re-render the cell correctly in all respects. 
            this.currentSymbol = new Symbol(symbol.getAdjustedEntity(), fg);
            Events.get().fireRerender(cell, this, currentSymbol);
        } else {
            cell.getEffects().remove(this);
            command.execute();
        }
    }
}
