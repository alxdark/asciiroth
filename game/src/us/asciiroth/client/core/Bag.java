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

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of items or effects. A bag keeps track of the count of like
 * items within the bag, and has logic for correctly adding and removing 
 * counts, rather than item instances. 
 *
 * @param <T>
 */
public class Bag<T extends Piece> {
    
    /**
     * List of entries in the bag. 
     */
    protected List<Entry<T>> entries;
    
    /** Constructor. */
    public Bag() {
        entries = new ArrayList<Entry<T>>();
    }
    /**
     * Add one count of an item to the bag. 
     * @param piece
     */
    public void add(T piece) {
        Entry<T> entry = findEntry(piece);
        if (entry != null) {
            entry.add();    
        } else {
            entries.add(new Entry<T>(piece));
        }
    }
    /**
     * Add an item to the bag at the indicated index. Used in an 
     * attempt to manipulate what you can see in the effects layer.
     * @param index
     * @param piece
     */
    public void add(int index, T piece) {
        Entry<T> entry = findEntry(piece);
        if (entry != null) {
            entry.add();    
        } else {
            entries.add(index, new Entry<T>(piece));
        }
    }
    /**
     * Remove one count of an item from the bag.
     * @param piece
     */
    public void remove(T piece) {
        Entry<T> entry = findEntry(piece);
        if (entry != null) {
            entry.remove();
            if (entry.getCount() <= 0) {
                entries.remove(entry);
            }
        }
    }
    /**
     * Get the current count for this item type.
     * @param piece
     * @return  the number in the bag, or 0 if none
     */
    public int getCount(T piece) {
        Entry<T> entry = findEntry(piece);
        return (entry != null) ? entry.getCount() : 0;
    }
    /**
     * Get the index of the entry for this item.
     * @param piece
     * @return  the index of the entry for this item
     */
    public int getIndex(T piece) {
        return entries.indexOf(findEntry(piece));
    }
    /**
     * Get the last item in this bag (and thus, the item that is visible
     * when on the ground).
     * @return      the last item in the bag.
     */
    public T last() {
        return (entries.isEmpty()) ? null : entries.get(entries.size()-1).piece;
    }
    /**
     * Get the item at the given index
     * @param index
     * @return      the item at the given index, or null if the index is out of bounds
     */
    public T get(int index) {
        return (!entries.isEmpty() && inBounds(index)) ? 
            entries.get(index).getPiece() : null;
    }
    /**
     * Does this bag contain one or more of the indicated piece?
     * @param piece
     * @return      true if it does, false otherwise
     */
    public boolean contains(T piece) {
        return (findEntry(piece) != null);
    }
    /**
     * Is the bag empty?
     * @return      true if empty, false otherwise
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }
    /**
     * @return      the set of entries as a list
     */
    public List<Entry<T>> asEntryList() {
        return entries;
    }
    /**
     * @return      the number of entries in the bag
     */
    public int size() {
        return entries.size();
    }
    public Entry<T> findEntry(T piece) {
        for (int i=0, len = entries.size(); i < len; ++i) {
            Entry<T> entry = (Entry<T>)entries.get(i);
            if (entry.getPiece() == piece) {
                return entry;
            }
        }
        return null;
    }
    private boolean inBounds(int index) {
        return (index >= 0 && index <= (entries.size()-1));
    }
    /**
     * An entry in the bag. An entry keeps track of the number of items in
     * the bag when there is more than one.
     *
     * @param <T>
     */
    public static class Entry<T extends Piece> {
        private T piece;
        private int count;
        private int ammo;
        
        /** Constructor.
         * 
         * @param piece
         */
        protected Entry(T piece) {
            if (piece == null) {
                throw new RuntimeException("Bag.Entry created with null piece");
            }
            this.piece = piece;
            this.count = 1;
        }
        public int getAmmo() { return ammo; }
        public void changeAmmo(int delta) { ammo += delta; }
        /**
         * Get the item.
         * @return      the item.
         */
        public T getPiece() { return piece; }
        /**
         * Set the item.
         * @param piece
         */
        public void setPiece(T piece) { this.piece = piece; }
        /**
         * Get the number of items in the bag.
         * @return      the number of items in the bag
         */
        public int getCount() { return count; }
        private void add() { count++; }
        private void remove() { count--; }

        @Override
        public String toString() {
            return piece.getName() + ((count > 1) ? " x"+count : "");
        }
    }
}
