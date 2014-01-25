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
package us.asciiroth.client.board;

import static us.asciiroth.client.core.Flags.TRANSIENT;
import static us.asciiroth.client.core.Flags.TRAVERSABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.AnimationProxy;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.ColorListener;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.decorators.DualTerrain;

import com.google.gwt.user.client.Random;

/**
 * The main model object in the game, the board represents the 
 * entire map and all the pieces on it.
 *
 */
public class Board {
    
    /**
     * The size of boards in columns. 
     */
    public static final int COLUMNS = 40;
    /**
     * The size of boards in rows.
     */
    public static final int ROWS = 25;
    
    boolean outside;
    Cell[][] cells;
    List<AnimationProxy> animated;
    Map<String, String> adjacentBoards;
    int playerX;
    int playerY;
    
    String scenarioName;
    String creator;
    String description;
    
    /**
     * An indicator of when the player last visited the cell. Each
     * time the player moves, this value is copied to the cell and
     * incremented
     */
    int visitCount;
    /**
     * Recorded for use by the map editor, but otherwise not used.
     */
    String startInventory;
    /**
     * Recorded for use by the map editor, but otherwise these are
     * not used.
     */
    int startX;
    /**
     * Recorded for use by the map editor, but otherwise these are
     * not used.
     */
    int startY;
    /**
     * Music file for this board. Not used.
     */
    String music;
    /**
     * Get the start X position of the board. If the player does not enter the 
     * board from another board, this is the X position that will be used to 
     * place the player. Used for the entry map of a scenario as well as testing.
     * 
     * @return  the start X position assigned to this board
     */
    public int getStartX() {
        return startX;
    }
    /**
     * Get the start Y position of the board. If the player does not enter the 
     * board from another board, this is the Y position that will be used to 
     * place the player. Used for the entry map of a scenario as well as testing.
     * 
     * @return  the start Y position assigned to this board
     */
    public int getStartY() {
        return startY;
    }
    /**
     * Set the starting X/Y coordinates of the player on this board. 
     * @param x     
     * @param y
     */
    public void setStartXY(int x, int y) {
        startX = x;
        startY = y;
    }

    /**
     * Constructor.
     *
     */
    public Board() {
        this.cells = new Cell[COLUMNS][ROWS];
        this.animated = new ArrayList<AnimationProxy>();
        this.adjacentBoards = new HashMap<String, String>();
        this.startX = -1;
        this.startY = -1;
        this.playerX = -1;
        this.playerY = -1;
    }
    /**
     * Is the terrain represented by this board above-ground?
     * @return  true if outside, false otherwise
     */
    public boolean isOutside() {
        return outside;
    }
    /**
     * 
     * @param outside
     */
    public void setOutside(boolean outside) {
        this.outside = outside;
    }
    /**
     * Get the list of pieces currently on the board that are animated.
     * @return  a list of <cdoe>AnimationProxy</code> elements, one for each animated piece on the board
     */
    public List<AnimationProxy> getAnimated() {
        return animated;
    }
    /**
     * Add a piece to the board as an animation. Since pieces are immutable, an 
     * <code>AnimationProxy</code> is generated for each animated piece that records 
     * its position and manages its animation, in order to keep such state out of 
     * the piece itself.
     * 
     * @param x
     * @param y
     * @param piece
     */
    void addAnimated(int x, int y, Animated piece) {
        animated.add(new AnimationProxy(x, y, piece));
    }
    /**
     * Remove the <code>AnimationProxy</code> for a piece on the board. 
     * @param x
     * @param y
     * @param piece
     */
    void removeAnimated(int x, int y, Animated piece) {
        for (int i=0, len = animated.size(); i < len; i++) {
            AnimationProxy proxy = animated.get(i);
            if (proxy.proxyFor(x, y, piece)) {
                animated.remove(proxy);
                return;
            }
        }
    }
    /**
     * Move an animated piece on the board. This is more efficient than removing 
     * the piece at one location and adding it to another, since that approach 
     * would involve creating a new animation proxy each time. Use this method 
     * to perform movement animation on the board.
     *  
     * @param fromX
     * @param fromY
     * @param piece     the piece that has been animated (may not be an instance of
     *                  <code>Animated</code>, as some animation like throwing/shooting is done
     *                  using proxies).
     * @param toX
     * @param toY
     */
    void moveAnimated(int fromX, int fromY, Piece piece, int toX, int toY) {
        if (piece instanceof Animated) {
            for (int i=0, len = animated.size(); i < len; i++) {
                AnimationProxy proxy = animated.get(i);
                if (proxy.proxyFor(fromX, fromY, piece)) { 
                    proxy.setXY(toX, toY);
                    return;
                }
            }
        }
    }
    /**
     * If the board is serialized (player leaves board or saves) while there are 
     * effects that are not transient, then saving will be delayed until these 
     * effects are played out. For example, you cannot save thrown items because 
     * when the player returns, they will appear to have hung in mid air the whole 
     * time. So instead, saving occurs on a timer that checks until there are no 
     * such effects, allowing them to all resolve their movement before the board 
     * is final serialized.
     * @return  true if there are any non transient effects currently in effect on the board.
     */
    public boolean hasNonTransientEffect() {
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                Cell cell = cells[x][y];
                if (!cell.hasNoEffects()) {
                    for (Bag.Entry<Effect> entry : cell.getEffects().asEntryList()) {
                        if (entry.getPiece().not(TRANSIENT)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Visit every cell on the board, for whatever reason.
     * @param visitor
     */
    public void visit(CellVisitor visitor) {
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                visitor.visit( cells[x][y], -1 );
            }
        }
    }
    
    /**
     * This searches a square of the board, centered on the range, from the
     * closest to the most distant squares, up and including the range of 
     * cells (so a range of 3 searches up to 3 cells away). It passes each 
     * cell to the visitor, if the visitor returns a cell, it is immediately 
     * returned, otherwise, all cells are traversed.
     */
    public void visitRange(Cell center, int range, boolean includeCenter, CellVisitor visitor) {
        int startX = center.getX();
        int startY = center.getY();
        
        Cell cell = null;
        if (includeCenter && !visitor.visit(center, 0)) {
            return;
        }
        // Three for loops... slow.
        for (int dist = 1; dist < range; dist++) {
            for (int y = -dist; y <= dist; y++) {
                if (y == -dist || y == dist) {
                    // At top and bottom, get every cell along x axis
                    for (int x = -dist; x <= dist; x++) {
                        cell = getCellAt(startX+x,startY+y);
                        if (cell != null && !visitor.visit(cell, dist)) {
                            return;
                        }
                    }
                } else {
                    // Just the edges
                    cell = getCellAt(startX+dist,startY+y);
                    if (cell != null && !visitor.visit(cell, dist)) {
                        return;
                    }
                    cell = getCellAt(startX-dist,startY+y);
                    if (cell != null && !visitor.visit(cell, dist)) {
                        return;
                    }
                }
            }
        }
    }
    
    public Cell find(CellFilter filter) {
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                if (filter.matches( cells[x][y], null )) {
                    return cells[x][y];
                }
            }
        }
        return null;
    }
    
    /**
     * Find a random cell that an item could be placed on that the player should 
     * be able to enter in order to pick up the item (cells that have traversable
     * terrain). Because this could include squares with agents that are not actually
     * movable, we also avoid cells with agents on them at the time we look.
     * @return  a cell that can take an agent or item 
     */
    public Cell findRandomCell() {
        Cell found = null;
        while (found == null || found.getTerrain().not(TRAVERSABLE) || found.getAgent() != null) {
            int x = Random.nextInt(Board.COLUMNS);
            int y = Random.nextInt(Board.ROWS);
            found = getCellAt(x, y);
        }
        return found;
    }
    
    public void rerenderBoard() {
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                Events.get().fireCellChanged(cells[x][y]);
            }
        }
    }
    
    /**
     * Starting at a specific cell, test adjacent cells with the selector
     * and continue finding and selecting adjacent cells until no adjacent
     * cells match.
     * @param start
     * @param filter
     * @return  list of cells that matched the selector
     */
    public List<Cell> findAdjacent(Cell start, CellFilter filter) {
        List<Cell> found = new ArrayList<Cell>();
        if (start != null) {
            if (filter.matches(start, null)) {
                found.add(start);
            }
            for (int i=0; i < found.size(); i++) {
                Cell current = found.get(i);
                for (Cell cell : current.getAdjacentCells(filter)) {
                    if (!found.contains(cell)) {
                        found.add(cell);
                    }
                }
            }
        }
        return found;
    }
    
    /**
     * Given a location on the board and a direction, get the adjacent cell.
     * 
     * @param x     the x location of the starting cell
     * @param y     the y location of the starting cell
     * @param dir   the direction to retrieve the adjacent cell from
     * @return      the adjacent cell
     */
    public Cell getAdjacentCell(int x, int y, Direction dir) {
        return getCellAt(x+dir.getXDelta(), y+dir.getYDelta());
    }
    /**
     * Get the cell currently occupied by the player.
     * @return  the cell currently occupied by the player
     */
    public Cell getCurrentCell() {
        return getCellAt(playerX, playerY);
    }
    /**
     * Get the cell at the given coordinates
     * @param x
     * @param y
     * @return  the cell at the given coordinates.
     */
    public Cell getCellAt(int x, int y) {
        return (x < 0 || x > (COLUMNS-1) || y < 0 || y > (ROWS-1)) ? 
            null : cells[x][y];
    }
    /**
     * Fire a color-related trigger across the board. All terrain that has 
     * color, and that matches the color of the event, will have its <code>
     * onTrigger</code> method fired. Each terrain type can have behavior 
     * under these circumstances. Triggers set up conditions for when such 
     * an event can occur.
     * @param ctx       context object containing game state
     * @param color     the color of the event; <code>colorEvent</code> will 
     *                  be called on all terrain of this color
     */
    public void fireColorEvent(Context ctx, Color color, Cell origin) {
        if (color == Color.NONE) {
            return;
        }
        Cell cell = null;
        Terrain terrain = null;
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                cell = cells[x][y];
                terrain = cell.terrain;
                if (terrain instanceof ColorListener) {
                    if (terrain.getColor() == color) {
                        ColorListener cl = (ColorListener)terrain;
                        cl.onColorEvent(ctx, cell, origin);
                    }
                    // Now that dual terrain is showing a particular piece, we want
                    // it to behave normally, including receiving color events, if
                    // appropriate. TODO: By creating a method like isOfColor(), the
                    // pieces could encapsulate whether or not they want to receive
                    // a color event, which would pull this into DualTerrain. This
                    // is the only case of such behavior in what turns out to be a 
                    // frequently used utility terrain, however. 
                    if (terrain instanceof DualTerrain) {
                        // Not all proxied terrains receive color events.
                        Terrain proxied = ((DualTerrain)terrain).getProxiedTerrain();
                        if (proxied instanceof ColorListener) {
                            if (proxied.getColor() == color) {
                                ColorListener cl = (ColorListener)proxied;
                                cl.onColorEvent(ctx, cell, origin);
                            }
                        }
                    }
                }
                Agent agent = cell.getAgent();
                if (agent != null && agent instanceof ColorListener) {
                    if (agent instanceof ColorListener) {
                        if (agent.getColor() == color) {
                            ColorListener cl = (ColorListener)agent;
                            cl.onColorEvent(ctx, cell, origin);
                        }
                    }
                }
            }
        }
    }
    /**
     * Set an adjacent board.
     * @param direction     the direction of the board
     * @param boardID       the boardID of the new board.
     */
    public void setAdjacentBoard(String direction, String boardID) {
        adjacentBoards.put(direction, boardID);
    }
    /**
     * Get a board adjacent to the current board in the indicated direction. 
     * This includes boards above and below the current one.
     * @param   direction
     * @return  the adjacent boardID or null if there is none.
     */
    public String getAdjacentBoard(Direction direction) {
        return adjacentBoards.get(direction.getName());
    }
    public void setStartInventory(String startInventory) {
        this.startInventory = startInventory;
    }
    public String getStartInventory() {
        return startInventory;
    }
    public void setMusic(String music) {
        this.music = music;
    }
    public String getMusic() {
        return music;
    }
    public String getScenarioName() {
        return scenarioName;
    }
    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
