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
 * Abstract implementation of a piece.
 *
 */
public abstract class AbstractPiece implements Piece {
    
    /**
     * The name of the piece, as displayed to the user.
     */
    protected final String name;
    /**
     * The symbol of the piece. There can sometimes be other
     * displays of the piece due to animation or outside rendering, etc. 
     */
    protected final Symbol symbol;
    /**
     * The color of this piece.
     */
    protected final Color color;
    
    /**
     * Flags.
     */
    protected final int flags;
    
    /**
     * Constructor.
     * @param name
     * @param flags
     * @param color
     * @param symbol
     */
    public AbstractPiece(String name, int flags, Color color, Symbol symbol) {
        if (name == null || name.length() == 0) {
            throw new RuntimeException("Piece must have name");
        }
        if (symbol == null) {
            throw new RuntimeException("Piece must have symbol");
        }
        if (color == null) {
            throw new RuntimeException("Piece must have color");
        }
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.flags = flags;
    }
    /**
     * Constructor.
     * @param name
     * @param flags
     * @param symbol
     */
    public AbstractPiece(String name, int flags, Symbol symbol) {
        this(name, flags, Color.NONE, symbol);
    }
    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    /**
     * AbstractPiece does not support different rendering outside
     * as opposed to inside. Subclasses must implement this.
     */
    public Symbol getSymbol() {
        return symbol;
    }
    public boolean is(int flag) {
        return (flags & flag) == flag;
    }
    public boolean not(int flag) {
        return (flags & flag) != flag;
    }
}
