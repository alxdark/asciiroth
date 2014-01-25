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

import us.asciiroth.client.Util;
import us.asciiroth.client.core.Game;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * A reusable dialog that loads an HTML URL into a scroll panel. Used at 
 * least for help and about dialogs, and might be used elsewhere in the
 * game as well.
 */
public class HTMLDialog extends CenteredPopupPanel {
    
    public static final String DEFAULT_WIDTH = "22em";
    
    private ScrollPanel spanel;
    private String url;
    private MousyButton closeButton;
    
    public HTMLDialog() {
        super();
        setStyleName("n-dialog n-scrollDialog");
        setWidth(DEFAULT_WIDTH);
        spanel = new ScrollPanel();
        spanel.setStyleName("scroller");
        spanel.setHeight("19em");
        addComponent(spanel);
        
        closeButton = addCloseButton();
    }
    public void setHTML(String url) {
        this.url = url;
    }
    @Override
    public void center() {
        try {
            RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
            builder.sendRequest(null, new DialogRequestCallback(this));
        } catch(RequestException e) {
            Util.showError(e.getMessage());
        }       
    }
    private void displayInternal() {
        setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = (Window.getClientWidth() - offsetWidth) / 2;
                int top = (Game.get().getHeightOfGameScreen() - offsetHeight) >> 1;
                setPopupPosition(left, top);
            }
        });
    }
    private class DialogRequestCallback implements RequestCallback {
        private HTMLDialog dialog;
        public DialogRequestCallback(HTMLDialog dialog) {
            this.dialog = dialog;
        }
        public void onError(Request request, Throwable e) {
            Util.showError(e.getMessage());
        }
        public void onResponseReceived(Request request, Response response) {
            dialog.spanel.clear();
            dialog.spanel.add(new HTML(response.getText()));
            displayInternal();
        }
    }
    @Override
    public void focus() {
        closeButton.setFocus(true);
    }
}
