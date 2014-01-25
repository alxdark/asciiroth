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
package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.DARKOLIVEGREEN;
import static us.asciiroth.client.core.Color.OLIVEDRAB;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.ORGANIC;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.items.Arrow;

/**
 * A Sleestak from <cite>The Land of the Lost</cite>. Slow (but 
 * not as slow as in the TV series, because then they would be 
 * utterly harmless), shoots arrows (but again, better than they 
 * did in the TV show), and that's about it. Fires a color event 
 * when killed.  
 */
public class Sleestak extends AbstractAnimatedAgent {
    
    private static final Targeting FIRING_TARGETING = 
        new Targeting().attackPlayer(10);
    private static final Targeting MOVING_TARGETING = 
        new Targeting().dodgeBullets(90).attackPlayer(12).moveRandomly().trackPlayer();

    private final Arrow arrow;

    /**
     * Constructor.
     * @param color
     */
    public Sleestak(Color color) {
        super("Sleestak", (CARNIVORE | ORGANIC), color, 
            CombatStats.SLEESTAK_CTBH, new Symbol("S", OLIVEDRAB, null, DARKOLIVEGREEN, null));
        this.arrow = (Arrow)Registry.get().getPiece(Arrow.class);
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.SLEESTAK_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 7 == 0) {
            Game.get().agentMove(cell, this, MOVING_TARGETING);
        } else if (frame % 6 == 0) {
            Game.get().shoot(cell, this, arrow, FIRING_TARGETING);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Sleestak> SERIALIZER = new BaseSerializer<Sleestak>() {
        public Sleestak create(String[] args) {
            return new Sleestak(Color.byName(args[1]));
        }
        public Sleestak example() {
            return new Sleestak(Color.NONE);
        }
        public String getTag() {
            return "Agents";
        }
        public String store(Sleestak s) {
            return "Sleestak|"+s.color.getName();
        }
        public String template(String type) {
            return "Sleestak|{color}";
        }
    };
}
