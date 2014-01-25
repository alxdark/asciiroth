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

import static us.asciiroth.client.core.Color.DARKSLATEBLUE;
import static us.asciiroth.client.core.Color.SLATEBLUE;
import static us.asciiroth.client.core.Flags.AMMUNITION;
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Flags.THEFT_RESISTANT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

/**
 * Hooloovoos are a hyper-intelligent shade of the color blue (Douglas Adams). 
 * When they hit the player, if the player is carrying anything, the player's 
 * inventory is teleported to random places on the board (always floor). 
 * Furthermore, ammunition bounces off of Hooloovoos and becomes harmful
 * to the player. They are not super fast, however. They fire a color event 
 * when killed.
 * <p>
 * I've had to make Hooloovoo's organic because otherwise, they have no 
 * weakness and they're impossible to kill! Now at least they can be 
 * killed with a bomb.
 *
 */
public class Hooloovoo extends AbstractAnimatedAgent {
    
    private static final Targeting TARGETING = 
        new Targeting().attackPlayer(8).moveRandomly();
    
    /**
     * Constructor.
     * @param color
     */
    public Hooloovoo(Color color) {
        super("Hooloovoo", ORGANIC, color, CombatStats.HOOLOOVOO_CTBH, 
            new Symbol("H", SLATEBLUE, DARKSLATEBLUE));
    }
    
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        // Currently this is the only option
        Player player = event.getPlayer();
        if (player.getBag().size() > 1) {
            if (player.testResistance(THEFT_RESISTANT)) {
                return;
            }
            while (player.getBag().size() > 1) {
                Item item = player.getBag().last();
                Event e = Game.get().createEvent();
                item.onDeselect(e, e.getBoard().getCurrentCell());
                if (e.isCancelled()) { // Blue Ring, Red Ring, etc.
                    continue;
                }
                player.getBag().remove(item);
                Cell cell = event.getBoard().findRandomCell();
                cell.getBag().add(item);
            }
            Events.get().fireModalMessage("The Hooloovoo teleports your stuff all over the place");
        } else {
            Game.get().damage(agentLoc, agent, CombatStats.HOOLOOVOO_DAMAGE);
        }
    }
    @Override
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir) {
        // Ammo bounces of a Hooloovoo and becomes dangerous to the player
        if (item.is(AMMUNITION)) {
            Game.get().shoot(event, itemLoc, this, item, dir.getReverseDirection());
            // Cancel event after shooting or else shooting doesn't work because
            // of the canceled event.
            event.cancel(); 
        }
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%6 == 0) { // going to be too fast...
            Game.get().agentMove(cell, this, TARGETING);    
        }
    }
    /** Type serializer. */
    public static final Serializer<Hooloovoo> SERIALIZER = new BaseSerializer<Hooloovoo>() {
        public Hooloovoo create(String[] args) {
            return new Hooloovoo(Color.byName(args[1]));
        }
        public Hooloovoo example() {
            return new Hooloovoo(Color.STEELBLUE);
        }
        public String store(Hooloovoo h) {
            return "Hooloovoo|"+h.color.getName();
        }
        public String template(String type) {
            return "Hooloovoo|{color}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
