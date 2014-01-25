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
package us.asciiroth.client.effects;

import us.asciiroth.client.core.AbstractPiece;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Effect;
import us.asciiroth.client.core.Symbol;

public abstract class AbstractEffect extends AbstractPiece implements Effect {

    public AbstractEffect(String name, int flags, Color color, Symbol symbol) {
        super(name, flags, color, symbol);
    }
    public AbstractEffect(String name, int flags, Symbol symbol) {
        super(name, flags, symbol);
    }
    public boolean isAboveAgent() {
        return false;
    }
}
