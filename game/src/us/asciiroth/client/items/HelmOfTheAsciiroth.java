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
import static us.asciiroth.client.core.Color.BLACK;
import static us.asciiroth.client.core.Color.WHITE;
import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.board.Cell;
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Direction;
import us.asciiroth.client.core.Event;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TypeOnlySerializer;
import us.asciiroth.client.event.Events;

/**
 * An artifact (to me, this means there should only ever be one of these in 
 * any given scenario). It has the curious property that when used, it shoots
 * stonifying bullets out in all directions (at a small cost in health to the
 * player).
 *
 */
public class HelmOfTheAsciiroth extends AbstractItem implements Animated {
    
    private final Stoneray stoneray;
    
    /** Constructor. */
    public HelmOfTheAsciiroth() {
        super("Helm of the Asciiroth", 0, new Symbol("&Omega;", WHITE, null, BLACK, null));
        this.stoneray = (Stoneray)Registry.get().getPiece(Stoneray.class);
    }
    @Override
    public void onUse(Event event) {
        for (Direction dir : Direction.getAdjacentDirections()) {
            Event e = Game.get().createEvent();
            Game.get().shoot(e, e.getBoard().getCurrentCell(), e.getPlayer(), stoneray, dir);    
        }
        Game.get().damage(event.getBoard().getCurrentCell(), event.getPlayer(), 10);
    }
    public boolean randomSeed() {
    	return true;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        boolean outside = ctx.getBoard().isOutside();
        Color bg = cell.getTerrain().getSymbol().getBackground(outside);
        Color nbg = Util.oscillate(bg, Color.GREEN, 10, frame);
        Symbol s = new Symbol(symbol.getAdjustedEntity(), symbol.getColor(false), nbg, symbol.getColor(true), nbg);
        Events.get().fireRerender(cell, this, s);
    }
    @Override
    public String getDefiniteNoun(String phrase) {
        return Util.format(phrase, "the " + name);
    }
    @Override
    public String getIndefiniteNoun(String phrase) {
        return Util.format(phrase, "the " + name);
    }
    /** Type serializer. */
    public static final Serializer<HelmOfTheAsciiroth> SERIALIZER = new TypeOnlySerializer<HelmOfTheAsciiroth>() {
        public HelmOfTheAsciiroth create(String[] args) {
            return new HelmOfTheAsciiroth();
        }
        public String getTag() { return "Artifacts"; }
    };
}
