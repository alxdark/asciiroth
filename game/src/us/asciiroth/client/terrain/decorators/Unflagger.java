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
package us.asciiroth.client.terrain.decorators;

import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Flags;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Wall;

/**
 * A utility terrain. When it receives a color event, it removes the indicated flag 
 * from the player.
 *
 */
public class Unflagger extends Decorator {

    private final String flagStr;
    private final int flag;

    /**
     * Constructor.
     * @param terrain
     * @param color
     * @param flagStr
     */
    public Unflagger(Terrain terrain, Color color, String flagStr) {
        super(terrain, 0, color, terrain.getSymbol());
        this.flagStr = flagStr;
        this.flag = Flags.getFlag(flagStr);
        if (this.flag == -1) {
            throw new RuntimeException("Flag is not valid");
        }
    }
    public Terrain proxy(Terrain terrain) {
        return new Unflagger(terrain, color, flagStr);
    }
    @Override
    public void onColorEventInternal(Context ctx, Cell cell, Cell origin) {
        ctx.getPlayer().remove(flag);
    }
    /** Type serializer. */
    public static final Serializer<Unflagger> SERIALIZER = new BaseSerializer<Unflagger>() {
        public Unflagger create(String[] args) {
            return new Unflagger(unescTerrain(args[1]), Color.byName(args[2]), args[3]);
        }
        public Unflagger example() {
            return new Unflagger(new Wall(), Color.STEELBLUE, "Poisoned");
        }
        public String store(Unflagger e) {
            return Util.format("Unflagger|{0}|{1}|{2}", esc(e.terrain), e.color.getName(), e.flagStr);
        }
        public String template(String type) {
            return "Unflagger|{terrain}|{color}|{item}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
}
