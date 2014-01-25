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

import us.asciiroth.client.core.Game;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ConfirmQuitDialog extends CenteredPopupPanel {

    private MousyButton saveButton;
    
    public ConfirmQuitDialog() {
        super();
        addLabel("Save before quitting?");
        saveButton = addButton("Save", new ClickHandler() {
			public void onClick(ClickEvent event) {
				Game.get().saveGameAndQuit();
			}
        });
        addButton("Quit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				Game.get().quitApplication();
			}
        });
        
        addCancelButton();
    }
    @Override
    public void focus() {
        saveButton.setFocus(true);
    }
}
