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

/**
 * An element of the game board. There are four kinds of pieces in the game: Terrain, 
 * Items, Agents, and effects. The first three are immutable and declared in the 
 * JSON-based map files. The fourth time, effects, are an extension of the implementation 
 * of animation in the game and they implement certain game features like thrown or 
 * shot items. Unlike the other three pieces, effects have state, they are created by 
 * the game engine (not placed using the map editor), and they are not saved. Some, like 
 * thrown items, will even delay a board save until they are played out. 
 * <p>
 * All pieces are immutable. Their state is passed in via their constructor, their member 
 * fields are final, and they transform the board by replacing themselves with other 
 * pieces with different state. So all floor instances are the same instance, for example. 
 * This allows pieces to be efficiently cached by the Registry, and retrieved by a key 
 * that describes the entire state of the piece. 
 * <p>
 * All pieces should have a public member field named SERIALIZER that implements a Serializer
 * for the piece (a class that can convert it back and forth from a string that is 
 * embedded in the board JSON). The Serializer should be added to the Registry, see the 
 * code in that class.
 * <p>
 * All pieces have color, even if the color is None. Color is a key attribute that can be 
 * used to tie pieces together via a color event. 
 * <p>
 * There is a parallel set of abstract classes that implement the piece type hierarchy. 
 *
 */
public interface Piece {

    /**
     * The visible name of the piece.
     * @return  the name of the piece as displayed to the user.
     */
    public String getName();
    /**
     * The symbol to display for this piece.
     * @return  the symbol for this piece
     */
    public Symbol getSymbol();
    /**
     * What is the color of this piece? While many piece types are permanently
     * of color "None", many more can be parameterized with a color in order 
     * to tie them together through color-based events. 
     * @return  the Color of this piece
     */
    public Color getColor();
    /**
     * Does this piece have the indicated bit flag?
     * @param flag
     * @return  true if piece has the flag
     * @see us.asciiroth.client.core.Flags
     */
    public boolean is(int flag);
    
    /**
     * Does this piece not have the indicated flag?
     * @param flag
     * @return  true if piece does not have the flag
     * @see us.asciiroth.client.core.Flags
     */
    public boolean not(int flag);
}
