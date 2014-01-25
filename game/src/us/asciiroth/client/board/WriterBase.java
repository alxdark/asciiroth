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
package us.asciiroth.client.board;

import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Base class for serializers.
 *
 */
public class WriterBase {
    /**
     * Add a boolean value as a property of the supplied JSON object. 
     * @param b
     * @param name
     * @param value
     */
    protected void addBoolean(JSONObject b, String name, boolean value) {
        // Could suppress if false
        b.put(name, JSONBoolean.getInstance(value));
    }
    /**
     * Add a String value as a property of the supplied JSON object. 
     * @param b
     * @param name
     * @param value
     */
    protected void addString(JSONObject b, String name, String value) {
        if (value != null && value.length() > 0) {
            b.put(name, new JSONString(value));
        }
    }
    /**
     * Add a Number value as a property of the supplied JSON object. 
     * @param b
     * @param name
     * @param value
     */
    protected void addNumber(JSONObject b, String name, int value) {
        b.put(name, new JSONNumber(value));
    }
    /**
     * Add an Array value as a property of the supplied JSON object, converting
     * from the supplied List. 
     * @param b
     * @param name      
     * @param values    
     */
    protected void addArray(JSONObject b, String name, List<String> values) {
        JSONArray array = new JSONArray();
        for (int i=0, len = values.size(); i < len; i++) {
            array.set(i, new JSONString((String)values.get(i)));    
        }
        b.put(name, array);
    }
}
