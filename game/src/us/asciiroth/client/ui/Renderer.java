package us.asciiroth.client.ui;

import com.google.gwt.user.client.Element;

/**
 * A browser-specific way of rendering a cell; the performance of different approaches
 * varies between browsers.
 *
 */
public interface Renderer {
    public void render(Element td, String entity, String color, String bgcolor);
}
