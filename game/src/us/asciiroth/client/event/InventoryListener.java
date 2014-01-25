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
package us.asciiroth.client.event;

import us.asciiroth.client.core.PlayerBag;

/**
 * A component that listens for changes to the players inventory.
 *
 */
public interface InventoryListener {
    /**
     * The player's inventory has changed: items have been added or removed,
     * or the selected item has changed.
     * 
     * @param bag   the player's bag of items
     */
    public void onInventoryChanged(PlayerBag bag);
}
