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

import java.util.ArrayList;

import us.asciiroth.client.board.Cell;

import com.google.gwt.user.client.Element;

public class MouseCellListenerCollection extends ArrayList<MouseCellListener> {
	private static final long serialVersionUID = 1L;
	
	public void fireMouseDown(Element td, Cell cell, boolean shifted) {
        MouseCellListener listener;
        for (int i=0, len = size(); i < len; i++) {
            listener = (MouseCellListener)get(i);
            listener.onMouseDown(td, cell, shifted);
        }
    }
    public void fireMouseOver(Element td, Cell cell, boolean shifted) {
        MouseCellListener listener;
        for (int i=0, len = size(); i < len; i++) {
            listener = (MouseCellListener)get(i);
            listener.onMouseOver(td, cell, shifted);
        }
    }
    public void fireMouseUp(Element td, Cell cell, boolean shifted) {
        MouseCellListener listener;
        for (int i=0, len = size(); i < len; i++) {
            listener = (MouseCellListener)get(i);
            listener.onMouseUp(td, cell, shifted);
        }
    }
    public void fireMouseOut(Element td, Cell cell, boolean shifted) {
        MouseCellListener listener;
        for (int i=0, len = size(); i < len; i++) {
            listener = (MouseCellListener)get(i);
            listener.onMouseOut(td, cell, shifted);
        }
    }
    public void fireContextMenu(Element td, Cell cell, boolean shifted) {
        MouseCellListener listener;
        for (int i=0, len = size(); i < len; i++) {
            listener = (MouseCellListener)get(i);
            listener.onContextMenu(td, cell, shifted);
        }
    }
}
