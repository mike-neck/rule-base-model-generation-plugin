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

import org.mikeneck.gradle.plugin.model.PropertyEntry;
import org.mikeneck.gradle.plugin.model.Type;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.mikeneck.gradle.plugin.util.NameRule.PROPERTY_NAME_PATTERN;
import static org.mikeneck.gradle.plugin.util.NameRule.TYPE_NAME_PATTERN;
import static org.mikeneck.gradle.plugin.util.NameRule.thenTrue;

public final class PropertyEntryUtil {

    private PropertyEntryUtil() {}

    public static Collection<String> collectImports(Collection<PropertyEntry> entries) {
        return entries.stream()
                .map(PropertyEntry::getType)
                .filter(Type::requireImport)
                .map(Type::getImportType)
                .map(imp -> "import " + imp + ";")
                .collect(Collectors.toSet());
    }

    public static boolean fieldsAllWellDefined(Collection<PropertyEntry> entries) {
        return entries.size() == entries
                .stream()
                .filter(PropertyEntryUtil::wellDefined)
                .count();
    }

    public static boolean wellDefined(PropertyEntry entry) {
        return Optional.ofNullable(entry)
                .filter(NAME_NOT_NULL)
                .filter(NAME_NOT_EMPTY)
                .filter(NAME_MATCHES_RULE)
                .filter(TYPE_NOT_NULL)
                .filter(TYPE_NOT_REFER_TO_ANOTHER
                        .or(REF_TYPE_WELL_DEFINED))
                .map(thenTrue())
                .orElse(false);
    }

    private static final Predicate<PropertyEntry> NAME_NOT_NULL = e -> e.getName() != null;

    private static final Predicate<PropertyEntry> NAME_NOT_EMPTY = e -> !e.getName().isEmpty();

    private static final Predicate<PropertyEntry> NAME_MATCHES_RULE = e -> PROPERTY_NAME_PATTERN.matching(e.getName());

    private static final Predicate<PropertyEntry> TYPE_NOT_NULL = e -> e.getType() != null;

    private static final Predicate<PropertyEntry> TYPE_NOT_REFER_TO_ANOTHER = e -> !e.getType().requireGenericType();

    private static final Predicate<PropertyEntry> REF_TYPE_NOT_NULL = e -> e.getRefType() != null;

    private static final Predicate<PropertyEntry> REF_TYPE_NOT_EMPTY = e -> !e.getRefType().isEmpty();

    private static final Predicate<PropertyEntry> REF_TYPE_NOT_PROHIBITED = e -> !Type.valuesAsString().contains(e.getRefType());

    private static final Predicate<PropertyEntry> REF_TYPE_MATCHES_RULE = e -> TYPE_NAME_PATTERN.matching(e.getRefType());

    private static final Predicate<PropertyEntry> REF_TYPE_WELL_DEFINED =
            REF_TYPE_NOT_NULL
                    .and(REF_TYPE_NOT_EMPTY)
                    .and(REF_TYPE_NOT_PROHIBITED)
                    .and(REF_TYPE_MATCHES_RULE);
}
