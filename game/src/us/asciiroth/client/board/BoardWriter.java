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

import static us.asciiroth.client.core.Flags.PLAYER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import us.asciiroth.client.Registry;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Terrain;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Start with this storing in memory as a JSON data structure.
 *
 */
public class BoardWriter extends WriterBase {
    
    /**
     * The property specifying whether the map represents an 
     * outside or inside area (the player and other pieces may be drawn
     * differently as a result). 
     */
    public static final String OUTSIDE_MAP = "outside";
    /**
     * If provided, and the player does not enter from another map, this is
     * the starting X position of the player on the board.
     */
    public static final String START_X_KEY = "startX";
    /**
     * If provided, and the player does not enter from another map, this is
     * the starting Y position of the player on the board.
     */
    public static final String START_Y_KEY = "startY";
    /**
     * For testing purposes, if the player starts on this map rather than 
     * entering from another map, the player can be assigned a starting 
     * inventory. Comma-separated list of items.
     */
    public static final String START_INVENTORY = "startInv";
    /**
     * Not currently implemented, but there is a property to specify an mp3
     * file in case music is added later.
     */
    public static final String MUSIC = "music";
    /**
     * The property specifying a diagram described using arbitrarily chosen symbols
     * for each cell of the board. Their meaning is described by the <code>DIAGRAM_KEY</code>
     * portion of the map.
     */
    public static final String DIAGRAM_KEY = "diagram";
    /**
     * A property specifying a mapping between the symbols in the diagram and 
     * the terrain they represent. The value of each element in this JSON object
     * are either String keys representing an individual <code>Terrain</code> piece,
     * or an array of pieces, starting with a terrain piece but capable of including
     * items and an agent as well. The latter is useful because it allows the diagram
     * to indicate where items and agents should be placed, rather than having to 
     * specify their x/y coordinates separately. 
     */
    public static final String TERRAIN_KEY = "terrain";
    /**
     * An array of JSON objects that describe the pieces and their location on the 
     * board.  
     */
    public static final String PIECES_KEY  = "pieces";
    /**
     * For a given piece, the String key that describes the piece (essentially its 
     * serialization for the board JSON).
     */
    public static final String KEY_KEY     = "key";
    /**
     * For a given piece, its X coordinate on the board.
     */
    public static final String X_KEY       = "x";
    /**
     * For a given piece, its Y coordinate on the board.
     */
    public static final String Y_KEY       = "y";
    
    /**
     * If this is the start board, it can encode the name of the scenario
     * for the load feature.
     */
    public static final String SCENARIO_NAME = "name";
    /**
     * If this is the start board, it can encode the creator of the scenario.
     */
    public static final String CREATOR = "creator";
    /**
     * If this is the start board, it can encode a description of the scenario.
     */
    public static final String DESCRIPTION = "description";
    
    public String write(Board board, boolean preserveMetadata) {
        JSONObject b = new JSONObject();
        
        // Metadata
        for (int i=0, len = Direction.getMapDirections().size(); i < len; i++) {
            Direction dir = (Direction)Direction.getMapDirections().get(i);
            addString(b, dir.getName(), (preserveMetadata) ? board.getAdjacentBoard(dir) : "");
        }
        addBoolean(b, OUTSIDE_MAP, board.outside);
        addNumber(b, START_X_KEY, board.startX);
        addNumber(b, START_Y_KEY, board.startY);
        addString(b, START_INVENTORY, board.startInventory);
        addString(b, MUSIC, board.music);
        addString(b, SCENARIO_NAME, board.scenarioName);
        addString(b, CREATOR, board.creator);
        addString(b, DESCRIPTION, board.description);
        
        Set<JSONObject> pieceSet = new HashSet<JSONObject>();
        Map<Terrain, String> terrain = new HashMap<Terrain, String>();
        int asciiCode = 35; // skipping " character, which is an issue
        
        // Map
        JSONArray array = new JSONArray();
        for (int mapY=0; mapY < Board.ROWS; mapY++) {
            StringBuffer sb = new StringBuffer();
            for (int mapX=0; mapX < Board.COLUMNS; mapX++) {
                Cell cell = board.getCellAt(mapX, mapY);
                
                if (!terrain.containsKey(cell.getTerrain())) {
                    String sym = Character.toString((char)asciiCode++);
                    terrain.put(cell.getTerrain(), sym);
                    sb.append(sym);
                } else {
                    sb.append( (String)terrain.get(cell.getTerrain()) );
                }
                
                // Items
                for (Bag.Entry<Item> entry : cell.getBag().asEntryList()) {
                    for (int j=0; j < entry.getCount(); j++) {
                        addPiece(pieceSet, cell.getX(), cell.getY(), entry.getPiece());
                    }
                }
                // Agent. Player position is recorded and stored separately. 
                if (cell.getAgent() != null && cell.getAgent().not(PLAYER)) {
                    addPiece(pieceSet, cell.getX(), cell.getY(), cell.getAgent());
                }
            }
            array.set(mapY, new JSONString(sb.toString()));
        }
        b.put(DIAGRAM_KEY, array);
        
        // Terrain legend
        JSONObject legend = new JSONObject();
        for (Map.Entry<Terrain, String> entry: terrain.entrySet()) {
            legend.put(entry.getValue(), new JSONString(Registry.get().getKey(entry.getKey())));
        }
        b.put(TERRAIN_KEY, legend);
        
        array = new JSONArray();
        List<JSONObject> pieceList = new ArrayList<JSONObject>(pieceSet);
        for (int i=0; i < pieceList.size(); i++) {
            array.set(i, (JSONValue)pieceList.get(i));
        }
        b.put(PIECES_KEY, array);
        
        /*
        String ser = b.toString();
        verifySerialization(board, ser);
        return ser;
        */
        return b.toString();
    }

    private void addPiece(Set<JSONObject> pieceSet, int x, int y, Piece piece) {
        JSONObject obj = new JSONObject();
        obj.put(X_KEY, new JSONNumber(x));
        obj.put(Y_KEY, new JSONNumber(y));
        obj.put(KEY_KEY, new JSONString(Registry.get().getKey(piece)));
        pieceSet.add(obj);
    }
    /*
    private void verifySerialization(Board board, String ser) {
        BoardReader reader = new BoardReader();
        Board newBoard = new Board();
        reader.read(null, null, newBoard, ser, null);
        
        // Now, compare the two.
        for (int x=0; x < Board.COLUMNS; x++) {
            for (int y=0; y < Board.ROWS; y++) {
                Cell newCell = newBoard.getCellAt(x, y);
                Cell oldCell = board.getCellAt(x, y);
                if (newCell.getTerrain() != oldCell.getTerrain()) {
                    Util.showError("Terrain is not the same: {0}/{1}",
                        newCell.getTerrain().getName(), oldCell.getTerrain().getName());
                }
                for (int i=0; i < newCell.getBag().size(); i++) {
                    Item newItem = newCell.getBag().get(i);
                    Item oldItem = oldCell.getBag().get(i);
                    if (newItem != oldItem || 
                        newCell.getBag().getCount(newItem) != oldCell.getBag().getCount(oldItem)) {
                        Util.showError("Items are not the same or different count: {0}/{1}",
                            newItem.getName(), oldItem.getName());
                    }
                }
            }
        }
    }
    */
}
