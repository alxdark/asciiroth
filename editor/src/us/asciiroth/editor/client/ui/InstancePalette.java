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
package us.asciiroth.editor.client.ui;

import us.asciiroth.client.Registry;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.Piece;
import us.asciiroth.editor.client.InstanceEditor;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InstancePalette extends VerticalPanel {

    private InstanceEditor selectedEditor;
    
    public InstancePalette() {
        super();
        setStyleName("n-instancePalette");
        setWidth("100%");
        RootPanel.get("instancePaletteHolder").add(this);
    }
    
    public void createInstance(String template) {
        for (int i=0; i < getWidgetCount(); i++) {
            InstanceEditor editor = (InstanceEditor)getWidget(i);
            if (editor.getCurrentValue() == template) {
                setSelectedEditor(editor);
                return; // only one allowed.
            }
        }
        InstanceEditor editor = new InstanceEditor(this, template);
        insert(editor, 0);
        scrollToTop(getParent().getElement());
        setSelectedEditor(editor);
    }
    private native void scrollToTop(Element el) /*-{
        el.scrollTop = 0;
    }-*/;
    public InstanceEditor getSelectedEditor() {
        return selectedEditor;
    }
    public void setSelectedEditor(InstanceEditor editor) {
        if (this.selectedEditor != null) {
            this.selectedEditor.selected(false);
        }
        editor.selected(true);
        this.selectedEditor = editor;
    }
    public void selectPiece(Piece piece) {
        if (this.selectedEditor != null) {
            this.selectedEditor.selected(false);
        }
        String key = Registry.get().getKey(piece);
        for (int i=0; i < getWidgetCount(); i++) {
            InstanceEditor editor = (InstanceEditor)getWidget(i);
            if (editor.getCurrentValue().equals(key)) {
                editor.selected(true);
                this.selectedEditor = editor;
                return;
            }
        }
    }
    public void resetToBoard(Board board) {
        // Try and preserve the selection based on value;
        String value = (this.selectedEditor != null) ? this.selectedEditor.getCurrentValue() : null;
        
        this.selectedEditor = null;
        while(getWidgetCount() >0) {
            remove(getWidgetCount()-1);
        }
        Registry reg = Registry.get();
        for (int y=0; y < Board.ROWS; ++y) {
            for (int x=0; x < Board.COLUMNS; ++x) {
                Cell cell = board.getCellAt(x, y);
                String key = reg.getKey(cell.getTerrain());
                createInstance(key);
                
                for (Bag.Entry<Item> entry : cell.getBag().asEntryList()) {
                    key = reg.getKey(entry.getPiece());
                    createInstance(key);
                }
                if (cell.getAgent() != null) {
                    key = reg.getKey(cell.getAgent());
                    createInstance(key);
                }
            }
        }
        // If there's an editor with the value that was selected, select it.
        if (value != null) {
            for (int i=0; i < getWidgetCount(); i++) {
                InstanceEditor editor = (InstanceEditor)getWidget(i);
                if (editor.getCurrentValue() == value) {
                    setSelectedEditor(editor);
                    return;
                }
            }
        }
    }
}
