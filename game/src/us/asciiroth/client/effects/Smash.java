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

import static us.asciiroth.client.core.Color.BUILDING_WALL;
import static us.asciiroth.client.core.Color.LESSNEARBLACK;
import static us.asciiroth.client.core.Color.NONE;
import static us.asciiroth.client.core.Color.WHITE;
import static us.asciiroth.client.core.Flags.TRANSIENT;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.event.Events;

/**
 * Something being smashed apart.
 *
 */
public class Smash extends AbstractEffect implements Animated {

    private static final Symbol[] SYMBOLS = {
        new Symbol("&lowast;", WHITE, null, BUILDING_WALL, null),
        new Symbol("%", WHITE, null, BUILDING_WALL, null),
        new Symbol("&permil;", WHITE, null, BUILDING_WALL, null),
        new Symbol("&#4347;", "&there4;", WHITE, null, BUILDING_WALL, null),
        new Symbol("&#4347;", "&there4;", LESSNEARBLACK, null, BUILDING_WALL, null),
    };
    
    public Smash() {
        super("Smash", TRANSIENT, NONE, SYMBOLS[0]);
    }
    public boolean randomSeed() {
    	return false;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame < 5) {
            Events.get().fireRerender(cell, this, SYMBOLS[frame]);
        } else {
            cell.getEffects().remove(this);
        }
    }
}
