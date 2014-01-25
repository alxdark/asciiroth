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

/**
 * The problem with this API is that it is going to cut off file selection 
 * entirely with technologies like AIR.
 * @author adark
 *
 */
public interface EditorStore {
    public boolean fileExists(String path);
    public void selectFile(FilePathCallback callback);
    public void selectDirectory(FilePathCallback callback);
    public void loadBoard(String path, LoadCallback callback);
    public void saveBoard(String path, String json, Command callback);
    public void saveBoardAs(String json, FilePathCallback callback);
}
