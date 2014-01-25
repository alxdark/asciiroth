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
package us.asciiroth.client.items;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.AbstractPiece;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Symbol;

/**
 * Abstract do-nothing implementation of an item.
 *
 */
public abstract class AbstractItem extends AbstractPiece implements Item {

    /**
     * Constructor.
     * @param name
     * @param flags
     * @param symbol
     */
    public AbstractItem(String name, int flags, Symbol symbol) {
        super(name, flags, symbol);
    }
    /**
     * Constructor
     * @param name
     * @param flags
     * @param color
     * @param symbol
     */
    public AbstractItem(String name, int flags, Color color, Symbol symbol) {
        super(name, flags, color, symbol);
    }
    public Item onFire(Event event) {
        return null;
    }
    public void onDrop(Event event, Cell cell) {
    }
    public void onThrow(Event event, Cell cell) {
    }
    public void onThrowEnd(Event event, Cell cell) {
    }
    public void onHit(Event event, Cell agentLoc, Agent agent) {
    }
    public void onUse(Event event) {
    }
    public void onSteppedOn(Event event, Cell agentLoc, Agent agent) {
    }
    public void onSelect(Context context, Cell cell) {
    }
    public void onDeselect(Event event, Cell cell) {
    }
    public String getDefiniteNoun(String phrase) {
        if (name.startsWith("The ")) {
            return Util.format(phrase, name);
        }
        String s = Util.format(phrase, "the " + name);
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
    public String getIndefiniteNoun(String phrase) {
        if (name.startsWith("The ")) {
            return Util.format(phrase, name);
        }
        String article = (name.substring(0,1).matches("[aeiou]")) ? "an" : "a";
        String s = Util.format(phrase, article + " " + name);
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
