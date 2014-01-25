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

import static us.asciiroth.client.core.Color.DARKGREEN;
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
import us.asciiroth.client.items.PoisonDart;

/**
 * A <a href="http://en.wikipedia.org/wiki/Triffid">Triffid</a>. I love the radio 
 * show that was made out of this story in the '50s. (Actually, I love all radio 
 * drama, it fuels the production of this game. So, homage.)
 * <p>
 * Triffids move relatively slowly but shoot poison darts that prevent the player
 * from being able to heal with Healers. So they're a drag. Fires a color event 
 * when killed.
 *
 */
public class Triffid extends AbstractAnimatedAgent {
    
    private static final Targeting DART_TARGETING = 
        new Targeting().attackPlayer(10);
    private static final Targeting MOVE_TARGETING = 
        new Targeting().attackPlayer(20).moveRandomly();
    
    private final PoisonDart dart;
    
    /**
     * Constructor.
     * @param color
     */
    public Triffid(Color color) {
        super("A Triffid", ORGANIC, color, CombatStats.TRIFFID_CTBH, new Symbol("&#165;", DARKGREEN));
        this.dart = (PoisonDart)Registry.get().getPiece(PoisonDart.class);
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.TRIFFID_DAMAGE);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 25 == 0) {
            Game.get().agentMove(cell, this, MOVE_TARGETING);
        } else if (frame % 8 == 0) {
            Game.get().shoot(cell, this, dart, DART_TARGETING);
        }
    }
    
    /** Type serializer. */
    public static final Serializer<Triffid> SERIALIZER = new BaseSerializer<Triffid>() {
        public Triffid create(String[] args) {
            return new Triffid(Color.byName(args[1]));
        }
        public Triffid example() {
            return new Triffid(Color.NONE);
        }
        public String store(Triffid t) {
            return "Triffid|"+t.color.getName();
        }
        public String template(String type) {
            return "Triffid|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };

}
