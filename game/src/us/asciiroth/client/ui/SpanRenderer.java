package us.asciiroth.client.ui;

import com.google.gwt.user.client.Element;

/**
 * Render a span into the table cell using an array join. In FF2, this 
 * removed visual oddities, but it also happens to be very fast in that
 * browser. 
 * 
 * In FF3, caching the array and rewriting the elements that are changing, 
 * is actually substantially slower than this in-line literal form. Wierd, huh?
 *
 */
public class SpanRenderer implements Renderer {
    public native void render(Element td, String entity, String color, String bgcolor) /*-{
        td.innerHTML = ["<span class='s' style='color: ",color,"; background-color: ",
            bgcolor,"'>",entity,"</span>"].join("");
    }-*/;
}
