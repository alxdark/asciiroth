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

import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.BUILDING_FLOOR;
import static us.asciiroth.client.core.Color.DARKGOLDENROD;
import static us.asciiroth.client.core.Flags.MELEE_WEAPON;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.effects.InFlightItem;
import us.asciiroth.client.effects.Smash;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.Arrow;
import us.asciiroth.client.items.Bullet;

/**
 * A large urn that can have an item inside it. It can be broken with any weapon
 * or it can be shot at with a ranged weapon of some kind.
 * 
 */
public class Urn extends AbstractTerrain {

    private final Item item;
    
    private Urn(Item item) {
        super("Urn", 0,
            new Symbol("u", DARKGOLDENROD, BLACK, DARKGOLDENROD, BUILDING_FLOOR));
        this.item = item;
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        if (cell.isBagEmpty()) {
            Item item = player.getBag().getSelected();
            if (item.is(MELEE_WEAPON)) {
                Events.get().fireMessage(cell, "You smash open the urn");
                smashUrn(cell);
            } else {
                Events.get().fireMessage(cell, "The urn is too large");
            }
        }
        super.onEnter(event, player, cell, dir);
    }
    @Override
    public void onFlyOver(Event event, Cell cell, InFlightItem flier) {
        if (flier.getItem() instanceof Bullet || flier.getItem() instanceof Arrow) {
            event.cancel();
            smashUrn(cell);
        }
        // Otherwise acts as PENETRABLE, stuff flies over.
    }
    private void smashUrn(final Cell to) {
        if (this.item != null) {
            to.getBag().add(this.item);    
        }
        to.getEffects().add(new Smash());
        to.setTerrain((Terrain)Registry.get().getPiece(Rubble.class));
    }
    
    /** Type serializer. */
    public static final Serializer<Urn> SERIALIZER = new BaseSerializer<Urn>() {
        public Urn create(String[] args) {
            return (args.length == 1) ?
                new Urn(null) : new Urn(unescItem(args[1])); 
        }
        public Urn example() {
            return new Urn(null);
        }
        public String store(Urn c) {
            return (c.item == null) ? "Urn" : ("Urn|"+esc(c.item)); 
        }
        public String template(String type) {
            return "Urn|{item?}";
        }
        public String getTag() {
            return "Room Features";
        }
    };
}
