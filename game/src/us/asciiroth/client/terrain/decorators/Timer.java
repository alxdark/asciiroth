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
import us.asciiroth.client.core.Animated;
import us.asciiroth.client.core.BaseSerializer;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Context;
import us.asciiroth.client.core.Serializer;
import us.asciiroth.client.core.Terrain;
import us.asciiroth.client.terrain.Wall;

/**
 * A decorator that fires a color event periodically (you can specify
 * how fast in frames). This would be used to open close doors or the 
 * like as part of a puzzle or challenge.
 */
public class Timer extends Decorator implements Animated {

    private final int frames;
    
    public Timer(Terrain terrain, Color color, int frames) {
        super(terrain, 0, color);
        if (frames <= 0) {
            throw new RuntimeException("Ticks must be positive");
        }
        this.frames = frames;
    }
    public void onFrame(Context ctx, Cell cell, int frame) {
        if (frame%frames==0 && frame != 0) {
            ctx.getBoard().fireColorEvent(ctx, color, cell);
        }
    }
    public boolean randomSeed() {
        return false;
    }
    public Terrain proxy(Terrain terrain) {
        return new Timer(terrain, color, frames);
    }

    public static final Serializer<Timer> SERIALIZER = new BaseSerializer<Timer>() {
        public Timer create(String[] args) {
            return new Timer(unescTerrain(args[1]), Color.byName(args[2]), Integer.parseInt(args[3]));
        }
        public Timer example() {
            return new Timer(new Wall(), Color.NONE, 1);
        }
        public String getTag() {
            return "Utility Terrain";
        }
        public String store(Timer t) {
            return Util.format("Timer|{0}|{1}|{2}", 
                esc(t.terrain), t.color.getName(), Integer.toString(t.frames));
        }
        public String template(String type) {
            return "Timer|{terrain}|{color}|{frames}";
        }
    };
}
