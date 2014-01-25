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

import us.asciiroth.client.ui.CenteredPopupPanel;
import us.asciiroth.client.ui.DialogManager;
import us.asciiroth.client.ui.MousyButton;
import us.asciiroth.editor.client.Editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;

public class SaveFirstDialog extends CenteredPopupPanel {

    private final MousyButton saveButton;
    
    public SaveFirstDialog(final String prompt, final Command command) {
        super();
        addComponent(new Label(prompt));
        
        saveButton = new MousyButton("6em", "Save");
        saveButton.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
                Editor.get().saveBoard(command);
                DialogManager.get().pop();
        	}
        });
        addButton(saveButton);
        
        MousyButton cancelButton = new MousyButton("6em", "Continue");
        cancelButton.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
                command.execute();
                DialogManager.get().pop();
        	}
        });
        addButton(cancelButton);
    }
    
    @Override
    public void center() {
        super.center();
        focus();
    }
    @Override
    public void focus() {
        saveButton.setFocus(true);
    }
}
