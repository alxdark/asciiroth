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

import static us.asciiroth.client.core.Color.MIDNIGHTBLUE;
import static us.asciiroth.client.core.Color.POWDERBLUE;
import static us.asciiroth.client.core.Flags.FLIER;
import static us.asciiroth.client.core.Flags.ORGANIC;
import static us.asciiroth.client.core.Flags.THEFT_RESISTANT;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
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
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Bow;

/**
 * A flying agent that steals the player's selected item, then attempts to run
 * away. If it is killed, the stolen item drops to the floor. Whether a corvid
 * is on the attack or on the run depends simply on whether it has an item or 
 * not. Corvids fire a color event when they die.
 *
 */
public class Corvid extends AbstractAnimatedAgent {
    
    private static final Symbol[] SYMBOLS = new Symbol[] {
        new Symbol("^", "&and;", POWDERBLUE, null, MIDNIGHTBLUE, null),
        new Symbol("v", "&or;", POWDERBLUE, null, MIDNIGHTBLUE, null)
    };
    
    private static final Targeting stealing = new Targeting()
        .attackPlayer(12).moveRandomly().dodgeBullets(60);
    
    private static final Targeting running = new Targeting()
        .fleePlayer(12).dodgeBullets(60);
    
    private final Item item;
    
    /**
     * Constructor.
     * @param color
     * @param item
     */
    public Corvid(Color color, Item item) {
        super("Corvid", (FLIER | ORGANIC), color, CombatStats.CORVID_CTBH, SYMBOLS[0]);
        this.item = item;
    }
    
    /**
     * Constructor.
     * @param color
     */
    public Corvid(Color color) {
        this(color, null);
    }
    
    @Override
    public void onHit(Event event, Cell attackerLoc, Cell agentLoc, Agent agent) {
        Player player = event.getPlayer();
        Item item = player.getBag().getSelected();
        if (item != PlayerBag.EMPTY_HANDED) {
            if (player.testResistance(THEFT_RESISTANT)) {
                return;
            }
            Event e = Game.get().createEvent();
            item.onDeselect(e, e.getBoard().getCurrentCell());
            if (e.isCancelled()) { // Blue Ring, Red Ring, etc.
                return;
            }
            player.getBag().remove(item);
            Events.get().fireModalMessage("The corvid snatches the "+item.getName()+" from your hands!");
            
            attackerLoc.removeAgent(this);
            attackerLoc.setAgent(Registry.get().cache(new Corvid(color, item)));
        } else {
            Game.get().damage(agentLoc, agent, CombatStats.CORVID_DAMAGE);
        }
    }
    @Override
    public void onHitBy(Event event, Cell agentLoc, Agent agent, Direction dir) {
        event.cancel();
    }
    @Override
    public void onHitBy(Event event, Cell itemLoc, Item item, Direction dir) {
        super.onHitBy(event, itemLoc, item, dir);
    }
    @Override
    public void onDie(Event event, Cell agentLoc) {
        if (item != null) {
            boolean cancelled = event.isCancelled();
            Game.get().drop(event, agentLoc, item);
            event.suppressCancel();
            if (cancelled) {
                event.cancel();
            }
        }
        event.getBoard().fireColorEvent(event, color, agentLoc);
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        Events.get().fireRerender(cell, this, SYMBOLS[frame%2]);
        if (frame%3 == 0) {
            if (item == null) {
                Game.get().agentMove(cell, this, stealing);
            } else {
                Game.get().agentMove(cell, this, running);
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<Corvid> SERIALIZER = new BaseSerializer<Corvid>() {
        public Corvid create(String[] args) {
            return new Corvid(Color.byName(args[1]), (args.length == 3) ? unescItem(args[2]) : null);
        }
        public Corvid example() {
            return new Corvid(Color.NONE, new Bow());
        }
        public String store(Corvid c) {
            return (c.item != null) ?
                Util.format("Corvid|{0}|{1}", c.color.getName(), esc(c.item)) :
                Util.format("Corvid|{0}", c.color.getName());
        }
        public String template(String type) {
            return "Corvid|{color}|{item?}";
        }
        public String getTag() {
            return "Agents";
        }
    };
}
