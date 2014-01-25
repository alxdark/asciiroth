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

import static us.asciiroth.client.core.Flags.DETECT_HIDDEN;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.terrain.Floor;
import us.asciiroth.client.terrain.Wall;

/**
 * A mimic looks like one kind of terrain, but is entirely another kind
 * of terrain in behavior. Agents are not fooled by the appearance of mimics.
 *
 */
public class Mimic extends Decorator implements TerrainProxy {

    protected final Terrain appearsAs;
    
    /**
     * Constructor
     * @param appearsAs     the terrain this terrain will appear to be
     * @param actual        the terrain this terrain will behave as
     * @param color         the color of this terrain for color events
     */
    Mimic(Terrain appearsAs, Terrain actual, Color color) {
        super(actual, 0, color);
        this.appearsAs = appearsAs;
    }
    /**
     * Creates a proxy where the supplied terrain replaces the terrain
     * the mimic appears as (it remains the same actual terrain).
     */
    public Terrain proxy(Terrain terrain) {
        return new Mimic(terrain, this.terrain, color);
    }
    @Override
    public Terrain getProxiedTerrain() {
        return appearsAs;
    }
    @Override
    public Symbol getSymbol() {
        return appearsAs.getSymbol();
    }
    @Override
    public void onAdjacentTo(Context context, Cell cell) {
        if (context.getPlayer().is(DETECT_HIDDEN)) {
            Events.get().fireRerender(cell, this, terrain.getSymbol());
        }
    }
    @Override
    public void onNotAdjacentTo(Context context, Cell cell) {
        Events.get().fireRerender(cell, this, appearsAs.getSymbol());
    }
    /** Type serializer. */
    public static final Serializer<Mimic> SERIALIZER = new BaseSerializer<Mimic>() {
        public Mimic create(String[] args) {
            Terrain appearsAs = unescTerrain(args[1]);
            Terrain actual = unescTerrain(args[2]);
            return new Mimic(appearsAs, actual, Color.byName(args[3]));
        }
        public Mimic example() {
            return new Mimic(new Wall(), new Floor(), Color.STEELBLUE);
        }
        public String store(Mimic m) {
            String t1 = esc(m.appearsAs);
            String t2 = esc(m.terrain);
            return Util.format("Mimic|{0}|{1}|{2}", t1, t2, m.color.getName());
        }
        public String template(String type) {
            return "Mimic|{appearsAsTerrain}|{actualTerrain}|{color}";
        }
        public String getTag() {
            return "Utility Terrain";
        }
    };
    
}
