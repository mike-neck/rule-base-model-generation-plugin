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
package org.mikeneck.gradle.plugin.util;

import java.util.function.Function;

public enum NameRule {

    PROPERTY_NAME_PATTERN("^[\\p{Lower}]+[\\p{Alnum}]*$"),
    TYPE_NAME_PATTERN("^[\\p{Upper}][\\p{Alnum}]*$"),
    ENUM_VALUE_PATTERN("^[\\p{Upper}][_A-Z0-9]*$"),
    PACKAGE_NAME_PATTERN("^[\\p{Lower}][a-z0-9]*(.[\\p{Lower}][a-z0-9]*)*$");

    private final String pattern;

    NameRule(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean matching(String object) {
        return object.matches(pattern);
    }

    public static <T> Function<T, Boolean> thenTrue() {
        return anything -> true;
    }
}
