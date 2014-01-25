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
package us.asciiroth.client.agents;

import static us.asciiroth.client.core.Color.NONE;
import us.asciiroth.client.core.CombatStats;
import us.asciiroth.client.core.Symbol;

/**
 * An agent that cannot be moved and that is probably not animate.
 *
 */
public abstract class ImmobileAgent extends AbstractAgent {

    /**
     * Constructor.
     * @param name
     * @param flags
     * @param symbol
     */
    public ImmobileAgent(String name, int flags, Symbol symbol) {
        super(name, flags, NONE, CombatStats.INDESTRUCTIBLE, symbol);
    }
    /**
     * Cannot be destroyed by default.
     */
    @Override
    public int changeHealth(int value) {
        return 100;
    }
}
