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

import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.event.Events;

/**
 * A game event, with the information necessary for the callbacks on the various 
 * pieces to create a meaningful response to the event.
 *
 */
public class Event extends Context {

    private boolean cancelled;
    
    /**
     * 
     * @param player
     * @param board
     */
    public Event(Player player, Board board) {
        super(player, board);
    }
    /**
     * Cancel this event. Usually this means the originally intended action is prevented,
     * but it can mean in certain circumstances that the normal flow of the event is 
     * changed. See the documentation for individual callback methods for details on what
     * will happen if the event is canceled in the handling of that callback.
     *
     */
    public void cancel() {
        cancelled = true;
    }
    /**
     * Cancel this event and provide an explanation to the player, pointing to the current
     * cell where the player is.
     * @param message   a message to display to the player
     */
    public void cancel(String message) {
        cancelled = true;
        Events.get().fireMessage(getBoard().getCurrentCell(), message);
    }
    /**
     * Cancel this event and provide an explanation to the player, pointing to the indicated
     * cell. Note that currently, messages will only be shown on the two cells that are at 
     * either end of a directional event... were you to point somewhere else on the board, 
     * the message will not be displayed to the user.
     * @param cell      the cell that is the locus of the message
     * @param message   a message to display to the player
     */
    public void cancel(Cell cell, String message) {
        cancelled = true;
        Events.get().fireMessage(cell, message);
    }
    /**
     * Is the event canceled?
     * @return      true if the event has been canceled
     */
    public boolean isCancelled() {
        return cancelled;
    }
    /**
     * In the course of firing events it may be that one failure requires 
     * a further test. You can suppress cancel and test again. This really 
     * shouldn't be done outside of the Game singleton's controller methods, 
     * where event callbacks to the pieces are orchestrated.
     */
    public void suppressCancel() {
        cancelled = false;
    }
}
