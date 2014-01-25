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

/**
 * Base support for serializers that escape/unescape pieces as part of their
 * serialization key.
 *
 * @param <T>
 */
public abstract class BaseSerializer<T extends Piece> implements Serializer<T> {
    
    /**
     * When one piece is referenced by another, one key must be nested 
     * in another. Here's a simple escape mechanism that is still readable
     * in a map file: pipes are converted to carets.
     * 
     * @param piece     the piece to convert into an escaped string
     * @return  the piece key escaped for inclusion in another key
     */
    protected String esc(Piece piece) {
        if (piece == null) {
            return "";
        }
        String key = Registry.get().getKey(piece);
        return key.replaceAll("\\|","^");
    }
    /**
     * Convert escaped keys to unescaped keys (caret character is converted
     * back into a pipe character).
     * 
     * @param key   the key in escaped form
     * @return      the piece represented by this escaped key
     */
    protected Terrain unescTerrain(String key) {
        key = key.replaceAll("\\^","|");
        return (Terrain)Registry.get().getPiece(key);
    }
    /**
     * Unescape an item. 
     * @param key
     * @return  the item represented by this escaped key
     */
    protected Item unescItem(String key) {
        key = key.replaceAll("\\^","|");
        return (Item)Registry.get().getPiece(key);
    }
    /**
     * Unescape a piece. 
     * @param key
     * @return  the item represented by this escaped key
     */
    protected Piece unesc(String key) {
        key = key.replaceAll("\\^","|");
        return Registry.get().getPiece(key);
    }
    /**
     * Does this string represent the value true or false?
     * @param str
     * @return  true if the string is "true", false if it is "false", or else a 
     *          runtime exception.
     */
    protected boolean isTrue(String str) {
        if (str != null) {
            if ("true".equals(str.toLowerCase())) {
                return true;
            } else if ("false".equals(str.toLowerCase())) {
                return false;
            }
        }
        throw new RuntimeException("Boolean not true/false");
    }
    
    /**
     * unescape an agent. 
     * @param key
     * @return  the agent represented by this escaped key
     */
    protected Agent unescAgent(String key) {
        key = key.replaceAll("\\^","|");
        return (Agent)Registry.get().getPiece(key);
    }
}
