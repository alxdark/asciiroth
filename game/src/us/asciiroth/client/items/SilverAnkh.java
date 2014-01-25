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

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.SILVER;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;

/**
 * Just an object, with no special properties. The fallback symbol is kind of 
 * lame. Fallback symbols generally are kind of lame, I wouldn't play the game
 * in IE...
 */
public class SilverAnkh extends AbstractItem {

    public SilverAnkh() {
        // It has to be a little dark than silver to be seen outside..
        super("Silver Ankh", 0, NONE, 
            new Symbol("&#9765;", SILVER, null, BUILDING_WALL, null));
    }
    /** Type serializer. */
    public static final Serializer<SilverAnkh> SERIALIZER = new TypeOnlySerializer<SilverAnkh>() {
        public SilverAnkh create(String[] args) {
            return new SilverAnkh();
        }
        public String getTag() {
            return "Artifacts";
        }
    };
}
