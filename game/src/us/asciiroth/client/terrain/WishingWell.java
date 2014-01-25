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
package us.asciiroth.client.terrain;

import static us.asciiroth.client.core.Color.OCEAN;
import static us.asciiroth.client.core.Flags.PENETRABLE;
import static us.asciiroth.client.core.Flags.POISONED;
import static us.asciiroth.client.core.Flags.WEAK;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.GoldCoin;
import us.asciiroth.client.items.Grenade;
import us.asciiroth.client.items.Rock;

/**
 * Wishing well. Throw a coin in and it will give you what you 
 * most need (it'll fix your worst liability at the moment). But
 * it will only work one time. Along with fishing, it's intended
 * to be a bit of a safety valve for a scenario that's gotten
 * too hard. 
 */
public class WishingWell extends AbstractTerrain {

    private final Terrain terrain;
    private final State state;
    
    /**
     * Constructor.
     */
    public WishingWell(Terrain terrain, State state) {
        super("Well", PENETRABLE, 
            new Symbol("&#1060;", 
                OCEAN, terrain.getSymbol().getBackground(false), 
                OCEAN, terrain.getSymbol().getBackground(true)));
        this.terrain = terrain;
        this.state = state;
    }
    @Override
    public void onDrop(Event event, Cell itemLoc, Item item) {
        // Grenades just blow up.
        if (!(item instanceof Grenade)) {
            event.cancel(itemLoc, item.getDefiniteNoun("{0} falls into the well"));
        }
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        PlayerBag bag = player.getBag();
        if (bag.getSelected() instanceof GoldCoin) {
            bag.remove(bag.getSelected());
            Events.get().fireMessage(cell, "You toss a coin into the well. ");
            if (state == State.ON) {
                if (somethingGoodHappens(player, cell)) {
                    Terrain ww = TerrainUtils.getTerrainOtherState(this, state);
                    cell.setTerrain(ww);
                }
            } else {
                Events.get().fireMessage(cell, "Nothing happens.");
            }
        } else if (bag.getSelected() instanceof Rock) {
            Events.get().fireMessage(cell, 
                "You toss a rock into the well. It's calming, isn't it?");
        } else if (bag.getSelected() != PlayerBag.EMPTY_HANDED){
            Events.get().fireMessage(cell, "Try a coin...");
        }
    }
    
    private boolean somethingGoodHappens(Player player, Cell cell) {
        int currentValue = player.changeHealth(0);
        if (player.is(POISONED)) {
            player.remove(POISONED);
            Events.get().fireMessage(cell, "You are no longer poisoned.");
            return true;
        } else if (player.is(WEAK)) {
            player.remove(WEAK);
            Events.get().fireMessage(cell, "You no longer feel weak.");
            return true;
        } else if (currentValue < Player.MAX_HEALTH){
            // Heal fully.
            int newValue = player.changeHealth( player.changeHealth(0)-Player.MAX_HEALTH );
            Events.get().fireMessage(cell, "You're healed " + (newValue-currentValue) + " points.");
            return true;
        }
        Events.get().fireMessage(cell, "Nothing happens.");
        return false;
    }

    /** Type serializer. */
    public static final Serializer<WishingWell> SERIALIZER = new BaseSerializer<WishingWell>() {
        public WishingWell create(String[] args) {
            return new WishingWell(unescTerrain(args[1]), State.byName(args[2]));
        }
        public String getTag() {
            return "Room Features";
        }
        public WishingWell example() {
            return new WishingWell(new Floor(), State.ON);
        }
        public String store(WishingWell w) {
            return Util.format("WishingWell|{0}|{1}", esc(w.terrain), w.state.getName());
        }
        public String template(String type) {
            return "WishingWell|{terrain}|{state}";
        }
    };
}
