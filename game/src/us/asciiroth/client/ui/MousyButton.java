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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;

public class MousyButton extends Button {

    public MousyButton(String width, String html) {
        super(html.replaceAll(" ", "&#160;"));
        setStyleName("n-button");
        setWidth(width);
        sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }
    public MousyButton(String width, String html, ClickHandler handler) {
        super(html.replaceAll(" ", "&#160;"), handler);
        setStyleName("n-button");
        setWidth(width);
        sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }
    @Override
    public void onBrowserEvent(Event event) {
        switch(event.getTypeInt()) {
        case Event.ONMOUSEOVER:
            addStyleName("n-hover");
            break;
        case Event.ONMOUSEOUT:
            removeStyleName("n-hover");
            break;
        case Event.ONCLICK:
            removeStyleName("n-hover");
            break;
        }
        super.onBrowserEvent(event);
    }
    @Override
    public void setHTML(String html) {
        super.setHTML(html.replaceAll(" ", "&#160;"));
    }
}
