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
package us.asciiroth.client.terrain;

import java.util.List;

import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.board.CellVisitor;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.State;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TerrainProxy;

import com.google.gwt.user.client.Random;

/**
 * Terrain utilities.
 *
 */
public class TerrainUtils {
    
    public static void removeDecorator(final Event event, final TerrainProxy terrain) {
        event.getBoard().visit(new CellVisitor() {
            public boolean visit(Cell cell, int range) {
                // real terrains, not apparent terrains, and must force proxy replace
                if (terrain.equals( cell.getTerrain() )) {
                    TerrainProxy tp = (TerrainProxy)cell.getTerrain();
                    cell.setTerrain( tp.getProxiedTerrain(), true );
                }
                return false;
            }
        });
    }
    
    /**
     * Given a cell with a terrain that has a direction, and the current 
     * direction of that terrain, this method creates and caches a terrain
     * piece with an opposite direction. It does not change the board. 
     * 
     * @param cell  the target cell
     * @param dir   the current direction of the terrain
     * @return      the new terrain piece with its direction reversed
     */
    public static Terrain getTerrainWithReverseDirection(Cell cell, Direction dir) {
        String key = Registry.get().getKey(cell.getTerrain());
        String[] parts = key.split(dir.getName());
        key = parts[0] + dir.getReverseDirection().getName() + parts[1];
        return (Terrain)Registry.get().getPiece(key);
    }
    
    /**
     * To support animated pieces that loop around the board, this method returns
     * the approximate cell located on the opposite side of the board. It's 
     * approximate because there are many edge cases that aren't worth considering
     * in this method.
     * @param cell
     * @param direction
     * @return  approximate cell on the opposite side of the board
     */
    public static Cell getCellOnOppositeSide(Cell cell, Direction direction) {
        // Top or bottom of map, preference north/south
        if (cell.getY() == 0 || cell.getY() == (Board.ROWS-1)) {
            if (direction.isNortherly()) {
                return cell.getBoard().getCellAt(cell.getX(), Board.ROWS-1);
            } else if (direction.isSoutherly()) {
                return cell.getBoard().getCellAt(cell.getX(), 0);
            } else if (direction.isEasterly()) {
                return cell.getBoard().getCellAt(0, cell.getY());
            } else if (direction.isWesterly()) {
                return cell.getBoard().getCellAt(Board.COLUMNS-1, cell.getY());
            }
        }
        // left or right of map, preference east/west
        else {
            if (direction.isEasterly()) {
                return cell.getBoard().getCellAt(0, cell.getY());
            } else if (direction.isWesterly()) {
                return cell.getBoard().getCellAt(Board.COLUMNS-1, cell.getY());
            } else if (direction.isNortherly()) {
                return cell.getBoard().getCellAt(cell.getX(), Board.ROWS-1);
            } else if (direction.isSoutherly()) {
                return cell.getBoard().getCellAt(cell.getX(), 0);
            } 
        }
        return null;
    }
    
    /**
     * Given a list of directions, choose one at random.
     * @return  a randomly chosen direction taken from the list of directions
     */
    public static Direction getRandomDirection() {
        int i = Random.nextInt(Direction.getAdjacentDirections().size());
        return (Direction)Direction.getAdjacentDirections().get(i);
    }
    
    /**
     * Give a list of cells, choose one at random.
     * @param cells
     * @return  a randomly chosen cell taken from the list of cells
     */
    public static Cell getRandomCell(List<Cell> cells) {
        return (cells.isEmpty()) ?
            null : (Cell)cells.get(Random.nextInt(cells.size()));
    }
    
    /**
     * Assuming the indicated cell has a terrain type with state, this method
     * toggles the state on/off and then changes the terrain for that cell. 
     * This method does change the board. <em>This method will not toggle the
     * state while an agent is in the cell.</em> That's almost never what you
     * want and the source of much oddity in the game if it happens.
     * @param cell
     * @param state
     */
    public static void toggleCellState(Cell cell, Terrain terrain, State state) {
        if (cell.getAgent() == null) {
            Terrain other = getTerrainOtherState(terrain, state);
            cell.setTerrain(other, false);
        }
    }
    
    /**
     * Get this terrain piece but with its state toggled.If a piece is supplied that
     * does not have state, it will simply be returned by this method. The new terrain 
     * piece is cached by the Registry, but this method does not change the board.
     * Be careful with this method that you don't change a terrain's state while an 
     * agent is on the terrain that would have a negative effect. The method <code>
     * toggleCellState</code> prevents this.
     *
     * @param terrain
     * @param state
     * @return      the new terrain piece with its state toggled on/off
     */
    public static Terrain getTerrainOtherState(Terrain terrain, State state) {
        String key = Registry.get().getKey(terrain);
        if (state.isOn()) {
            key = key.replaceAll("\\bon\\b", "off");
        } else {
            key = key.replaceAll("\\boff\\b", "on");
        }
        return (Terrain)Registry.get().getPiece(key);
    }    
}
