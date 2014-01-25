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
package us.asciiroth.editor.client;

import com.google.gwt.core.client.EntryPoint;

/** Entry point for the application. */
public class Launch implements EntryPoint {

    public void onModuleLoad() {
        // The things you see in Opera when this is enabled, are truly frightening
        // GWT.setUncaughtExceptionHandler(Util.UNCAUGHT_EXCEPTION_HANDLER);
        
        Editor.get().init();
        Editor.get().newBoard();
    }

}
