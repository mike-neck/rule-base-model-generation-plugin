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

import org.gradle.model.ModelSet;

import java.util.Objects;
import java.util.StringJoiner;

public class ClassEntryPojo implements ClassEntry {

    private String name;

    private ClassType type;

    private ModelSet<PropertyEntry> fields;

    private ModelSet<EnumEntry> values;

    public ClassEntryPojo() {
        this.fields = new ModelSetImpl<>();
        this.values = new ModelSetImpl<>();
    }

    public ClassEntryPojo(ClassEntry ce) {
        this.name = ce.getName();
        this.type = ce.getType();
        this.fields = ce.getFields();
        this.values = ce.getValues();
    }

    public ClassEntryPojo(String name, ClassType type, ModelSet<PropertyEntry> fields, ModelSet<EnumEntry> values) {
        this.name = name;
        this.type = type;
        this.fields = fields;
        this.values = values;
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
    public ClassType getType() {
        return type;
    }

    @Override
    public void setType(ClassType type) {
        this.type = type;
    }

    @Override
    public ModelSet<PropertyEntry> getFields() {
        return fields;
    }

    public void setFields(ModelSet<PropertyEntry> fields) {
        this.fields = fields;
    }

    @Override
    public ModelSet<EnumEntry> getValues() {
        return values;
    }

    public void setValues(ModelSet<EnumEntry> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "ClassEntryPojo:[", "]")
                .add("name: [" + (name == null ? "null" : name) + "]")
                .add("type: [" + (type == null ? "null" : type) + "]")
                .add("fields: [" + (fields == null ? "null" : fields) + "]")
                .add("values: [" + (values == null ? "null" : values) + "]")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassEntry)) return false;
        ClassEntry that = (ClassEntry) o;
        return Objects.equals(name, that.getName()) &&
                Objects.equals(type, that.getType()) &&
                Objects.equals(fields, that.getFields()) &&
                Objects.equals(values, that.getValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, fields, values);
    }
}
