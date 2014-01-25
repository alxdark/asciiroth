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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.CARNIVORE;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.ORGANIC;
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

/**
 * Our first movin', fightin' agent. Does not have a ranged weapon or a 
 * great range, but does a good amount of damage. Fires a color event when 
 * killed.
 *
 */
public class Rhindle extends AbstractAnimatedAgent {
    
    private static final Targeting TARGETING = 
        new Targeting().dodgeBullets(90).attackPlayer(10).moveRandomly().trackPlayer();
    
    /**
     * Constructor.
     * @param color
     */
    public Rhindle(Color color) {
        super("Rhindle", (CARNIVORE | ORGANIC | FIRE_RESISTANT), color, CombatStats.SQUEAKY_CTBH, 
            new Symbol("&amp;", WHITE, null, BLACK, null));
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.SQUEAKY_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 5 == 0) {
            Game.get().agentMove(cell, this, TARGETING);
        }
    }
    /** Type serializer. */
    public static final Serializer<Rhindle> SERIALIZER = new BaseSerializer<Rhindle>() {
        public Rhindle create(String[] args) {
            return new Rhindle(Color.byName(args[1]));
        }
        public Rhindle example() {
            return new Rhindle(Color.NONE);
        }
        public String store(Rhindle s) {
            return "Rhindle|"+s.color.getName();
        }
        public String template(String type) {
            return "Rhindle|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
