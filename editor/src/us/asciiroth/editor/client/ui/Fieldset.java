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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Fieldset extends ComplexPanel {

    public Fieldset(String legendText) {
      setElement(DOM.createFieldSet());
      setStyleName("n-metadata");
      Element legend = DOM.createLegend();
      legend.setInnerText(legendText);
      getElement().appendChild(legend);
    }
    
    public void add(String name, Widget widget) {
        Label label = new Label(name);
        label.setStyleName("n-fieldLabel");
        add(label);
        add(widget);
    }

    @Override
    public void add(Widget w) {
      super.add(w, getElement());
    }

    public void insert(Widget w, int beforeIndex) {
      super.insert(w, getElement(), beforeIndex, true);
    }
  }