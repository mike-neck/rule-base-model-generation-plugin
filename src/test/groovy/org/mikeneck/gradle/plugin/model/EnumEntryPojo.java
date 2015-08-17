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

public class EnumEntryPojo implements EnumEntry {

    private String value;

    public EnumEntryPojo() {}

    public EnumEntryPojo(EnumEntry ee) {
        this.value = ee.getValue();
    }

    public EnumEntryPojo(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "EnumEntryPojo:[", "]")
                .add("value: [" + (value == null ? "null" : value) + "]")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnumEntry)) return false;
        EnumEntry that = (EnumEntry) o;
        return Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
