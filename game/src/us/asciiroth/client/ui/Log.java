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

import com.google.gwt.core.client.GWT;

/** Simple logging class. */
public class Log {

    private static Log instance = new Log();
    public static Log get() {
        return instance;
    }
	
	public void debug(String... message) {
	    // dispensing with StringBuilder, I don't think it really matters
	    String msg = "";
	    for (int i=0, len = message.length; i < len; i++) {
	        msg += ((i > 0) ? " " : "") + message[i];
	    }
        if (!GWT.isScript()) {
            GWT.log(msg, null);
        } else {
            jsDebug(msg);
        }
	}
	
	private native void jsDebug(String message) /*-{
	    if ($wnd.console) {
	        $wnd.console.log(message);
	    } else if ($wnd.runtime) {
	        $wnd.air.trace(message);
	    } else if ($wnd.opera) {
	        $wnd.opera.postError(message);
	    }
	}-*/;
}
