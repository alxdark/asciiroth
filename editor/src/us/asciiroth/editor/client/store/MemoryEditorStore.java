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

public class MemoryEditorStore implements EditorStore {
    public boolean fileExists(String path) {
        return false;
    }
    public void loadBoard(String path, LoadCallback callback) {
        callback.execute(path, "");
    }
    public void saveBoard(String path, String json, Command callback) {
        callback.execute();
    }
    public void saveBoardAs(String json, FilePathCallback callback) {
        callback.execute("");
    }
    public void selectDirectory(FilePathCallback callback) {
        callback.execute("");
    }
    public void selectFile(FilePathCallback callback) {
        callback.execute("");
    }
}
