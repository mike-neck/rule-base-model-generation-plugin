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

import org.mikeneck.gradle.plugin.model.ClassEntry;
import org.mikeneck.gradle.plugin.model.ClassType;
import org.mikeneck.gradle.plugin.model.Type;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import static org.mikeneck.gradle.plugin.util.EnumEntryUtil.enumAllWellDefined;
import static org.mikeneck.gradle.plugin.util.NameRule.thenTrue;
import static org.mikeneck.gradle.plugin.util.NameRule.TYPE_NAME_PATTERN;
import static org.mikeneck.gradle.plugin.util.PropertyEntryUtil.fieldsAllWellDefined;

public final class ClassEntryUtil {

    private ClassEntryUtil() {}

    public static boolean classesAllWellDefined(Collection<ClassEntry> entries) {
        return entries.stream()
                .filter(ClassEntryUtil::wellDefined)
                .count() == entries.size();
    }

    public static boolean wellDefined(ClassEntry entry) {
        return Optional.ofNullable(entry)
                .filter(NAME_NOT_NULL)
                .filter(NAME_NOT_EMPTY)
                .filter(NAME_MATCHES_RULE)
                .filter(NAME_NOT_PROHIBITED)
                .filter(TYPE_NOT_NULL)
                .filter(ENUM_TYPE.or(INTERFACE_TYPE))
                .map(thenTrue())
                .orElse(false);
    }

    private static final Predicate<ClassEntry> NAME_NOT_NULL = e -> e.getName() != null;

    private static final Predicate<ClassEntry> NAME_NOT_EMPTY = e -> !e.getName().isEmpty();

    private static final Predicate<ClassEntry> NAME_MATCHES_RULE = e -> e.getName().matches(TYPE_NAME_PATTERN.getPattern());

    private static final Predicate<ClassEntry> NAME_NOT_PROHIBITED = e -> !Type.prohibitedTypeNames().contains(e.getName());

    private static final Predicate<ClassEntry> TYPE_NOT_NULL = e -> e.getType() != null;

    private static final Predicate<ClassEntry> TYPE_IS_ENUM = e -> ClassType.ENUM.equals(e.getType());

    private static final Predicate<ClassEntry> HAS_VALUES = e -> e.getValues().size() > 0;

    private static final Predicate<ClassEntry> HAS_NO_FIELD = e -> e.getFields().size() == 0;

    private static final Predicate<ClassEntry> VALUES_WELL_DEFINED = e -> enumAllWellDefined(e.getValues());

    private static final Predicate<ClassEntry> ENUM_TYPE = TYPE_IS_ENUM.and(HAS_VALUES).and(HAS_NO_FIELD).and(VALUES_WELL_DEFINED);

    private static final Predicate<ClassEntry> TYPE_IS_INTERFACE = e -> ClassType.INTERFACE.equals(e.getType());

    private static final Predicate<ClassEntry> HAS_NO_VALUES = e -> e.getValues().size() == 0;

    private static final Predicate<ClassEntry> HAS_FIELD = e -> e.getFields().size() > 0;

    private static final Predicate<ClassEntry> FIELDS_WELL_DEFINED = e -> fieldsAllWellDefined(e.getFields());

    private static final Predicate<ClassEntry> INTERFACE_TYPE = TYPE_IS_INTERFACE.and(HAS_NO_VALUES).and(HAS_FIELD).and(FIELDS_WELL_DEFINED);
}
