/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.gradle.plugin.model;

import org.gradle.model.Managed;

@Managed
public interface PropertyEntry {

    String getName();
    void setName(String name);

    Type getType();
    void setType(Type type);

    Type STRING = Type.STRING;
    Type INTEGER = Type.INTEGER;
    Type LONG = Type.LONG;
    Type DOUBLE = Type.DOUBLE;
    Type BOOLEAN = Type.BOOLEAN;
    Type BIG_INTEGER = Type.BIG_INTEGER;
    Type BIG_DECIMAL = Type.BIG_DECIMAL;
    Type CHARACTER = Type.CHARACTER;
    Type FILE = Type.FILE;
    Type MODEL_SET = Type.MODEL_SET;
    Type MODEL_MAP = Type.MODEL_MAP;
    Type ENUM = Type.ENUM;
    Type OTHER = Type.OTHER;

    String getRefType();
    void setRefType(String refType);
}
