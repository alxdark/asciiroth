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
import com.google.gwt.user.client.Command;

public class ConfirmDeleteGameDialog extends CenteredPopupPanel {
    
    private final MousyButton deleteButton;
    
    public ConfirmDeleteGameDialog(final GameListingDialog dialog, final String gameName) {
        super();
        addLabel("Really delete this game?");
        deleteButton = addButton("Delete", new ClickHandler() {
            public void onClick(ClickEvent event) {
                Game.get().deleteGame(gameName, new Command() {
                    public void execute() {
                        dialog.updateSavedGamesList();
                        DialogManager.get().pop();
                    }
                });
            }
        });
        addCancelButton();
    }
    @Override
    public void focus() {
        deleteButton.setFocus(true);
    }
}
