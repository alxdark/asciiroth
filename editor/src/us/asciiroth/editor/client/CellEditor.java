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
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.ui.CenteredPopupPanel;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.MousyButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;

public class CellEditor extends CenteredPopupPanel {

    private FlexTable grid;
    private int currentRow;
    
    public CellEditor(Cell cell) {
        super();
        addStyleName("n-cellEditor");
        grid = new FlexTable();
        grid.setCellSpacing(0);
        grid.setCellPadding(3);
        grid.getColumnFormatter().setStyleName(0, "n-ceSymbol");
        grid.getColumnFormatter().setStyleName(0, "n-ceKey");
        grid.getColumnFormatter().setStyleName(0, "n-ceCommands");
        
        addComponent(grid);

        renderCell(cell);
        grid.getFlexCellFormatter().setRowSpan(0, 3, currentRow);
        
        FlowPanel panel = new FlowPanel();
        panel.setStyleName("n-ceRightPanel");
        addCellCommands(panel, cell);
        addComponent(panel);
        
        addButton(new MousyButton("5em", "Close", DialogManager.CLOSE_DIALOG_HANDLER));
    }
    
    private void addCellCommands(final FlowPanel panel, final Cell cell) {
        Hyperlink startHere = new Hyperlink("Start Player Here", "starthere");
        startHere.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Editor.get().setDirty(true);
                Board board = Editor.get().getBoard();
                board.setStartXY(cell.getX(), cell.getY());
                DialogManager.get().pop();
            }
        });
        panel.add(startHere);
    }
    
    private void renderCell(Cell cell) {
        while(grid.getRowCount() > 0) {
            grid.removeRow(grid.getRowCount()-1);    
        }
        addRow(cell, cell.getTerrain(), 1);
        for (Bag.Entry<Item> entry : cell.getBag().asEntryList()) {
            addRow(cell, entry.getPiece(), entry.getCount());
        }
        if (cell.getAgent() != null) {
            addRow(cell, cell.getAgent(), 1);
        }
    }
    
    private void addRow(final Cell cell, final Piece piece, int count) {
        // Symbol
        grid.setHTML(currentRow, 0, Util.renderSymbolAndLabel(false, piece, count));
        // Key, which tells us a lot about this item that otherwise we can't see
        String key = Registry.get().getKey(piece);
        grid.setText(currentRow, 1, key);
        
        FlowPanel panel = new FlowPanel();
        // Commands.
        Hyperlink def = null;
        if (piece instanceof Terrain) {
            def = new Hyperlink("Default", "def");
            def.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Editor.get().setDirty(true);
                    cell.setTerrain(Editor.get().getBoardEditor().getDefaultTerrain(), true);
                    renderCell(cell);
                }
            });
            panel.add(def);
        } else if (piece instanceof Item) {
            def = new Hyperlink("Add", "add");
            def.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Editor.get().setDirty(true);
                    cell.getBag().add((Item)piece);
                    renderCell(cell);
                }
            });
            panel.add(def);
            panel.add(new HTML(" &bull; "));
            def = new Hyperlink("Remove", "remove");
            def.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Editor.get().setDirty(true);
                    cell.getBag().remove((Item)piece);
                    renderCell(cell);
                }
            });
            panel.add(def);
        } else if (piece instanceof Agent) {
            def = new Hyperlink("Remove", "remove");
            def.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    Editor.get().setDirty(true);
                    cell.removeAgent((Agent)piece);
                    renderCell(cell);
                }
            });
            panel.add(def);
        }
        panel.add(new HTML(" &bull; "));
        def = new Hyperlink("Select", "select");
        def.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Editor.get().getInstancePalette().selectPiece(piece);
                Editor.get().setDirty(true);
                DialogManager.get().pop();
            }
        });
        panel.add(def);
        grid.setWidget(currentRow++, 2, panel);
    }
    @Override
    public void focus() {
    }
}
