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
package us.asciiroth.editor.client;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Agent;
import us.asciiroth.client.core.HasBoard;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.ModifiableSymbol;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.Floor;
import us.asciiroth.client.terrain.Ocean;
import us.asciiroth.client.ui.BoardView;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.MouseCellListener;
import us.asciiroth.editor.client.ui.BrushPalette;

import com.google.gwt.user.client.Element;

public class BoardEditor extends BoardView implements MouseCellListener {
    
    private static final String SYMBOL_WITH_DECORATOR_MARK = 
        "<span class='{0}' style='color: {1}; background-color: {2}'>{3}</span>";
    
    private Cell lastCell;
    private boolean dragging;
    private BrushPalette brushPalette;
    private CellEditor cellEditor;
    
    public BoardEditor(HasBoard hasBoard) {
        super(hasBoard);
        grid.addMouseCellListener(this);
        // Note: Ugh. This is different than HasBoard, why not HasBrushPalette?
        // But then again, why rewrite it, it's just the map editor.
        brushPalette = Editor.get().getBrushPalette();
    }
    public void onMouseDown(Element td, Cell cell, boolean shifted) {
        if (shifted) {
            showCellEditor(td, cell);
        } else {
            dragging = true;
            paint(td, cell, true);
        }
    }
    public void onMouseOver(Element td, Cell cell, boolean shifted) {
        if (dragging && cell != lastCell) {
            lastCell = cell;
            paint(td, cell, false);
        }
        brushPalette.setXY(cell.getX(), cell.getY());
    }
    public void onMouseUp(Element td, Cell cell, boolean shifted) {
        dragging = false;
    }
    public void onContextMenu(Element td, Cell cell, boolean shifted) {
        Terrain terrain = cell.getTerrain();
        Editor.get().getInstancePalette().selectPiece(terrain);
    }
    public void onMouseOut(Element td, Cell cell, boolean shifted) {}
    
    public Terrain getDefaultTerrain() {
        if (hasBoard.getBoard().isOutside()) {
            return (Terrain)Registry.get().getPiece(Ocean.class);
        } else {
            return (Terrain)Registry.get().getPiece(Floor.class);
        }
    }
    public void removePiece(Piece piece) {
        Editor.get().setDirty(true);
        Board board = hasBoard.getBoard();
        Terrain defaultTerrain = getDefaultTerrain();
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                Cell cell = board.getCellAt(x, y);
                if (piece instanceof Terrain) {
                    if (cell.getTerrain() == piece) {
                        cell.setTerrain(defaultTerrain, true);
                    }
                } else if (piece instanceof Item) {
                    cell.getBag().remove((Item)piece);
                } else if (piece instanceof Agent) {
                    if (cell.getAgent() == piece) {
                        cell.removeAgent((Agent)piece);    
                    }
                }
            }
        }
    }
    public void updatePiece(Piece oldPiece, Piece newPiece) {
        Editor.get().setDirty(true);
        Board board = hasBoard.getBoard();
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                Cell cell = board.getCellAt(x, y);
                if (oldPiece instanceof Terrain && cell.getTerrain() == oldPiece) {
                    cell.setTerrain((Terrain)newPiece, true);
                } else if (oldPiece instanceof Item && cell.getBag().contains((Item)oldPiece)) {
                    cell.getBag().remove((Item)oldPiece);
                    cell.getBag().add((Item)newPiece);
                } else if (oldPiece instanceof Agent && cell.getAgent() == oldPiece) {
                    cell.removeAgent((Agent)oldPiece);
                    cell.setAgent((Agent)newPiece);
                }
            }
        }
    }
    
    private void showCellEditor(Element td, Cell cell) {
        cellEditor = new CellEditor(cell);
        DialogManager.get().push(cellEditor);
    }
    /**
     * 
     * @param td
     * @param cell
     * @param doAll - should any kind of piece be rendered. Used to limit mouse dragging
     *  to the painting of terrain only. Neither items nor agents are usefully laid down
     *  this way.
     */
    private void paint(Element td, Cell cell, boolean doAll) {
        Editor.get().setDirty(true);
        InstanceEditor editor = Editor.get().getInstancePalette().getSelectedEditor();
        
        if (editor != null) {
            Piece piece = editor.getPiece();
            if (piece == null) {
                dragging = false;
                // This behavior is just mysterious to first time users. They select a piece
                // in the editor and immediatly try and draw on the board and that works less
                // and less often as pieces get more complicated.
                Util.showError(
                    "The piece in the editor is not complete.<br/>"+
                    "Replace all <code>{token}</code> instances with values:<br/>"+
                    "<code>{color}</code> &rarr; HTML color word like 'Orchid' or 'Red'<br/>"+
                    "<code>{direction}</code> &rarr; North, South, East, etc.<br/>"+
                    "<code>{state}</code> &rarr; 'on' or 'off'<br/><br/>"+
                    "The piece will display on the left when the key is valid.<br/><br/>"+
                    "Embedded pieces (items, terrains) that have multiple parts must be escaped with ^.<br/>"+
                    "Triffids have keys like this: 'Triffid|Blue', so when embedded use<br/>"+
                    "'AgentCreator|Triffid^Blue' not 'AgentCreator|Triffid|Blue'.<br/>"+
                    "See the map-making guide for full details.");
            } else {
                editor.setLastPaintedPiece();
                if (piece instanceof Terrain) {
                    paintTerrain(cell, (Terrain)piece);
                } else if (piece instanceof Item && doAll) {
                    Item item = (Item)piece;
                    if (cell.getBag().contains(item)) {
                        cell.getBag().remove(item);
                    } else {
                        cell.getBag().add(item);    
                    }
                } else if (piece instanceof Agent && doAll) {
                    Agent agent = (Agent)piece;
                    if (cell.getAgent() == agent) {
                        cell.removeAgent(agent);
                    } else {
                        cell.setAgent(agent);
                    }
                }
                Events.get().fireCellChanged(cell);
            }
        }
    }
    private void paintTerrain(Cell targetCell, Terrain terrain) {
        int brushSize = brushPalette.getBrushSize();
        int xStart = targetCell.getX() - Math.round(brushSize/2);
        int yStart = targetCell.getY() - Math.round(brushSize/2);

        Board board = hasBoard.getBoard();
        for (int i= xStart, len = (xStart+brushSize); i < len; ++i) {
            for (int j= yStart, len2 = (yStart+brushSize); j < len2; ++j) {
                Cell cell = board.getCellAt(i, j);
                if (cell != null) {
                    if (terrain != cell.getTerrain()) {
                        cell.setTerrain(terrain, true);
                        Events.get().fireCellChanged(cell);
                    }
                }
            }
        }
    }
    @Override
    public void onCellChanged(Cell cell) {
        ModifiableSymbol sym = cell.getCurrentSymbol(null, null);
        String className = (cell.getTerrain() instanceof TerrainProxy) ? "sc" : "s";
        String html = renderSymbolWithMark(className, sym.getEntity(), sym.getColor(), sym.getBackground());
        renderWithoutCache(tableEl, cell.getX(), cell.getY(), html);
    }
    @Override
    public void onRerender(Cell cell, Piece piece, Symbol symbol) {
        ModifiableSymbol sym = cell.getCurrentSymbol(piece, symbol);
        String className = (cell.getTerrain() instanceof TerrainProxy) ? "sc" : "s";
        String html = renderSymbolWithMark(className, sym.getEntity(), sym.getColor(), sym.getBackground());
        renderWithoutCache(tableEl, cell.getX(), cell.getY(), html);
    }

    /**
     * For the editor, render a symbol with a mark if it is a decorator. 
     * @param className
     * @param entity
     * @param color
     * @param bgcolor
     * @return  string of HTML to render the symbol
     */
    public String renderSymbolWithMark(String className, String entity, String color, String bgcolor) {
        return Util.format(SYMBOL_WITH_DECORATOR_MARK, className, color, bgcolor, entity);
    }
    
    
}
