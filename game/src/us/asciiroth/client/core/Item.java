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

import us.asciiroth.client.board.Cell;

/**
 * Items are objects that can be picked up, moved, thrown and/or used by 
 * the player. They do not block movement into a cell.  
 *
 */
public interface Item extends Piece {
    /**
     * Notification that the item is about to be thrown. If the event is 
     * canceled at this time, the item will not be thrown and it stays 
     * in the possession of the agent. 
     * 
     * @param event
     * @param cell
     */
    public void onThrow(Event event, Cell cell);

    /**
     * If this item can fire any kind of projectile, it should return it 
     * when this method is executed.  
     * @param event
     * @return  an item that is the ammunition for this item when fired.
     */
    public Item onFire(Event event);
    
    /**
     * Item has touched an agent either as a result of being thrown against the 
     * agent, or as a result of the player walking into the agent while holding
     * the item. Cancelling the event within this method has no effect. 
     * @param event
     * @param agentLoc
     * @param agent
     */
    public void onHit(Event event, Cell agentLoc, Agent agent);
    /**
     * Notification that this item, having been thrown, has fallen to the ground
     * at the indicated cell. If you cancel the event, the item will disappear
     * from the board.
     * @param event
     * @param cell
     */
    public void onThrowEnd(Event event, Cell cell);
    /**
     * The player has used the item without indicating any particular direction 
     * for use. Even if the item has no intrinsic use, it's helpful to cancel the
     * event with a message explaining that the item requires a direction to be 
     * useful.
     * @param event
     */
    public void onUse(Event event);
    /**
     * Notification that the item is about to be dropped. Cancelling the event 
     * at this time prevents the item from leaving the agent's possession (it 
     * cannot be dropped for some reason).
     * @param event
     * @param cell
     */
    public void onDrop(Event event, Cell cell);
    /**
     * Notification that the user is trying to select the item. Cancelling
     * the event has no effect because it creates an odd game experience, 
     * but you can undertake work in this method if the item changes 
     * the environment
     * @param context
     * @param cell
     */
    public void onSelect(Context context, Cell cell);
    /**
     * Notification that the user is trying to deselect the item. Cancelling
     * the event prevents the item from being deselected.
     * @param event
     * @param cell
     */
    public void onDeselect(Event event, Cell cell);
    
    /**
     * Notification that an agent has entered the same square as this item.
     * @param event
     * @param agentLoc
     * @param agent
     */
    public void onSteppedOn(Event event, Cell agentLoc, Agent agent);
    
    // onPickup
    
    /**
     * Produce the phrase with the {0} token replaced with the 
     * name of this item using a definite article.
     * @param phrase
     */
    public String getDefiniteNoun(String phrase);
    
    /**
     * Produce the phrase with the {0} token replaced with the 
     * name of this item using an indefinite article.
     * @param phrase
     */
    public String getIndefiniteNoun(String phrase);
}
