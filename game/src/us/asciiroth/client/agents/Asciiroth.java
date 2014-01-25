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

import static us.asciiroth.client.core.Color.CYAN;
import static us.asciiroth.client.core.Color.DARKSEAGREEN;
import static us.asciiroth.client.core.Color.GREENYELLOW;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Flags.FIRE_RESISTANT;
import static us.asciiroth.client.core.Flags.PARALYSIS_RESISTANT;
import static us.asciiroth.client.core.Flags.STONING_RESISTANT;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Agentray;
import us.asciiroth.client.items.Weakray;
import us.asciiroth.client.terrain.TerrainUtils;

/**
 * A boss piece. It can only be killed with Terminus Est, and although
 * this alone is not too difficult, it is accompanied by attendants 
 * who can be deadly, and is sure to weaken the character, which is 
 * a serious handicap (or can be, depending on the scenario design). 
 * Oh yeah, and Asciiroth does run away...
 * <p>
 * Asciiroth should be placed on the board with "attendant" Lightning 
 * Lizards who have the same color and are the same as the agent passed
 * to Asciiroth. When they die, Asciiroth will shoot an 
 * <code>Agentray</code> that turns into a new lizard. In addition, 
 * it also shoots clouds that weaken the player.
 * <p>
 * NOTE: If there's ever any bullet that would kill Asciiroth, then it 
 * should dodge bullets. Currently it doesn't bother. 
 */
public class Asciiroth extends AbstractAnimatedAgent implements ColorListener {

    private static final Targeting FIRING_TARGETING = 
        new Targeting().attackPlayer(20);
    private static final Targeting MOVE_TARGETING = 
        new Targeting().attackPlayer(25).keepDistance(6).moveRandomly();
    
    private static final Symbol[] SYMBOLS = {
        new Symbol("&#1488;", CYAN),
        new Symbol("&#1488;", DARKSEAGREEN),
        new Symbol("&#1488;", GREENYELLOW)
    };
    
    private final Weakray weakray;
    private final Agentray agentray;
    
    public Asciiroth(Color color, Agent agent) {
        super("Asciiroth", (PARALYSIS_RESISTANT | STONING_RESISTANT | FIRE_RESISTANT), 
            color, CombatStats.ASCIIROTH_CTBH, SYMBOLS[0]);
        this.weakray = (Weakray)Registry.get().getPiece(Weakray.class);
        this.agentray = new Agentray(agent);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame % 4 == 0) {
            Game.get().agentMove(cell, this, MOVE_TARGETING);            
        } else if (frame % 11 == 0){
            Game.get().shoot(cell, this, weakray, FIRING_TARGETING);
        } else {
            Events.get().fireRerender(cell, this, SYMBOLS[frame%3]);
        }
    }
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Game.get().damage(agentLoc, agent, CombatStats.ASCIIROTH_DAMAGE);
    }
    @Override
    public void onDie(Event event, Cell cell) {
        shootAgentRay(event, cell);
        super.onDie(event, cell);
    }
    public void onColorEvent(Context ctx, Cell cell, Cell origin) {
        shootAgentRay(ctx, cell);
    }
    private void shootAgentRay(Context ctx, Cell cell) {
        // If an agentray bullet is stopped by a wall adjoining Asciiroth, no creature
        // is created. To prevent this, test to make sure we're not shooting in such 
        // a direction. In fact this gets really tight... were Asciiroth trapped by 
        // agents, it would be an issue.
        Cell next = null;
        Direction dir = null;
        while (dir == null || next == null || !next.canEnter(cell, this, dir, false)) {
            dir = TerrainUtils.getRandomDirection();
            next = cell.getAdjacentCell(dir);
        }
        Event event = Game.get().createEvent();
        Game.get().shoot(event, cell, this, agentray, dir);
    }
    public static final Serializer<Asciiroth> SERIALIZER = new BaseSerializer<Asciiroth>() {
        public Asciiroth create(String[] args) {
            return new Asciiroth(Color.byName(args[1]), unescAgent(args[2]));
        }
        public Asciiroth example() {
            return new Asciiroth(Color.NONE, new LightningLizard(NONE));
        }
        public String getTag() {
            return "Agents";
        }
        public String store(Asciiroth a) {
            return Util.format("Asciiroth|{0}|{1}",
                a.color.getName(),esc(a.agentray.getAgent()));
        }
        public String template(String type) {
            return "Asciiroth|{color}|{agent}";
        }
    };
}
