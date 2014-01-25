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

import java.util.List;

import us.asciiroth.client.event.Events;
import us.asciiroth.client.event.GameListener;

import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Timer;

/**
 * The class that executes the animation timeline, using an AnimationProxy 
 * to bridge between the immutable nature of game pieces (all instances of 
 * a piece on the board are the same piece) and their occurrence at specific 
 * places and at different moments on the animation timeline (each instance 
 * has unique animation-based state in the proxy). 
 *
 */
public class AnimationManager extends Timer implements GameListener {

    private double now;
    private double timePassed;
    private double lastTime;
    // after speeding up the game, this turned out to be a little much
    // private long period = 70;
    private double period = 80.0;
    
    /** Constructor. */
    public AnimationManager() {
        this.lastTime = Duration.currentTimeMillis();
    }
    /**
     * The game has been paused. Pauses animation.
     */
    public void onGamePaused() {
        cancel();
    }
    /**
     * The game has been resumed. Resumes animation.
     */
    public void onGameResumed() {
        scheduleRepeating(5);
    }
    /**
     * Main animation loop.
     */
    @Override
    public void run() {
        Event evt = Game.get().createEvent();
        if (evt.getBoard() != null) {
            // We're polling faster than the frame rate and using real time 
            // to measure fps. There's probably better ways, but I don't see
            // how to reset a timer's interval after it has been started.
            now = Duration.currentTimeMillis();
            timePassed = (now - lastTime);
            if (timePassed >= period) {
                lastTime = now;
                List<AnimationProxy> animated = evt.getBoard().getAnimated();
                // Walk backwards, as animations can remove themselves from the board.
                for (int i = (animated.size()-1); i >= 0; i--) {
                    animated.get(i).frame(evt);
                }
                Events.get().fireHandleModalMessage();
            }
        }
    }
}
