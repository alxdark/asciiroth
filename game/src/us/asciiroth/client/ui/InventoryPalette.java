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

import java.util.List;

import us.asciiroth.client.Util;
import us.asciiroth.client.core.Bag;
import us.asciiroth.client.core.Game;
import us.asciiroth.client.core.Item;
import us.asciiroth.client.core.PlayerBag;
import us.asciiroth.client.event.Events;
import us.asciiroth.client.event.InventoryListener;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Would like to rationalize this so that there's an intervening
 * list view, but that will be challenging.
 *
 */
public class InventoryPalette extends Palette implements InventoryListener {
    private static final int INV_LENGTH = 11;
    private VerticalPanel inventory;
    private FocusPanel focuser;
    
    public InventoryPalette() {
        super();
        setTitle("Inventory");
        inventory = new VerticalPanel();
        inventory.setStyleName("paddingTable");
        inventory.setWidth("100%");

        focuser = new FocusPanel(inventory);
        focuser.addMouseWheelHandler(new MouseWheelHandler() {
			public void onMouseWheel(MouseWheelEvent event) {
                if (!DialogManager.get().isPaused()) {
                    // Necessary for FF3 not to move selection twice...
                    DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
                    if (event.isNorth()) {
                        Game.get().selectUp();
                    } else if (event.isSouth()){
                        Game.get().selectDown();
                    }
                }
			}
        });
        
        setBodyWidget(focuser);
        RootPanel.get("sideViews").add(this);
    }
    public void onInventoryChanged(final PlayerBag bag) {
        inventory.clear();
        // Resetting this widget fixes a rendering bug in FF3
        // where the rounded border on the bottom of the div 
        // is not updated when you add items.
        setBodyWidget(focuser);
        
        // It seems like this should be simpler.
        int selIndex = bag.getIndex(bag.getSelected());
        int total = bag.size();
        int start = selIndex-(INV_LENGTH/2);
        int end = start+INV_LENGTH;
        
        if (end > total) {
            start -= (end-total);
            end = total;
        }
        if (start < 0) {
            end += Math.abs(start);
            start = 0;
        }
        end = (end > total) ? total : end;
        
        List<Bag.Entry<Item>> entries = bag.asEntryList();
        for (int i=start; i < end; i++) {
            final int j = i;
            Bag.Entry<Item> entry = entries.get(i);
            
            HTML html = new HTML( Util.renderSymbolAndLabel2(false, entry) );
            html.setStyleName(bag.isSelected(entry) ? "selectedItem" : "notSelectedItem");
            html.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    if (!DialogManager.get().isPaused()) {
                        if (bag.getSelected() == bag.get(j)) {
                            Events.get().fireClearCurrentCell();
                            Game.get().useSelectedItem();
                            Events.get().fireHandleInventoryMessaging();
                        } else {
                            bag.select(j);
                        }
                    }
                }
            });
            html.addMouseOverHandler(new MouseOverHandler() {
				public void onMouseOver(MouseOverEvent event) {
                    if (!DialogManager.get().isPaused()) {
                    	// WARN
                    	Widget w = (Widget)event.getSource();
                        w.addStyleName("hover");
                    }
				}
            });
            html.addMouseOutHandler(new MouseOutHandler() {
				public void onMouseOut(MouseOutEvent event) {
                    if (!DialogManager.get().isPaused()) {
                    	// WARN
                    	Widget w = (Widget)event.getSource();
                        w.removeStyleName("hover");
                    }
				}
            });
            inventory.add(html);
        }
    }
}
