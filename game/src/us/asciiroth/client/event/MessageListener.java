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
package us.asciiroth.client.event;

import us.asciiroth.client.board.Cell;

/**
 * Listener events for messaging in the game. These are all mirred in 
 * the <code>Events</code> class and two of the methods, <code>message</code> 
 * and <code>modalMessage</code>, are frequently used from the game
 * pieces.
 * <p>
 * Modal messages are shown "immediately" (within the game loop) and they 
 * pause the game animation. Other messages are displayed in the course
 * of processing user actions, and thus are presumed to follow from a 
 * player-initiated event (although they don't have to bear any obvious 
 * relationship to it and don't have to be near the player on the board).
 * <p>
 * The <code>clear*</code> and <code>handle*</code> methods are used internally
 * and should not be referenced from game pieces.
 *
 */
public interface MessageListener {
    public void message(Cell cell, String message);
    public void modalMessage(String message);
    
    public void clearCurrentCell();
    public void clearCell(Cell cell);
    public void clearAllCells();
    
    public void handleInventoryMessaging();
    public void handleModalMessage();
}
