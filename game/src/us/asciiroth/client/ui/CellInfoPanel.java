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

import static us.asciiroth.client.core.Flags.HIDES_ITEMS;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Item;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * This panel appears when you mouse over a cell and provides a legend for 
 * the contents of the cell. It is really complicated becausee mouseout
 * doesn't always seem to work...
 *
 */
public class CellInfoPanel extends PopupPanel implements MouseCellListener {
    
    private HTML content;
    private Cell lastCell;
    private Timer cellShow;
    private Timer cellHide;
    
    public CellInfoPanel() {
        super(true, false);
        cellShow = new Timer() {
            @Override
            public void run() {display();}
        };
        cellHide = new Timer() {
            @Override
            public void run() {hide();}
        };
        content = new HTML();
        content.setStyleName("n-info");
        add(content);
    }
//    @Override
//    public void show() {
//        super.show();
//        // WARN: This could be wrong
//        setPreviewingAllNativeEvents(false);
//        // DOM.removeEventPreview(this); // no, bad dialog
//    }
    public void display() {
        content.setHTML( renderCellInfo(lastCell) );
        setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                // TODO: We'd like to get the width of the TD
                int left = lastCell.getTd().getAbsoluteLeft() + 30;
                int top = lastCell.getTd().getAbsoluteTop();
                setPopupPosition(left, top);
            }
        });
    }
    public void onMouseOver(Element td, Cell cell, boolean shifted) {
        if (!DialogManager.get().isPaused() && cell != lastCell) {
            // This lastCell thing prevents FF from resetting the 
            // timer over and over on animated cells, where the mouseover
            // event fires repeatedly
            cellShow.cancel();
            lastCell = cell;
            cellShow.schedule(600);
            cellHide.schedule(2100);
        }
    }
    public void onMouseDown(Element td, Cell cell, boolean shifted) {
        cellShow.cancel();
        cellHide.cancel();
        hide();
    }
    public void onMouseUp(Element td, Cell cell, boolean shifted) {
    }
    public void onContextMenu(Element td, Cell cell, boolean shifted) {
    }
    public void onMouseOut(Element td, Cell cell, boolean shifted) {
        cellShow.cancel();
        cellHide.cancel();
        hide();
    }
    protected String renderCellInfo(Cell cell) {
        StringBuffer sb = new StringBuffer();
        sb.append(Util.renderSymbolAndLabel(false, cell.getTerrain(), 1));
        if (cell.getAgent() != null) {
            sb.append("<br/>");
            sb.append(Util.renderSymbolAndLabel(false, cell.getAgent(), 1));
        }
        if (!cell.getBag().isEmpty() && cell.getTerrain().not(HIDES_ITEMS)) {
            Item item = cell.getBag().last();
            int count = cell.getBag().getCount(item); 
            sb.append("<br/>");
            sb.append(Util.renderSymbolAndLabel(false, item, count));
        }
        return sb.toString();
    }
}
