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

import us.asciiroth.client.Util;

/**
 * A serializer base that will work for any piece that is defined only
 * by its type. Subclasses need only implement the create method and 
 * return a new instance of the type. 
 *
 * @param <T>
 */
public abstract class TypeOnlySerializer<T extends Piece> implements Serializer<T> {
    public String store(T piece) {
        return Util.getType(piece);
    }
    public T example() {
        return create(null);
    }
    public String template(String key) {
        return key;
    }
}
