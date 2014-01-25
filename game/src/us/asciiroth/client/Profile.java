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
package us.asciiroth.client;

/**
 * This class uses the same browser detection as GWT in 1.4 so it 
 * should give the same answer.  
 */
public class Profile {
    
    private boolean useExtendedFonts;

    public Profile() {
    }
    public native boolean isAirProvided() /*-{
	    return (typeof $wnd.runtime != "undefined");
	}-*/;    
    public native boolean isGearsProvided() /*-{
	    return (typeof $wnd.google != "undefined" && typeof $wnd.google.gears != "undefined");
	}-*/;
    public native boolean isHTML5Provided() /*-{
	    return (typeof $wnd.openDatabase != "undefined");
	}-*/;
    public boolean isPersistenceProvided() {
        return (isAirProvided() || isGearsProvided() || isHTML5Provided());
    }
    public native String getTestScenarioURL() /*-{
	    if ($doc.location.search) {
	        var s = unescape($doc.location.search.substring(1));
	        for (var i=s.length-1; i >= 0; i--) {
	          if (s.charAt(i) == "\\" || s.charAt(i) == "/") {
	            return s.substring(0, i+1);
	          }
	        }
	    }
	    return null;
	}-*/;
	public native String getTestBoardID() /*-{
	    if ($doc.location.search) {
	        var s = unescape($doc.location.search.substring(1));
	        for (var i=s.length-1; i >= 0; i--) {
	          if (s.charAt(i) == "\\" || s.charAt(i) == "/") {
	            return s.substring(i+1).split(".js")[0];
	          }
	        }
	    }
	    return null;
	}-*/;
    
    // As of GWT 1.5 m2, you can get at this with Window.Location...
    public boolean useExtendedFonts() {
        return useExtendedFonts;
    }
    public void setExtendedFonts(boolean useExtendedFonts) {
        this.useExtendedFonts = useExtendedFonts;
    }
}

