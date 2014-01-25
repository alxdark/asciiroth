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

public class BoardJSON extends JavaScriptObject {

    public static final native BoardJSON parseJSON(String json) /*-{
        return eval('('+json+')');
    }-*/;
    
    protected BoardJSON() {}
    
    public final native boolean outside() /*-{
        return this.outside;
    }-*/;
    public final native int startX() /*-{
        return this.startX;
    }-*/;
    public final native int startY() /*-{
        return this.startY;
    }-*/;
    public final native String adjacentBoard(String dir) /*-{
        return this[dir];
    }-*/;
    public final native String startInventory() /*-{
        return this.startInv;
    }-*/;
    public final native String music() /*-{
        return this.music;
    }-*/;
    public final native JsArray<String> diagram() /*-{
        return this.diagram;
    }-*/;
    public final native JsArray<PieceJSON> pieces() /*-{
        return this.pieces;
    }-*/;
    public final native String scenarioName() /*-{
        return this.name;
    }-*/;
    public final native String creator() /*-{
        return this.creator;
    }-*/;
    public final native String description() /*-{
        return this.description;
    }-*/;
    public final native String legend(String token) /*-{
        return this.terrain[token];
    }-*/;
}
