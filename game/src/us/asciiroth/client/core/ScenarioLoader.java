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
package us.asciiroth.client.core;

import us.asciiroth.client.Util;
import us.asciiroth.client.json.ScenarioJSON;
import us.asciiroth.client.ui.NewGameDialog;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class ScenarioLoader {
    
    public void load(final String filePath, final NewGameDialog dialog) {
        try {
            final String scenarioURL = ("file:///" + filePath + "/").replaceAll("\\\\", "/");
            final String url = scenarioURL + "start.js";
            RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
            builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable e) {
                    Util.showError(e.getMessage());
                }
                public void onResponseReceived(Request request, Response response) {
                    try {
                        ScenarioJSON scenario = ScenarioJSON.getScenario(response.getText());
                        scenario.setUrl(scenarioURL);
                        dialog.showLoadedScenario(scenario);
                    } catch(Exception e) {
                        Util.showError("Couldn't load scenario. You're looking for an <br/>"+
                                       "uncompressed folder that contains a <code>start.js</code> file.");
                    }
                }
            });
        } catch(RequestException e) {
            Util.showError(e.getMessage());
        }       
    }
}

