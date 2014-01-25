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

import us.asciiroth.client.core.HasBoard;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;

/**
 * It's a complete bastardization of the SourcesMouseEvents interface.
 * I don't know if it's worth fixing this... it'll really confuse anyone
 * who tries to work with it. The "Sender" is a game cell, and the x and
 * y are the coordinates of the cell, not the mouse. Which is 1000% more
 * useful than what the API provides.
 * 
 * Make this a "Paintable Grid", fix the APIs, and leave it aside for the
 * game. It's more appropriate to the editor.
 *
 */
public class MousyGrid extends Grid implements SourcesMouseCellEvents {
    
    private HasBoard hasBoard;
    private MouseCellListenerCollection listeners;
    
    public MousyGrid(HasBoard hasBoard, int rows, int columns) {
        super(rows, columns);
        this.hasBoard = hasBoard;
        sinkEvents(Event.ONMOUSEOUT | Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEOVER);
    }
    
    public native void addSpan(Element table, int x, int y) /*-{
        table.rows[y].cells[x].innerHTML = "<span>&emsp;</span>";
    }-*/;
    @Override
    public void onBrowserEvent(Event event) {
        // event.preventDefault();
        super.onBrowserEvent(event);
        if (listeners != null) {
            Element td = getEventTargetCell(event);
            if (td == null) {
              return;
            }
            boolean shifted = event.getShiftKey();
            us.asciiroth.client.board.Cell cell = null;
            switch(event.getTypeInt()) {
            case Event.ONMOUSEDOWN:
                cell = findCell(event);
                if (cell != null) {
                    if (event.getButton() == Event.BUTTON_LEFT) {
                        listeners.fireMouseDown(td, cell, shifted);    
                    } else {
                        listeners.fireContextMenu(td, cell, shifted);
                    }
                }
                break;
            case Event.ONMOUSEUP:
                cell = findCell(event);
                if (cell != null) {
                    listeners.fireMouseUp(td, cell, shifted);
                }
                break;
            case Event.ONMOUSEOVER:
                cell = findCell(event);
                if (cell != null) {
                    listeners.fireMouseOver(td, cell, shifted);
                }
                break;
            case Event.ONMOUSEOUT:
                cell = findCell(event);
                if (cell != null) {
                    listeners.fireMouseOut(td, cell, shifted);
                }
                break;
            }
        }
    }
    
    private us.asciiroth.client.board.Cell findCell(Event event) {
        // You run into the strangeness in this method of two competing
        // Element APIs. Not very slick.
        Element td = getEventTargetCell(event);
        if (td == null) {
          return null;
        }
        Element tr = DOM.getParent(td);
        Element body = DOM.getParent(tr);
        int row = DOM.getChildIndex(body, tr);
        int column = DOM.getChildIndex(tr, td);
        
        return hasBoard.getBoard().getCellAt(column, row);
    }

    public void addMouseCellListener(MouseCellListener listener) {
        if (listeners == null) {
            listeners = new MouseCellListenerCollection();
        }
        listeners.add(listener);
    }

    public void removeMouseCellListener(MouseCellListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
}
