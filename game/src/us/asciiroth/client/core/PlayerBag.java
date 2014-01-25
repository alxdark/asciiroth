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
package us.asciiroth.client.core;

import static us.asciiroth.client.core.Flags.WEAPON;
import us.asciiroth.client.Util;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.items.ConsumesAmmo;
import us.asciiroth.client.items.EmptyHanded;
import us.asciiroth.client.items.ProvidesAmmo;

/**
 * A bag that tracks the player's inventory. What makes the inventory
 * unique, as bags go, is that one item in the bag is selected, and there
 * is an item called "empty-handed" that simulates the player using nothing
 * at all in the bag.
 *
 */
public class PlayerBag extends Bag<Item> {

    /**
     * An item that represents "no item".
     */
    public static final EmptyHanded EMPTY_HANDED = new EmptyHanded();

    private Entry<Item> selected;
    
    /** Constructor. */
    public PlayerBag() {
        super();
        entries.add(new Entry<Item>(PlayerBag.EMPTY_HANDED));
        selected = entries.get(0);
    }
    /** Select the next highest item in the bag (wraps around to the end of the list. */
    public void selectUp() {
        changeIndex(-1);
    }
    /** Select the next lowest item in the bag (wraps around to the end of the list. */
    public void selectDown() {
        changeIndex(1);
    }
    /** Select the empty handed item in the bag. */
    public void selectEmptyHanded() {
        changeSelection(0);
    }
    public void select(int index) {
        changeSelection(index);
    }
    /** 
     * Is this entry selected
     * @param entry
     * @return          true if this entry is selected, false otherwise
     */ 
    public boolean isSelected(Entry<Item> entry) {
        if (entry == null || selected == null) {
            return false;
        }
        return (entry.getPiece() == selected.getPiece());
    }
    /**
     * Get the currently selected item (the item that is being "wielded" and 
     * is assumed to be in use when the player moves toward something that 
     * will respond to an item).
     * @return  the currently selected item.
     */
    public Item getSelected() {
        return selected.getPiece();
    }
    /**
     * Find an item by its name, the name displayed to the user. 
     * @param name  the name of the item as displayed to the user
     * @return      the item if it is in inventory, null otherwise
     */
    public Item getByName(String name) {
        for (Entry<Item> entry : entries) {
            Piece p = entry.getPiece(); 
            if (p.getName().equals(name) || Util.getType(p).equals(name)) {
                return entry.getPiece();
            }
        }
        return null;
    }
    @Override
    public void add(Item item) {
        super.add(item);
        combineAmmoIfNecessary(item);
        Events.get().fireInventoryChanged(this);
    }
    @Override
    public void remove(Item item) {
        if (item != PlayerBag.EMPTY_HANDED) {
            if (selected != null && selected.getPiece() == item && selected.getCount() <= 1) {
                selectUp(); // It's safe because empty-handed can't be removed.
            }
            splitAmmoIfNecessary(item);
            super.remove(item);
            Events.get().fireInventoryChanged(this);
        }
    }
    private void combineAmmoIfNecessary(Item item) {
        Item weapon = null;
        Item ammo = null;
        if (item instanceof ConsumesAmmo) {
            weapon = item;
            ammo = ((ConsumesAmmo)item).getAmmoType();
        } else if (item instanceof ProvidesAmmo) {
            weapon = ((ProvidesAmmo)item).providesAmmoFor();
            ammo = item;
        }
        if (weapon != null && ammo != null) {
            Bag.Entry<Item> weaponEntry = findEntry(weapon);
            Bag.Entry<Item> ammoEntry = findEntry(ammo);
            if (weaponEntry != null && ammoEntry != null) {
                weaponEntry.changeAmmo(ammoEntry.getCount());
                for (int i=0, len = ammoEntry.getCount(); i < len; i++) {
                    super.remove(ammo);
                }
            }
        }
    }
    private void splitAmmoIfNecessary(Item item) {
        if (item instanceof ConsumesAmmo) {
            Bag.Entry<Item> itemEntry = findEntry(item);
            // There's only one so after removal there will be no reason to 
            // keep the ammunition grouped; and there is ammunition to split off
            if (itemEntry.getCount() == 1 && itemEntry.getAmmo() > 0) {
                // We want the ammo to go where the item is about to be removed
                int index = getIndex(item);
                Item ammo = ((ConsumesAmmo)item).getAmmoType();
                for (int i=0; i < itemEntry.getAmmo(); i++) {
                    // Call super or add will combine ammo back with weapon, ad nauseum 
                    super.add(index, ammo);
                }
            }
        }
    }
    /**
     * Add one instance of the supplied item and remove one instance of the currently
     * selected item. Simulates an exchange.
     * @param item      the item the player receives
     * @return          the item the player is giving up in return
     */
    public Item exchange(Item item) {
        Item sel = null;
        if (selected.getPiece() != PlayerBag.EMPTY_HANDED) {
            sel = selected.getPiece();
            remove(selected.getPiece());
            add(item);
            changeSelection(getIndex(item));
        }
        return sel;
    }
    public void moveSelectedUp() {
        if (selected.getPiece() != PlayerBag.EMPTY_HANDED) {
            int index = entries.indexOf(selected);
            Bag.Entry<Item> entry = entries.remove(index);
            index -= 1;
            if (index < 1) {
                index = entries.size();
            }
            entries.add(index, entry);
            Events.get().fireInventoryChanged(this); 
        }
    }
    public void moveSelectedDown() {
        if (selected.getPiece() != PlayerBag.EMPTY_HANDED) {
            int index = entries.indexOf(selected);
            Bag.Entry<Item> entry = entries.remove(index);
            index += 1;
            if (index > entries.size()) {
                index = 1;
            }
            entries.add(index, entry);
            Events.get().fireInventoryChanged(this); 
        }
    }
    public void selectFirstWeapon() {
        for (int i = 1; i < entries.size(); i++) {
            Bag.Entry<Item> entry = entries.get(i);
            if (entry.getPiece().is(WEAPON)) {
                changeSelection(i);
                break;
            }
        }
    }
    private void changeIndex(int delta) {
        int index = entries.indexOf(selected);
        index += delta;
        if (index < 0) {
            index = entries.size()-1;
        } else if (index > (entries.size()-1)) {
            index = 0;
        }
        changeSelection(index);
    }
    
    /**
     * When initializing the player, this quietly sets the item
     * that is selected.
     * 
     * @param newIndex
     */
    public void setInitialSelection(int newIndex) {
        Entry<Item> newItem = entries.get(newIndex);
        selected = newItem;
    }
    
    /**
     * Handles events surrounding the change selection of items
     * in inventory.  
     * @param newIndex
     */
    private void changeSelection(int newIndex) {
        Event event = Game.get().createEvent();
        Item old = selected.getPiece();
        old.onDeselect(event, event.getBoard().getCurrentCell());
        
        if (!event.isCancelled()) {
            Entry<Item> newItem = entries.get(newIndex);
            newItem.getPiece().onSelect(event, event.getBoard().getCurrentCell());
            if (!event.isCancelled()) {
                selected = newItem;
                Events.get().fireInventoryChanged(this);
            }
        }
    }
}