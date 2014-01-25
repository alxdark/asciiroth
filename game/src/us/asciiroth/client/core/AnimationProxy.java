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

import com.google.gwt.user.client.Random;

/**
 * A proxy for a piece at a given position, which introduces the state 
 * necessary to track the animation of pieces on the board. 
 *
 */
public class AnimationProxy {
    
    private int x;
    private int y;
    private int frame;
    private Animated piece;
    
    /**
     * Constructor.
     * @param x
     * @param y
     * @param piece
     */
    public AnimationProxy(int x, int y, Animated piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
        this.frame = (piece.randomSeed()) ? Random.nextInt(100) : 0; 
    }
    
    /**
     * Test if this proxy is for a piece instance at the given location.
     * (What if there are multiple instances at the given location?)
     * 
     * @param x         x position of the piece
     * @param y         y position of the piece
     * @param piece     a piece that is animated
     * @return          true if this is the proxy for the piece
     */
    public boolean proxyFor(int x, int y, Object piece) {
        return (this.x == x && this.y == y && this.piece == piece);
    }
    /**
     * Set the X/Y position of this proxy (an optimization so that animated
     * pieces can be moved without destroying and creating a proxy).
     * @param x     the new X position of the animation
     * @param y     the new Y position of the animation
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Execute on animation frame, for the piece at this location.
     * @param ctx
     */
    public void frame(Context ctx) {
        Cell cell = ctx.getBoard().getCellAt(x, y);
        piece.onFrame(ctx, cell, frame);
        frame++;
    }
    @Override
    public int hashCode() {
        int result = piece.hashCode();
        result += (x*100);
        result += y;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AnimationProxy)) {
            return false;
        }
        AnimationProxy that = (AnimationProxy)obj;
        return (that.x == this.x &&
                that.y == this.y &&
                that.piece == this.piece);
    }
}
