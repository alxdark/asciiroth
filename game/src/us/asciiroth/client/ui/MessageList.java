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

import java.util.ArrayList;

public class MessageList extends ArrayList<String> {
	private static final long serialVersionUID = 1L;
	
	public String toBreakList() {
        StringBuilder sb = new StringBuilder();
        for (int i=0, len = size(); i < len; i++) {
            sb.append(get(i));
            if (i < (len-1)) {
                sb.append("<br/>");
            }
        }
        return sb.toString();
    }
    public String toBreakListWithEnterPrompt() {
        add("<small>ESC/ENTER to continue&#8230;</small>");
        return toBreakList(); 
    }
}
