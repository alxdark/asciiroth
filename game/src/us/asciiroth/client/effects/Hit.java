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

import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

/**
 * Shows when an agent has been hit.
 *
 */
public class Hit extends AbstractEffect implements Animated {
    
    private Agent agent;
    
    /**
     * Constructor
     * @param agent
     */
    public Hit(Agent agent) {
        super("Hit", TRANSIENT, new Symbol(agent.getSymbol().getAdjustedEntity(), Color.BLACK, Color.RED));
    }
    @Override
    public boolean isAboveAgent() {
        return true;
    }
    public boolean randomSeed() {
    	return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame == 0) {
            Events.get().fireRerender(cell, agent, getSymbol());
        } else {
            cell.getEffects().remove(this);
        }
    }
}
