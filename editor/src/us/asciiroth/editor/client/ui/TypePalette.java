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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.client.ui.Palette;
import us.asciiroth.editor.client.Editor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TypePalette extends Palette {

    private static final String TYPE_PALETTE_FORMAT = 
        "<span class='{0}' style='color: {1}; background-color: {2}'>{3}</span>&emsp;{4}";
    
    private HTML selectedType;
    private Map<Widget, String> widgetToTemplate;
    private Map<String, DisclosurePanel> tagToDpanel;
    
    private ClickHandler handler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (selectedType != null) {
                selectedType.setStyleName("notSelectedItem");
            }
            // selectedType = (HTML)sender;
            // WARN: May not be correct;
            selectedType = (HTML)event.getSource();
            selectedType.setStyleName("selectedItem");
            
            String template = (String)widgetToTemplate.get(selectedType);
            Editor.get().getInstancePalette().createInstance(template);
        }
    };
    
    public TypePalette() {
        super();
        setTitle("Piece Library");
        
        ScrollPanel panel = new ScrollPanel();
        panel.setStyleName("n-typePalette");
        panel.setWidth("100%");
        panel.setHeight("326px");
        
        VerticalPanel outerVpanel = new VerticalPanel();
        outerVpanel.setWidth("100%");
        panel.add(outerVpanel);

        widgetToTemplate = new HashMap<Widget, String>();
        tagToDpanel = new HashMap<String, DisclosurePanel>();

        for (Map.Entry<String, Map<String, String>> entry : 
             Registry.get().getSerializersByTags().entrySet()) {
            
            String tag = entry.getKey();
            List<String> types = getSortedListOfTypes(entry.getValue());
            for (String type : types) {
                String template = entry.getValue().get(type);
                HTML paletteItem = createPaletteItem(type, template);
                DisclosurePanel dpanel = (DisclosurePanel)tagToDpanel.get(tag);
                if (dpanel == null) {
                    dpanel = new DisclosurePanel(tag, false);
                    VerticalPanel vpanel = new VerticalPanel();
                    vpanel.setWidth("100%");
                    vpanel.setHeight("100%");
                    dpanel.setContent(vpanel);
                    outerVpanel.add(dpanel);
                    tagToDpanel.put(tag, dpanel);
                }
                ((VerticalPanel)dpanel.getContent()).add(paletteItem);
            }
        }
        setBodyWidget(panel);
        RootPanel.get("sideViews").add(this);
    }
    
    private HTML createPaletteItem(String type, String template) {
        Piece piece = Registry.get().getExample(type);
        String descr = renderSymbolForTypePalette(piece);
        
        HTML paletteItem = new HTML(descr);
        paletteItem.setStyleName("notSelectedItem");
        paletteItem.addClickHandler(handler);
        widgetToTemplate.put(paletteItem, template);
        return paletteItem;
    }

    private List<String> getSortedListOfTypes(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        return keys;
    }
    
    public String renderSymbolForTypePalette(Piece piece) {
        Symbol symbol = piece.getSymbol();
        String color = (symbol.getColor(false) != null) ? symbol.getColor(false).toString() : "inherit";
        String bgcolor = (symbol.getBackground(false) != null) ? symbol.getBackground(false).toString() : "inherit";
        return Util.format(TYPE_PALETTE_FORMAT,
            (piece instanceof TerrainProxy) ? "sc" : "s",
            color, bgcolor, symbol.getAdjustedEntity(), Util.getType(piece) );
    }
    
}
