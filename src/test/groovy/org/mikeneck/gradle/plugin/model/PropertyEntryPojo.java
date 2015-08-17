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

import java.util.Objects;
import java.util.StringJoiner;

public class PropertyEntryPojo implements PropertyEntry {

    private String name;

    private Type type;

    private String refType;

    public PropertyEntryPojo() {}

    public PropertyEntryPojo(PropertyEntry pe) {
        this.name = pe.getName();
        this.type = pe.getType();
        this.refType = pe.getRefType();
    }

    public PropertyEntryPojo(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public PropertyEntryPojo(String name, Type type, String refType) {
        this.name = name;
        this.type = type;
        this.refType = refType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String getRefType() {
        return refType;
    }

    @Override
    public void setRefType(String refType) {
        this.refType = refType;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "PropertyEntryPojo:[", "]")
                .add("name: [" + (name == null ? "null" : name) + "]")
                .add("type: [" + (type == null ? "null" : type) + "]")
                .add("refType: [" + (refType == null ? "null" : refType) + "]")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PropertyEntry)) return false;
        PropertyEntry that = (PropertyEntry) o;
        return Objects.equals(name, that.getName()) &&
                Objects.equals(type, that.getType()) &&
                Objects.equals(refType, that.getRefType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, refType);
    }
}
