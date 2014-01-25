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

import static us.asciiroth.client.core.Flags.PARALYZED;
import static us.asciiroth.client.core.Flags.TURNED_TO_STONE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.agents.Paralyzed;
import us.asciiroth.client.agents.Statue;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.json.BoardJSON;
import us.asciiroth.client.json.PieceJSON;
import us.asciiroth.client.ui.BoardView;

import com.google.gwt.user.client.Command;

/**
 * The board reader is used by the editor which does not need the player
 * information, so this is stored in the PlayerData object and only read
 * as part of loading a game.
 *
 */
public class BoardReader {
	public BoardReader() {
	}
    
	/**
	 * Load the map and place the player at the indicated coordinates.
     * @param view     The view; optional but if it exists, the TD element will be 
     *                  cached in the cell for performance reasons
	 * @param player   the current player instance, which communicates the players X/Y 
     *                  position as well as the URL of the board
	 * @param board    the board instance to initialize
	 * @param json     the json to deserialize convert to the board
	 * @param command  an optional callback command to execute when deserialization is done
	 */
    public void read(BoardView view, Player player, Board board, String json, Command command) {
        BoardJSON bjson = BoardJSON.parseJSON(json);
        board.music = bjson.music();
        board.outside = bjson.outside();
        board.scenarioName = bjson.scenarioName();
        board.creator = bjson.creator();
        board.description = bjson.description();
        for (int i=0, len = Direction.getMapDirections().size(); i < len; i++) {
            Direction dir = (Direction)Direction.getMapDirections().get(i);
            board.setAdjacentBoard(dir.getName(), bjson.adjacentBoard(dir.getName()));
        }
        parseDiagram(view, board, bjson);
        parsePieces(board, bjson);
        parseStartData(player, board, bjson);
        
        if (command != null) {
            command.execute();
        }
	}

    private void parseDiagram(BoardView view, Board board, BoardJSON bjson) {
        String token, key = null;
        Cell cell = null;
        // Exists: we're loading a board on top of an existing board, and thus
        // can use the TD elements already cached, or there's no view so we 
        // just can't do that (as is the case when we use the reader in tests)
        boolean exists = (board.cells[0][0] != null);
        
        //Duration d = new Duration();
        for (int mapY=0; mapY < Board.ROWS; mapY++) {
            String row = bjson.diagram().get(mapY);
            for (int mapX=0; mapX < Board.COLUMNS; mapX++) {
                // Arbitrary token mapped to terrain object.
                token = row.substring(mapX, mapX+1);
                key = bjson.legend(token);
                if (key == null) {
                    throw new RuntimeException("Token " + token + " not mapped to a piece");
                }
                // This is the only place that a cell should ever be created.
                cell = board.cells[mapX][mapY] = (exists) ? 
                    cell.init(board) : new Cell(board, view.getElementAt(mapX, mapY), mapX, mapY);
                addTerrainToBoard(board, cell, key);
            }
        }
        //Enabling this will break the editor, BTW
        //DOM.setInnerText(DOM.getElementById("time"), ""+d.elapsedMillis());
    }
    private void parsePieces(Board board, BoardJSON bjson) {
        if (bjson.pieces() != null && bjson.pieces().size() > 0) {
            for (int i=0, len = bjson.pieces().size(); i < len; i++) {
                PieceJSON pjson = bjson.pieces().get(i);
                Cell cell = board.getCellAt(pjson.x(), pjson.y());
                addPieceToBoard(board, cell, pjson.key());
            }
        }
    }
    private void addTerrainToBoard(Board board, Cell cell, String key) {
        if (key != null) {
            Piece piece = Registry.get().getPiece(key);
            if (piece == null) {
                throw new RuntimeException("Could not find piece with key: " + key);
            }
            if (!(piece instanceof Terrain)) {
                throw new RuntimeException("The key " + key + " added as terrain, but it is not.");
            }
            cell.setTerrain((Terrain)piece);
        }
    }
    private void addPieceToBoard(Board board, Cell cell, String key) {
        if (key != null) {
            Piece piece = Registry.get().getPiece(key);
            if (piece == null) {
                throw new RuntimeException("No piece with key: " + key);
            }
            if (piece instanceof Terrain) {
                throw new RuntimeException("Terrain " + key + " incorrectly added as item/agent piece");
            } else if (piece instanceof Item) {
                cell.getBag().add((Item)piece);
            } else if (piece instanceof Agent) {
                cell.setAgent((Agent)piece);
            }
        }
    }
    private void parseStartData(Player player, Board board, BoardJSON bjson) {
        // This happens when testing deserialization of the board
        if (player == null) {
            return;
        }
        board.setStartXY(bjson.startX(), bjson.startY());
        board.startInventory = bjson.startInventory();
        
        // Does the player have a position (not -1/-1)
        int startX = player.getStartX(); 
        int startY = player.getStartY();
        
        if (startX == -1 && startY == -1) {
            // No, use the start values
            startX = board.getStartX();
            startY = board.getStartY();
            if (board.startInventory != null) {
                String[] items = board.startInventory.split(",");
                for (int i=0; i < items.length; i++) {
                    Item item = (Item)Registry.get().getPiece(items[i].trim());
                    if (item != null) {
                        player.getBag().add(item);
                    }
                }
            }
        }
        // Place the player. Take note of the player's condition: he or she might be
        // paralyzed or turned to stone, and we want to represent that, recreating the 
        // AgentProxy involved to do so.  
        Cell cell = board.getCellAt(startX, startY);
        if (cell != null) {
            if (player.is(PARALYZED)) {
                cell.setAgent(new Paralyzed(player));
            } else if (player.is(TURNED_TO_STONE)) {
                cell.setAgent(new Statue(player, Color.NONE));
            } else {
                cell.setAgent(player);    
            }
        }
    }
}
