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


import java.util.List;

import us.asciiroth.client.core.Game;
import us.asciiroth.client.store.NamesCallback;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class SaveGameDialog extends GameListingDialog {
    
    private TextBox name;
    private Label error;
    
    public SaveGameDialog() {
        super("Save");
    }
    @Override
    protected void createDialog() {
        addLabel("Save game as");
        name = new TextBox();
        name.setWidth("14em");
        name.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				validate(false);
			}
        });
        addComponent(name);
        
        error = addLabel("Please enter a name.");
        error.setStyleName("n-error");
        error.setVisible(false);
        
        addLabel("Saved games");
    }
    @Override
    public void onListUpdated() {
        if (list.getItemCount() > 0) {
            name.setText(list.getItemText(list.getSelectedIndex()));    
        }
    }
    
    private void validate(final boolean thenSaveGame) {
        if (name.getText() == null || name.getText().length() == 0) {
            error.setText("Please enter a name.");
            error.setVisible(true);
        } else {
            error.setVisible(false);
            if (thenSaveGame) {
				Game.get().getSavedGames(new NamesCallback() {
					public void execute(List<String> names) {
			            if (names.contains(name.getText())) {
			                ConfirmSaveDialog confirm = new ConfirmSaveDialog(name.getText());
			                DialogManager.get().push(confirm);
			            } else {
			                DialogManager.get().popAll();
			                Game.get().saveGame(name.getText());
			            }
					}
				});
            }
        }
    }
    @Override
    public void onPrimaryAction() {
    	validate(true);
    }
    @Override
    public void center() {
        updateSavedGamesList();
        name.setText(Game.get().getPlayer().getName());
        error.setVisible(false);
        super.center();
    }
    @Override
    public void focus() {
        primary.setFocus(true);
    }
}
