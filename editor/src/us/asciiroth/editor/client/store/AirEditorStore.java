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
package us.asciiroth.editor.client.store;

import com.google.gwt.user.client.Command;

public class AirEditorStore implements EditorStore {
    
    public native boolean fileExists(String path) /*-{
        var file = new $wnd.air.File(path);
        return (file.exists);
    }-*/;
    
    public native void selectFile(FilePathCallback callback) /*-{
        var jsFilter = new $wnd.air.FileFilter("JavaScript", "*.js");
        var file = new $wnd.air.File();
        file.addEventListener($wnd.air.Event.SELECT, dirSelected);
        file.browseForOpen("Select a map", [jsFilter]);
        function dirSelected(event) {
            callback.@us.asciiroth.editor.client.store.FilePathCallback::execute(Ljava/lang/String;)(file.nativePath);
        }        
    }-*/;
    
    public native void selectDirectory(FilePathCallback callback) /*-{
        var file = $wnd.air.File.documentsDirectory;
        file.addEventListener($wnd.air.Event.SELECT, dirSelected);
        file.browseForDirectory("Select the root directory of the map URLs");
        function dirSelected(event) {
            callback.@us.asciiroth.editor.client.store.FilePathCallback::execute(Ljava/lang/String;)(file.nativePath);
        }        
    }-*/;
    public native void loadBoard(String path, LoadCallback callback) /*-{
        var file = new $wnd.air.File(path);
        if (file.exists) {
            try {
                var stream = new $wnd.air.FileStream();
                stream.open(file, $wnd.air.FileMode.READ);
                var json = stream.readUTFBytes(stream.bytesAvailable);
                stream.close();
                if (callback != null) {
                    callback.@us.asciiroth.editor.client.store.LoadCallback::execute(Ljava/lang/String;Ljava/lang/String;)(file.nativePath, json);
                }
            } catch(e) {
                // There is an exception throw, nothing is reported, and nothing seems wrong.
                // $wnd.alert("Error loading map: " + e.message + " " + e.details);
            }
        } else {
            $wnd.alert("Map doesn't exist."); 
        }
    }-*/;
    public native void saveBoard(String filePath, String json, Command callback) /*-{
        var file = new $wnd.air.File(filePath);
        var stream = new $wnd.air.FileStream();
        stream.open(file, $wnd.air.FileMode.WRITE);
        stream.writeUTFBytes(json);
        stream.close();
        if (callback != null) {
            callback.@com.google.gwt.user.client.Command::execute()();
        }
    }-*/;
    public native void saveBoardAs(String json, FilePathCallback callback) /*-{
        var file = new $wnd.air.File();
        file.addEventListener($wnd.air.Event.SELECT, dirSelected);
        file.browseForSave("Save map as");
        function dirSelected(event) {
            var stream = new $wnd.air.FileStream();
            stream.open(event.target, $wnd.air.FileMode.WRITE);
            stream.writeUTFBytes(json);
            stream.close();
            if (callback != null) {
                callback.@us.asciiroth.editor.client.store.FilePathCallback::execute(Ljava/lang/String;)(event.target.nativePath);
            }
        }        
    }-*/;
}
