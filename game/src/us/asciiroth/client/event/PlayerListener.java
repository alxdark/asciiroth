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

import us.asciiroth.client.core.Player;

/**
 * A component that receives notification of player updates, such as changes 
 * in health or name, but not in flags. There's a separate listener for flag-related 
 * changes.
 *
 */
public interface PlayerListener {
    /**
     * Player has been updated.
     * @param player
     */
    public void onPlayerChanged(Player player);
}
