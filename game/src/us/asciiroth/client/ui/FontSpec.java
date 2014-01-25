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

import us.asciiroth.client.Registry;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * Display almost all the entities used in the game (pieces that are 
 * animated may use entities that cannot be seen by calling getSymbol()).
 * Used to identify font issues in specific browsers to fallbacks can
 * be added to the proper fallback specification.
 */
public class FontSpec extends CenteredPopupPanel {
    
    private ScrollPanel spanel;

    /**
     * Constructor.
     */
    public FontSpec() {
        super();
        setStyleName("n-dialog n-scrollDialog");
        setWidth("25em");
        spanel = new ScrollPanel();
        spanel.setStyleName("scroller");
        spanel.setHeight("19em");
        addComponent(spanel);
        addCloseButton();
    }
    @Override
    public void focus() {}
    
    @Override
    public void center() {
        super.center();
        spanel.add(new HTML(Registry.get().getEntitySpec()));
    }
}
