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

import us.asciiroth.client.board.Cell;

import com.google.gwt.user.client.Element;

public interface MouseCellListener {
    public void onMouseDown(Element td, Cell cell, boolean shifted);
    public void onMouseOver(Element td, Cell cell, boolean shifted);
    public void onMouseUp(Element td, Cell cell, boolean shifted);
    public void onMouseOut(Element td, Cell cell, boolean shifted);
    public void onContextMenu(Element td, Cell cell, boolean shifted);
}
