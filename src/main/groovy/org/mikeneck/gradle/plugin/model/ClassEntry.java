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
import org.gradle.model.ModelSet;

@Managed
public interface ClassEntry {

    String getName();
    void setName(String name);

    ClassType getType();
    void setType(ClassType type);

    ClassType ENUM = ClassType.ENUM;
    ClassType INTERFACE = ClassType.INTERFACE;

    ModelSet<PropertyEntry> getFields();

    ModelSet<EnumEntry> getValues();
}
