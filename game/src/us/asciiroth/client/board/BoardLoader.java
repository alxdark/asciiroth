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
package us.asciiroth.client.board;

import us.asciiroth.client.Util;
import us.asciiroth.client.core.Player;
import us.asciiroth.client.ui.BoardView;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;

public class BoardLoader {
    
    private BoardReader reader;
    
	public BoardLoader(BoardReader reader) {
        this.reader = reader;
	}
    public void load(final BoardView view, final Player player, final Board board, final Command command) {
        try {
            String url = player.getScenarioURL() + player.getBoardID() + ".js";
            RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
            builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable e) {
                    Util.showError(e.getMessage());
                }
                public void onResponseReceived(Request request, Response response) {
                    try {
                        reader.read(view, player, board, response.getText(), command);
                    } catch(Exception e) {
                        // Certainly the most likely possibility is that the distribution was
                        // tampered with, it has been removed at the web site, or it was
                        // loaded locally and then the scenario folder was moved or 
                        // deleted.
                        Util.showError("Couldn't load map. Could it have been moved/deleted?<br/>"+
                                       "This saved game cannot be played further, unfortunately.");
                    }
                }
            });
        } catch(RequestException e) {
            Util.showError(e.getMessage());
        }       
    }
}

