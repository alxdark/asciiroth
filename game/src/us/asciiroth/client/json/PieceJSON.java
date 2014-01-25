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

public class PieceJSON extends JavaScriptObject {

    public static final native PieceJSON parseJSON(String json) /*-{
        return eval('('+json+')');
    }-*/;
    
    protected PieceJSON() {}
    
    public final native int x() /*-{
        return this.x;
    }-*/;
    public final native int y() /*-{
        return this.y;
    }-*/;
    public final native String key() /*-{
        return this.key;
    }-*/;
}
