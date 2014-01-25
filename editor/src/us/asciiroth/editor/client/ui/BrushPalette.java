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
package us.asciiroth.editor.client.ui;

import us.asciiroth.client.ui.AttributesPalette;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;

public class BrushPalette extends AttributesPalette {

    private Label position;
    private int brushSize;
    
    public BrushPalette() {
        super();
        setTitle("Brush Options");
        
        position = new Label();
        
        HorizontalPanel hp = new HorizontalPanel();
        hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        createBrushOption(hp, "1");
        createBrushOption(hp, "2");
        createBrushOption(hp, "5");
        createBrushOption(hp, "10");
        createBrushOption(hp, "20");
        
        setAttribute("X/Y:", position);
        setAttributeAlignMiddle("Brush:", hp);
        
        RootPanel.get("sideViews").add(this);
    }

    private void createBrushOption(HorizontalPanel hp, final String size) {
        final int s = Integer.parseInt(size);
        RadioButton radio = new RadioButton("brushSize");
        radio.setText(size);
        radio.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) { brushSize = s; }
        });
        if ("1".equals(size)) {
        	radio.setValue(Boolean.TRUE);
            brushSize = 1;
        }
        hp.add(radio);
    }
    
    public void setXY(int x, int y) {
        position.setText(Integer.toString(x) +" / "+ Integer.toString(y));
    }
    
    public int getBrushSize() {

        return brushSize;
    }
}
