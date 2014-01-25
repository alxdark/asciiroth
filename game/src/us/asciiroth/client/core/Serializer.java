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

/**
 * For each piece type there is a serializer that can convert the piece back and
 * forth to a key, and that also provides some metadata about the type so it 
 * can be manipulated in the map editor. Those latter methods are just not included
 * in the compiled code of the game, so they introduce no overhead and are conveniently
 * included as part of the serializer.
 *
 * @param <T>
 */
public interface Serializer<T extends Piece> {
    /**
     * Create a piece. The Registry will cache this instance, returning it any 
     * time a similar piece is need in the game.
     * 
     * @param args an array of strings, originally pipe-delimited, that can be used
     * to create a valid instance of this type. 
     * @return  a piece 
     */
    public T create(String[] args);
    /**
     * Create a string representation of this piece, referred
     * to in the code as a "key". 
     * 
     * @param piece
     * @return  a string key for the supplied piece
     */
    public String store(T piece);
    /**
     * Provide an example of this piece for display in the editor.
     * @return  an example piece to demonstrate the type in the map editor
     */
    public T example();
    /**
     * Provide a template string that can be used by the editor to
     * guide the creation of a valid instance of this type.
     * @param type
     * @return  a key-like template that can be used in the editor
     */
    public String template(String type);
    
    /**
     * A category to file this piece under in the editor.
     * @return string
     */
    public String getTag();
}
