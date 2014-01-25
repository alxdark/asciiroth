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
package us.asciiroth.client.ui;

import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.HasBoard;
import us.asciiroth.client.core.ModifiableSymbol;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.CellListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class BoardView implements CellListener { 

    protected HasBoard hasBoard;
    protected MousyGrid grid;
    protected Element tableEl;
    protected Renderer renderer = GWT.create(Renderer.class);
	
	public BoardView(HasBoard hasBoard) {
        this.hasBoard = hasBoard;
        grid = new MousyGrid(hasBoard, Board.ROWS, Board.COLUMNS);
        grid.setStyleName("n-grid");
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        RootPanel.get("boardPanel").add(grid);
        tableEl = grid.getElement();
	}
    public void addMouseCellListener(MouseCellListener listener) {
        grid.addMouseCellListener(listener);
    }
    
    public int getHeight() {
    	return tableEl.getOffsetHeight() + tableEl.getAbsoluteTop();
    }
    
    /* Did call this after reading a board, but all the model setters
     * are already painting the board, this was just redundant
    public void renderBoard(Board board) {
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                board.getCellAt(x, y).render();
            }
        }
    }
    */
    
    public void onCellChanged(Cell cell) {
        ModifiableSymbol sym = cell.getCurrentSymbol(null, null);
        renderer.render(cell.getTd(), sym.getEntity(), sym.getColor(), sym.getBackground());
    }
    public void onRerender(Cell cell, Piece piece, Symbol symbol) {
        ModifiableSymbol sym = cell.getCurrentSymbol(piece, symbol);
        renderer.render(cell.getTd(), sym.getEntity(), sym.getColor(), sym.getBackground());
    }

    public Element getElementAt(int mapX, int mapY) {
        return grid.getCellFormatter().getElement(mapY, mapX);
    }
    
    // Used by the editor.
    protected native void renderWithoutCache(Element table, int x, int y, String html) /*-{
        table.rows[y].cells[x].innerHTML = html;
    }-*/;
}
