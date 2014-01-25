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
package us.asciiroth.editor.client;

import us.asciiroth.client.Registry;
import us.asciiroth.client.Util;
import us.asciiroth.client.core.Color;
import us.asciiroth.client.core.Piece;
import us.asciiroth.client.core.Symbol;
import us.asciiroth.client.core.TerrainProxy;
import us.asciiroth.editor.client.ui.InstancePalette;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;

/**
 * An editor associated to a piece instance on the board, or available for addition to the board.
 *
 */
public class InstanceEditor extends Composite implements ClickHandler, FocusHandler, KeyUpHandler {

    private static final String EMPTY = "<div style='font-family: monospace'>&emsp;</div>";
    
    private static final String INSTANCE_PALETTE_FORMAT = 
        "<span class='{0}' style='color: {1}; background-color: {2}'>{3}</span>";
    
    private String template;
    private Piece piece;
    // This is the piece as it was last used on the board; when the editor is updated,
    // we'll take the current piece and replace every instance of the lastPainted piece
    // we find on the board.
    private Piece lastPainted;
    private HTML symbol;
    private HTML color;
    private Image status;
    private InstancePalette palette;
    private FocusPanel panel;
    private TextArea field;
    
    /**
     * Constructor.
     * @param palette
     * @param template
     */
    public InstanceEditor(final InstancePalette palette, String template) {
        this.palette = palette;
        this.template = template;
        panel = new FocusPanel();
        panel.setStyleName("n-instanceEditor");
        panel.addClickHandler(this);
        createPanelContents(template);
        update();
        initWidget(panel);
    }
    
    private void createPanelContents(String template) {
        Grid grid = new Grid(1, 5);
        grid.setWidth("100%");
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.getCellFormatter().setWidth(0, 0, "25px");
        grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setWidth(0, 1, "25px");
        grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setWidth(0, 2, "25px");
        grid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
        grid.getCellFormatter().setWidth(0, 4, "120px");
        grid.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);
        HorizontalPanel hpanel = new HorizontalPanel();
        hpanel.setWidth("100%");
        hpanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        hpanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        // panel.add(hpanel);
        panel.add(grid);

        // The symbol itself
        symbol = new HTML(EMPTY);
        // hpanel.add(symbol);
        grid.setWidget(0, 0, symbol);
        
        color = new HTML(EMPTY);
        // hpanel.add(color);
        grid.setWidget(0, 1, color);
        
        // status
        status = new Image("icons/bullet_delete.png");
        // hpanel.add(status);
        grid.setWidget(0, 2, status);
        
        field = new TextArea();
        field.setWidth("400px");
        field.setVisibleLines(1);
        field.setText(template);
        field.addFocusHandler(this);
        field.addKeyUpHandler(this);
        // hpanel.add(field);
        grid.setWidget(0, 3, field);
        
        final InstanceEditor that = this;
        Hyperlink hyperlink = new Hyperlink("Remove", "remove");
        hyperlink.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
                BoardEditor boardEditor = Editor.get().getBoardEditor();
                boardEditor.removePiece(piece);
                Editor.get().getInstancePalette().remove(that);
        	}
        });
        // hpanel.add(hyperlink);
        grid.setWidget(0, 4, hyperlink);
    }
    
    private boolean adjusted;
    
    private void adjustHeight() {
        int h = field.getElement().getPropertyInt("scrollHeight");
        
        // Log.get().debug(Integer.toString(h));
        if (h > 0) {
            if (!adjusted) {
                h+=2;
            }
            field.setHeight(Integer.toString(h)+"px");    
        }
    }
    
    public void update() {
        String key = field.getText();
        if (key == null || key.length() == 0) {
            return;
        }
        adjustHeight();
        try {
            piece = Registry.get().getPiece(key);
            if (Registry.get().getKey(piece).matches(".*[\\}\\{].*")) {
                throw new RuntimeException("Not all parameters replaced");
            }

            status.setUrl("icons/bullet_green.png");
            symbol.setHTML(renderSymbolForInstanceEditor(piece));
            if (piece.getColor() != Color.NONE) {
                color.setHTML("<div style='font-family: monospace; width:1em; background-color: "+(piece.getColor().toString())+"'>&emsp;</div>");    
            }
            
            // One serious issue here is if the piece changes type completely... that's
            // not workable. So we don't update at that point.
            if (lastPainted != null && Util.getType(lastPainted) == Util.getType(piece)) {
                BoardEditor boardEditor = Editor.get().getBoardEditor();
                boardEditor.updatePiece(lastPainted, piece);
                
            }
            lastPainted = piece;
        } catch(Exception e) {
            symbol.setHTML(EMPTY);
            color.setHTML(EMPTY);
            status.setUrl("icons/bullet_delete.png");
        }
    }
    public void selected(boolean selected) {
        panel.setStyleName(selected ? "n-instanceEditorSelected" : "n-instanceEditor");
        if (!selected) {
            field.setHeight("20px");
        }
    }
    public Piece getPiece() {
        return piece;
    }
    public String getTemplate() {
        return template;
    }
    public String getCurrentValue() {
        return field.getText();
    }
    public void setLastPaintedPiece() {
        this.lastPainted = piece;
    }
    public void onClick(ClickEvent event) {
        palette.setSelectedEditor(this);
        field.setFocus(true);
    }
    public void onFocus(FocusEvent event) {
        palette.setSelectedEditor(this);
        field.setFocus(true);
        adjustHeight();
    }
    public void onKeyUp(KeyUpEvent event) {
        update();
    }
    
    /**
     * Render the symbol for a piece. This would be the fourth way this happens, always in a span, too.
     * @param piece
     * @return  an HTML string rendering the terrain with a frame if it has been augmented
     */
    private String renderSymbolForInstanceEditor(Piece piece) {
        Symbol symbol = piece.getSymbol();
        String color = (symbol.getColor(false) != null) ? symbol.getColor(false).toString() : "inherit";
        String bgcolor = (symbol.getBackground(false) != null) ? symbol.getBackground(false).toString() : "inherit";
        return Util.format(INSTANCE_PALETTE_FORMAT,
            (piece instanceof TerrainProxy) ? "sc" : "s",
            color, bgcolor, symbol.getAdjustedEntity());
    }
    
    
}
