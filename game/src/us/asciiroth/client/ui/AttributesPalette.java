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

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AttributesPalette extends Palette {
    
    private FlexTable attributes;
    
    public AttributesPalette() {
        super();
        attributes = new FlexTable();
        
        attributes.setStyleName("paddingTable");
        attributes.setCellPadding(2);
        setBodyWidget(attributes);
    }
    public void setAttribute(String name, Widget w) {
        int row = attributes.getRowCount();
        attributes.setText(row, 0, name);
        attributes.setWidget(row, 1, w);
    }
    public void setAttributeAlignMiddle(String name, Widget w) {
        int row = attributes.getRowCount();
        attributes.getCellFormatter().setVerticalAlignment(row, 0, HorizontalPanel.ALIGN_MIDDLE);
        attributes.setText(row, 0, name);
        attributes.getCellFormatter().setVerticalAlignment(row, 1, HorizontalPanel.ALIGN_MIDDLE);
        attributes.setWidget(row, 1, w);
    }
}
