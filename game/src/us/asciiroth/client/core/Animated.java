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

import us.asciiroth.client.board.Cell;

/**
 * A piece that is animated. The piece receives notification once per
 * frame in the animation timeline, and can move itself, create an 
 * event to be re-rerendered, or perform any other time-based activity.
 *
 */
public interface Animated {
    /**
     * Animation callback, executed once each frame for each piece 
     * animated on the board.
     * @param ctx
     * @param cell
     * @param frame
     */
    public void onFrame(Context ctx, Cell cell, int frame);
    
    /**
     * Should this animation start with a randomly seeded number as 
     * its first frame? For some animations, this can prevent all 
     * presentations of the piece on the board executing the same 
     * animation frame at the same time.
     * @return  true if a random frame should begin the animated piece's timeline
     */
    public boolean randomSeed();
    
}
