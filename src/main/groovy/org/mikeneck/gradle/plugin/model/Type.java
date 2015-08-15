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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public enum Type {

    STRING("String", false, true, ""),
    INTEGER("Integer", false, true, ""),
    LONG("Long", false, true, ""),
    DOUBLE("Double", false, true, ""),
    BOOLEAN("Boolean", false, true, ""),
    BIG_INTEGER("BigInteger", false, true, "java.math.BigInteger"),
    BIG_DECIMAL("BigDecimal", false, true, "java.math.BigDecimal"),
    CHARACTER("Character", false, true, ""),
    FILE("File", false, true, "java.io.File"),
    MODEL_SET("ModelSet", true, false, "org.gradle.model.ModelSet"),
    MODEL_MAP("ModelMap", true, false, "org.gradle.model.ModelMap"),
    ENUM("", true, true, ""),
    OTHER("", true, false, "");

    private final String asString;

    private final boolean generic;

    private final boolean setter;

    private final String importType;

    Type(String asString, boolean generic,
         boolean setter, String importType) {
        this.asString = asString;
        this.generic = generic;
        this.setter = setter;
        this.importType = importType;
    }

    public String getAsString() {
        return asString;
    }

    public boolean getCanUseAsType() {
        return asString.isEmpty() == false;
    }

    public boolean requireGenericType() {
        return generic;
    }

    public boolean getIsCollection() {
        return generic && asString.isEmpty() == false;
    }

    public boolean requireSetter() {
        return setter;
    }

    public boolean requireImport() {
        return importType.isEmpty() == false;
    }

    public String getImportType() {
        return importType;
    }

    private static final Map<String, Set<String>> CACHE = new LinkedHashMap<>();

    private static Stream<Type> basicTypes() {
        return Stream.of(values())
                .filter(e -> !ENUM.equals(e))
                .filter(e -> !OTHER.equals(e));
    }

    public static Set<String> valuesAsString() {
        return CACHE.computeIfAbsent("valuesAsString",
                k -> basicTypes()
                        .map(Type::getAsString)
                        .collect(toSet()));
    }

    public static Set<String> prohibitedTypeNames() {
        return CACHE.computeIfAbsent("prohibitedTypeNames",
                k -> Stream
                        .concat(basicTypes().map(Type::getAsString),
                                javaLangType())
                        .collect(toSet()));
    }

    public static Stream<String> javaLangType() {
        return Stream.of(Class.class, Object.class, Byte.class,
                Void.class, Exception.class, Enum.class, Error.class,
                Appendable.class, AutoCloseable.class, Cloneable.class,
                Comparable.class, Deprecated.class, FunctionalInterface.class,
                Iterable.class, Number.class, Override.class,
                Package.class, Readable.class, Runnable.class,
                SafeVarargs.class, Short.class, System.class,
                Thread.class)
                .map(Class::getSimpleName);
    }
}
