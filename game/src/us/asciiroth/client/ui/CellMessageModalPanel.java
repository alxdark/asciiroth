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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;

/**
 * This is a slightly more aggressive form of message panel that
 * appears next to the player, and shows a message that the player
 * must manually dismiss, so he or she can't miss it. This is used
 * to show inline messages from triggers, for example.
 *
 */
public class CellMessageModalPanel extends CellPanel {
    
    private Element td;
    
    public CellMessageModalPanel() {
        super("n-messageModal", false, true);
        setAnimationEnabled(true);
    }
    public void setMessage(Element td, String message) {
        this.td = td;
        this.label.setHTML(message);
    }
    
    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        if (preview.getTypeInt() == Event.ONKEYDOWN) {
            int charCode = preview.getNativeEvent().getKeyCode();
            if (charCode == InputManager.KEY_ESC || 
                charCode == InputManager.KEY_RETURN ||
                charCode == InputManager.KEY_NUM_5) {
                DialogManager.get().pop();
            }
        }
        // Although we're calling cancel here, the event still ultimately
        // leads to the main menu showing...
        preview.cancel();
    }
    
    @Override
    public void center() {
        setPopupPositionAndShow(new PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = td.getAbsoluteLeft() + td.getOffsetWidth() + 6; 
                int top = td.getAbsoluteTop();
                
                if (left < 5) { // It gets moved back, right on top of player.
                    left = 5;
                    top -= offsetHeight;
                }
                if (left+offsetWidth > Window.getClientWidth()) {
                    left -= ((left+offsetWidth)-Window.getClientWidth()+5);
                }
                if (top+offsetHeight > Window.getClientHeight()) {
                    top -= ((top+offsetHeight)-Window.getClientHeight()+5);
                }
                setPopupPosition(left, top);
            }
        });
    }
}
