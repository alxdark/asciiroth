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
package us.asciiroth.client.items;

import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.core.Symbol;

/**
 * An item that represents that the player is not holding anything 
 * in particular. Null object pattern. 
 */
public class EmptyHanded extends AbstractItem {
    /** Constructor. */
    public EmptyHanded() {
        super("<i>Empty-Handed</i>", 0, new Symbol("&emsp;", WHITE));
    }
}
