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
import us.asciiroth.client.board.WriterBase;
import us.asciiroth.client.items.EmptyHanded;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class PlayerWriter extends WriterBase {
    
    public static final String BAG_KEY            = "bag";
    public static final String EMPTY_HANDED_KEY   = "EmptyHanded";
    public static final String FLAGS_KEY          = "flags";
    public static final String HEALTH_KEY         = "health";
    public static final String NAME_KEY           = "name";
    public static final String BASE_KEY           = "base";
    public static final String URL_KEY            = "url";
    public static final String X_KEY              = "x";
    public static final String Y_KEY              = "y";
    
    public String write(Player player, int x, int y) {
        JSONObject b = new JSONObject();
        addString(b, NAME_KEY, player.getName());
        addString(b, BASE_KEY, player.getScenarioURL());
        addString(b, URL_KEY, player.getBoardID());
        addNumber(b, HEALTH_KEY, player.changeHealth(0));
        addNumber(b, X_KEY, x);
        addNumber(b, Y_KEY, y);
        addNumber(b, FLAGS_KEY, player.getFlags());
        
        JSONArray array = new JSONArray();
        int i= 0;
        for (Bag.Entry<Item> entry : player.getBag().asEntryList()) {
            if (!(entry.getPiece() instanceof EmptyHanded)) {
                
                String key = Registry.get().getKey(entry.getPiece());
                // addNumber(bag, key, entry.getCount());
                JSONObject obj = new JSONObject();
                addString(obj, "key", key);
                addNumber(obj, "count", entry.getCount());
                if (player.getBag().isSelected(entry)) {
                    addBoolean(obj, "selected", true);
                }
                array.set(i, obj);
                i++;
            }
        }
        b.put(BAG_KEY, array);
        return b.toString();
    }
}
