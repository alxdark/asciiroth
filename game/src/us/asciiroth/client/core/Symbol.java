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

import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.Profile;

/**
 * An immutable, visual representation of a piece in the game. Game boards are 
 * marked as either outside or not (underground), which indicates that the 
 * board is either lit by sunlight (and thus more naturally colored), or not. 
 * In the latter case, the color scheme is the same kind of inverse white-on-black
 * colors you see in a terminal and that is the stuff of roguelike games.
 * <p>
 * Symbols are layered, currently with terrain on the bottom, followed by items, 
 * then effects, and then the agent if present. The highest entity, color or 
 * background color is merged into the final symbol displayed for a given cell.
 * <p>
 */
public class Symbol {
    
    final static private Profile PROFILE = Game.get().getProfile();
    
    /** 
     * The glyph (usually an HTML entity) for this symbol. Use the non-breaking
     * space "&emsp;" or "&nbsp;" for pieces that have no visible glyph.
     */
    final private String entity;
    
    /** A glyph for this symbol that may only work on some versions of windows and Mac. */
    final private String advanced;
    /** The underground color of the glyph. */
    final private Color color;
    /** The outside color of the glyph. */
    final private Color outsideColor;
    /** The underground background color of the cell. */
    final private Color background;
    /** The outside background color of the cell. */
    final private Color outsideBackground;
    
    /**
     * Constructor.
     * @param entity
     * @param color
     */
    public Symbol(String entity, Color color) {
        this(entity, entity, color, null);
    }
    
    /**
     * Constructor.
     * @param entity
     * @param advanced
     * @param color
     */
    public Symbol(String entity, String advanced, Color color) {
        this(entity, advanced, color, null);
    }
    
    /**
     * Constructor.
     * @param entity
     * @param color
     * @param background
     */
    public Symbol(String entity, Color color, Color background) {
        this(entity, entity, color, background, color, background);
    }
    
    /**
     * Constructor.
     * @param entity
     * @param color
     * @param background
     */
    public Symbol(String entity, String advanced, Color color, Color background) {
        this(entity, advanced, color, background, color, background);
    }
    
    /**
     * Constructor.
     * @param entity
     * @param fg
     * @param bg
     * @param ofg
     * @param obg
     */
    public Symbol(String entity, Color fg, Color bg, Color ofg, Color obg) {
        this(entity, entity, fg, bg, ofg, obg);
    }
    
    /**
     * Constructor.
     * @param entity
     * @param advanced
     * @param fg
     * @param bg
     * @param ofg
     * @param obg
     */
    public Symbol(String entity, String advanced, Color fg, Color bg, Color ofg, Color obg) {
        this.entity = entity;
        this.advanced = advanced;
        this.color = (fg == NONE) ? null : fg;
        this.background = bg;
        this.outsideColor = (ofg == NONE) ? null : ofg;
        this.outsideBackground = obg;
    }
    
    public String getAdjustedEntity() {
        return (PROFILE.useExtendedFonts()) ? advanced : entity;
    }
    public String getEntity() {
        return entity;
    }
    public String getAdvanced() {
        return advanced;
    }
    public Color getBackground(boolean outside) {
        return (outside) ? outsideBackground : background;
    }
    public Color getColor(boolean outside) {
        return (outside) ? outsideColor : color;
    }
    /*
    @Override
    public String toString() {
        String colorStr = (color != null) ? color.toString() : "";
        String bgcolorStr = (background != null) ? background.toString() : "";
        return Util.renderSymbol(entity, colorStr, bgcolorStr);
    }
    */
}
