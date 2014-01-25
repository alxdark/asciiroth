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
 * A terrain piece that can be trigged by color events.
 * 
 * @see us.asciiroth.client.board#trigger(Cell)
 *
 */
public interface ColorListener {
    /**
     * Terrain has received a color event, probably related to the 
     * firing of a color event on the board. Different terrain behave
     * differently when triggered, most flip between a set of states.
     * @param ctx   a context object with references to game state
     * @param cell
     * @param origin
     */
    public void onColorEvent(Context ctx, Cell cell, Cell origin);
}
