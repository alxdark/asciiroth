package us.asciiroth.client.ui;

import com.google.gwt.user.client.Element;

/**
 * Render to the table cell using the DOM. IE and Safari both perform better with 
 * this approach, but visually it doesn't work in Firefox 2, you see the updates
 * independently of one another and their are wierd artifacts as a result.
 *
 */
public class DomRenderer implements Renderer {
    public native void render(Element td, String entity, String color, String bgcolor) /*-{
        td.innerHTML = entity;
        td.style.color = color;
        td.style.backgroundColor = bgcolor;
    }-*/;
}
