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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * One of those little panels on the right-hand side. There are different palettes 
 * in the game and the map editor, although the general layout of the applications 
 * are the same. 
 *
 */
public class Palette extends Composite {

    private FlowPanel title;
    private FlowPanel content;
    
    /** Constructor. */
    public Palette() {
        FlowPanel panel = new FlowPanel();
        panel.setStyleName("n-sideView");
        title = new FlowPanel();
        title.setStyleName("title");
        panel.add(title);
        content = new FlowPanel();
        content.setStyleName("content");
        panel.add(content);
        initWidget(panel);
    }
    @Override
    public void setTitle(String titleStr) {
        title.clear();
        title.add(new Label(titleStr));
    }
    /**
     * Set the widget that will occupy the body of the palette, under its title.
     * @param w
     */
    public void setBodyWidget(Widget w) {
        content.clear();
        content.add(w);
    }
}
