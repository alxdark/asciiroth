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
import us.asciiroth.client.core.Player;
import us.asciiroth.client.store.NamesCallback;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Base class for load and save game dialogs, both of which allow you 
 * to browse saved games and delete them.
 */
public abstract class GameListingDialog extends CenteredPopupPanel {

    protected final ListBox list;
    protected final Button primary;

    public GameListingDialog(String primaryAction) {
        super();
        createDialog();
        list = new ListBox(false);
        list.setVisibleItemCount(5);
        list.setWidth("14em");
        list.setHeight("12em");
        list.addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) { onListUpdated(); }
        });
        list.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) { onListUpdated(); }
        });
        addComponent(list);
        updateSavedGamesList();
        primary = addButton(primaryAction, new ClickHandler() {
            public void onClick(ClickEvent event) { onPrimaryAction(); }
        });
        addButton("Delete", new ClickHandler() {
            public void onClick(ClickEvent event) { deleteGame(); }
        });
        addCancelButton();
    }
    
    protected abstract void createDialog();
    
    public void onListUpdated() {};
    
    public void onPrimaryAction() {};
    
    protected void deleteGame() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            ConfirmDeleteGameDialog confirmDelete = 
                new ConfirmDeleteGameDialog(this, list.getItemText(index));
            DialogManager.get().push(confirmDelete);
        }
    }
    protected void updateSavedGamesList() {
        final Player player = Game.get().getPlayer();
        list.clear();
        
        Game.get().getSavedGames(new NamesCallback() {
			public void execute(List<String> games) {
		        for (int i=0; i < games.size(); i++) {
		            String game = games.get(i);
		            list.addItem(game);
		            if (player != null && player.getName().equals(game)) {
		                list.setItemSelected(i, true);
		            }
		        }
		        // Don't try and set to 0 unless there's at least one game.
		        if (list.getSelectedIndex() == -1 && !games.isEmpty()) {
		            list.setItemSelected(0, true);
		        }
		        onListUpdated();
			}
        });
    }


}