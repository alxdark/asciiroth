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

import static us.asciiroth.client.core.Color.DARKORANGE;
import static us.asciiroth.client.core.Color.DARKRED;
import static us.asciiroth.client.core.Color.ORANGE;
import static us.asciiroth.client.core.Color.RED;
import static us.asciiroth.client.core.Flags.MELEE_WEAPON;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**  
 * An artifact and a very capable sword. Taken from Gene Wolfe's 
 * <cite>The Book of the New Sun</cite>. Well worth your time to read.
 */
public class TerminusEst extends AbstractItem implements Animated {

    /** Constructor. */
    public TerminusEst() {
        super("Terminus Est", MELEE_WEAPON, new Symbol("&dagger;", RED, null, DARKRED, null));
    }
    @Override
    public void onHit(Event event, Cell cell, Agent agent) {
        Game.get().damage(cell, agent, CombatStats.TERMINUS_EST_DAMAGE);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Color color = (!cell.getBoard().isOutside()) ?
            Util.oscillate(RED, ORANGE, 8, frame) :
            Util.oscillate(DARKRED, DARKORANGE, 8, frame);
        Symbol s = new Symbol(symbol.getAdjustedEntity(), color);
        Events.get().fireRerender(cell, this, s);
    }
    @Override
    public String getDefiniteNoun(String phrase) {
        return Util.format(phrase, name);
    }
    @Override
    public String getIndefiniteNoun(String phrase) {
        return Util.format(phrase, name);
    }
    /** Type serializer. */
    public static final Serializer<TerminusEst> SERIALIZER = new TypeOnlySerializer<TerminusEst>() {
        public TerminusEst create(String[] args) {
            return new TerminusEst();
        }
        public String getTag() {
            return "Artifacts";
        }
    };
}
