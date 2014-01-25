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

import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.HTMLDialog;
import us.asciiroth.editor.client.Editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class ApplicationBar extends HorizontalPanel {

    private HTMLDialog help;
    private AdjacentMapDialog adjMapDialog;
    
    public ApplicationBar() {
        super();
        setStyleName("n-appBar");
        setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        createBar();
        
        RootPanel.get("appBar").add(this);
    }
    
    private void createBar() {
        final Button adjMapButton = new Button("<img src='icons/arrow_out.png'/>");
        adjMapButton.setTitle("Navigate to adjacent map");
        adjMapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
                if (adjMapDialog == null) {
                    adjMapDialog = new AdjacentMapDialog();
                }
                adjMapDialog.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int left = adjMapButton.getElement().getAbsoluteLeft()  - offsetWidth;
                        int top = adjMapButton.getElement().getAbsoluteTop() + adjMapButton.getOffsetHeight();
                        adjMapDialog.setPopupPosition(left, top);
                    }
                });
			}
        });
        add(adjMapButton);
        
        Button button = new Button("<img src='icons/table_edit.png'/>");
        button.setTitle("Edit map metadata");
        button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Editor.get().showMetadataDialog();
			}
        });
        add(button);
        
        button = new Button("<img src='icons/add.png'/>");
        button.setTitle("New map");
        button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Editor.get().newBoard();
			}
        });
        add(button);
        
        button = new Button("<img src='icons/folder.png'/>");
        button.setTitle("Load map");
        button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Editor.get().loadBoard(null);
			}
        });
        add(button);
        
        button = new Button("<img src='icons/disk.png'/>");
        button.setTitle("Save map");
        button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Editor.get().saveBoard(null);
			}
        });
        add(button);
        
        button = new Button("<img src='icons/disk_multiple.png'/>");
        button.setTitle("Save map as");
        button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Editor.get().saveBoardAs();
			}
        });
        add(button);
        
        // if (Editor.get().getProfile().isAirProvided()) {
            button = new Button("<img src='icons/control_play_blue.png'/>");
            button.setTitle("Play map");
            button.addClickHandler(new ClickHandler() {
            	public void onClick(ClickEvent event) {
            		Editor.get().testBoard();
            	}
            });
            add(button);
        // }
        
        button = new Button("<img src='icons/style_add.png'/>");
        button.setTitle("Make up name");
        button.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		Editor.get().makeUpName();
        	}
        });
        add(button);
        
        button = new Button("<img src='icons/help.png'/>");
        button.setTitle("Editor Help");
        button.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
                if (help == null) {
                    help = new HTMLDialog();
                    help.setWidth("400px");
                }
                help.setHTML("editor-help.html");
                DialogManager.get().push(help);
        	}
        });
        add(button);
    }
}
