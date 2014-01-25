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

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.GOLD;
import static us.asciiroth.client.core.Color.GOLDENROD;
import static us.asciiroth.client.core.Color.NEARBLACK;
import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.GoldCoin;

/**
 * Asciiroth's means of supporting shops. This piece works like an Exchanger, 
 * but it only excepts gold coins in variable amounts (and it doesn't put the
 * gold on the other side... the gold is just gone).
 */
public class VendingMachine extends AbstractTerrain {

    private final int cost;
    private final GoldCoin gold;
    
    public VendingMachine(int cost) {
        super("Vending Machine ("+cost+"<span style='color: gold'>&bull;</span> apiece)", 0, 
            new Symbol("&#247;", GOLD, NEARBLACK, GOLDENROD, BUILDING_WALL));
        this.cost = cost;
        this.gold = (GoldCoin)Registry.get().getPiece(GoldCoin.class);
    }
    @Override
    public void onEnter(Event event, Player player, Cell cell, Direction dir) {
        event.cancel();
        if (!dir.isDiagonal()) {
            Cell otherSide = cell.getAdjacentCell(dir);
            Item otherItem = (Item)otherSide.getBag().last();
            int purse = player.getBag().getCount(gold);
            
            if (otherItem == null) {
                Events.get().fireMessage(cell, "The vending machine is empty");
            } else if (purse < cost) {
                Events.get().fireMessage(cell, "You need "+cost+" gold coins");
            } else {
                for (int i=0; i < cost; i++) {
                    player.getBag().remove(gold);
                }
                otherSide.getBag().remove(otherItem);
                player.getBag().add(otherItem);
                Events.get().fireMessage(cell, otherItem.getDefiniteNoun("You purchase {0}"));
            }
        }
    }
    /** Type serializer. */
    public static final Serializer<VendingMachine> SERIALIZER = new BaseSerializer<VendingMachine>() {
        public VendingMachine create(String[] args) {
            return new VendingMachine(Integer.parseInt(args[1]));
        }
        public VendingMachine example() {
            return new VendingMachine(1);
        }
        public String getTag() {
            return "Room Features";
        }
        public String store(VendingMachine vm) {
            return "VendingMachine|"+Integer.toString(vm.cost);
        }
        public String template(String type) {
            return "VendingMachine|{cost}";
        }
    };
}
