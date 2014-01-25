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

public class LoadGameDialog extends GameListingDialog {

    public LoadGameDialog() {
        super("Play");
    }
    @Override
    protected void createDialog() {
        addLabel("Select a game to load");
    }
    @Override
    public void onPrimaryAction() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            DialogManager.get().popAll();
            Game.get().loadGame(list.getItemText(index));
        }
    }
    @Override
    public void center() {
        updateSavedGamesList();
        super.center();
    }
    @Override
    public void focus() {
        list.setFocus(true);
    }
}
