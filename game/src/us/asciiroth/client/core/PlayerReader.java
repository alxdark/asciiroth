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
package us.asciiroth.client.core;

import us.asciiroth.client.Registry;
import us.asciiroth.client.json.BagEntryJSON;
import us.asciiroth.client.json.JsArray;
import us.asciiroth.client.json.PlayerJSON;
import us.asciiroth.client.ui.InputManager;

public class PlayerReader {
    
    public Player read(InputManager im, String json) {
        PlayerJSON pjson = PlayerJSON.parseJSON(json);
        Player player = new Player(im, pjson.name(), pjson.base(), pjson.url(), pjson.x(), pjson.y());
        player.setHealth(pjson.health());
        player.add(pjson.flags());
        addBagItems(player, pjson);
        return player;
    }
    private void addBagItems(Player player, PlayerJSON pjson) {
        JsArray<BagEntryJSON> array = pjson.bag();
        for (int i=0; i < array.size(); i++) {
            BagEntryJSON entry = array.get(i);
            Item item = (Item)Registry.get().getPiece(entry.key());
            int count = entry.count();
            for (int j=0; j < count; j++) {
                player.getBag().add(item);
            }
            if (entry.selected()) {
                player.getBag().setInitialSelection(i+1);
            }
        }
    }
}
