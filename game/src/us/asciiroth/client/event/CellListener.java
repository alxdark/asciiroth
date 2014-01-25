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
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;

/**
 * A component that responds to updates of the cells on the board. Most 
 * importantly, a component that can re-render the cell
 */
public interface CellListener {
    /**
     * Event notification that a cell has changed in some way: terrain has been 
     * transformed, items have been picked up or dropped, effects are flying over
     * the cell, or similar.
     * 
     * @param cell  the cell that has changed
     */
    public void onCellChanged(Cell cell);
    /**
     * Event notification that a piece would like to rerender itself at the 
     * given location. Used by animated piece.
     * @param cell
     * @param piece
     * @param sym
     */
    public void onRerender(Cell cell, Piece piece, Symbol sym);
}
