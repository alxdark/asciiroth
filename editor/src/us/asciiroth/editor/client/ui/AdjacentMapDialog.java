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

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Board;
import us.asciiroth.client.core.Direction;
import us.asciiroth.editor.client.Editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

public class AdjacentMapDialog extends PopupPanel {

    private Grid grid;
    
    public AdjacentMapDialog() {
        super(true, false);
        setStyleName("n-dialog");
        setWidth("48px");
        grid = new Grid(4,3);
        grid.setWidth("100%");
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        addAdjMap(0, 1, "icons/arrows/north.png", Direction.NORTH);
        addAdjMap(2, 1, "icons/arrows/south.png", Direction.SOUTH);
        addAdjMap(1, 2, "icons/arrows/east.png", Direction.EAST);
        addAdjMap(1, 0, "icons/arrows/west.png", Direction.WEST);
        addAdjMap(3, 0, "icons/arrows/up.gif", Direction.UP);
        addAdjMap(3, 2, "icons/arrows/down.gif", Direction.DOWN);
        add(grid);
    }
    
    private void addAdjMap(int row, int col, String url, final Direction dir) {
        Image image = new Image(url);
        image.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Board board = Editor.get().getBoard();
                final String boardID = board.getAdjacentBoard(dir);
                if (boardID == null || boardID.length() == 0) {
                    Util.showError("There is no " + dir.getName() + " map");
                } else {
                    // Figure out the base URL... all boards for a scenario are in the 
                    // same directory to make this simpler.
                    String path = Editor.get().getBoardFilePath();
                    String baseURL = (path.indexOf("\\") != -1) ?
                        path.substring(0, path.lastIndexOf("\\")) :
                        path.substring(0, path.lastIndexOf("/"));
                    Editor.get().loadBoard(baseURL+"/"+boardID+".js");
                }
            }
        });
        grid.setWidget(row, col, image);
        
    }
}
