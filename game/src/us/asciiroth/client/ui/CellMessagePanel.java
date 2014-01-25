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
package us.asciiroth.client.ui;

import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Element;

public class CellMessagePanel extends CellPanel {
    
    private double timeout;
    private MessageList messages;
    private Element td;
    
    private int tdLeft;
    private int tdTop;
    private int tdHeight;
    private int tdWidth;
    
    public CellMessagePanel() {
        super("n-message", false, false);
        this.messages = new MessageList();
    }
    public Element getTd() {
        return td;
    }
    public void setTd(Element element) {
        this.td = element;
        tdLeft = element.getAbsoluteLeft();
        tdTop = element.getAbsoluteTop();
        tdHeight = element.getOffsetHeight();
        tdWidth = element.getOffsetWidth();
    }
    public int getTdLeft() {
        return tdLeft;
    }
    public int getTdTop() {
        return tdTop;
    }
    public int getTdHeight() {
        return tdHeight;
    }
    public int getTdWidth() {
        return tdWidth;
    }
    public boolean isInUse() {
        return (td != null);
    }
    public void clearMessages() {
        messages.clear();
    }
    public void addMessage(String message) {
        messages.add(message);
        String s = messages.toBreakList();
        label.setHTML(s);
        timeout = (Duration.currentTimeMillis() + (s.length()*40.0));
    }
    /**
     * The length of time a message shows gets longer as the length of the 
     * message increases. Although the message manager is on a timer, so there's
     * a resolution of about 500 ms to this. Still, it seems to work okay.
     * @return  the number of milliseconds (at a minimum) the message should 
     *          be shown before it is automatically hidden. Moving or interacting
     *          with the board can cause the message to dismiss sooner. 
     */
    public double getTimeout() {
        return timeout;
    }
    @Override
    public void hide() {
        this.td = null;
        label.setHTML("");
        messages.clear();
        super.hide();
    }
    public void display(final int left, final int top) {
        setPopupPositionAndShow(new PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                setPopupPosition(left, top);
            }
        });
    }
    public boolean overlaps(CellMessagePanel peer) {
        int peerX = peer.getAbsoluteLeft();
        int peerY = peer.getAbsoluteTop();
        int peerWidth = peer.getOffsetWidth()+peerX;
        int peerHeight = peer.getOffsetHeight()+peerY;
        return overlaps(peerX, peerY, peerWidth, peerHeight);
    }
    public boolean overlaps(Element td) {
        int peerX = td.getAbsoluteLeft();
        int peerY = td.getAbsoluteTop();
        int peerWidth = td.getOffsetWidth()+peerX;
        int peerHeight = td.getOffsetHeight()+peerY;
        return overlaps(peerX, peerY, peerWidth, peerHeight);
    }
    private boolean overlaps(int peerLeft, int peerTop, int peerRight, int peerBottom) {
        int left = getAbsoluteLeft();
        int top = getAbsoluteTop();
        int right = left+getOffsetWidth();
        int bottom = top+getOffsetHeight();
        
        // The reverse of tests that verify the rectangles do NOT overlap
        return !(bottom < peerTop || top > peerBottom || right < peerLeft || left > peerRight);
    }
}
