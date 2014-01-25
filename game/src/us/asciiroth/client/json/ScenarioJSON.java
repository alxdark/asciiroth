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
package us.asciiroth.client.json;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * ScenarioJSON metadata, retrieved either from the scenarios array (a listing
 * of games loaded at the start of the game), or from an individual scenario
 * file in a scenario folder, when the game is loaded via the AIR version of
 * the application. It's just the information necessary to describe the 
 * scenario to the user and to load it.
 */
public class ScenarioJSON extends JavaScriptObject {
    
    public static final native ScenarioJSON getScenario(String json) /*-{
        return eval('('+json+')');
    }-*/;
    
    public static final native JsArray<ScenarioJSON> getScenarios() /*-{
        return $wnd.scenarios;
    }-*/;
    
    protected ScenarioJSON() {}
    
    public final native String name() /*-{
        return this.name;
    }-*/;
    public final native String url() /*-{
        return this.url;
    }-*/;
    public final native void setUrl(String url) /*-{
        this.url = url;
    }-*/;
    public final native String creator() /*-{
        return this.creator;
    }-*/;
    public final native void setCreator(String creator) /*-{
        this.creator = creator;
    }-*/;
    public final native String description() /*-{
        return this.description;
    }-*/;
    public final native String setDescription(String description) /*-{
        return this.description = description;
    }-*/;
    public final native int version() /*-{
        return this.version || 1;
    }-*/;
}
