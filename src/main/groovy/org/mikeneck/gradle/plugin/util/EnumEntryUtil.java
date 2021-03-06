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

import org.mikeneck.gradle.plugin.model.EnumEntry;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import static org.mikeneck.gradle.plugin.util.NameRule.ENUM_VALUE_PATTERN;
import static org.mikeneck.gradle.plugin.util.NameRule.thenTrue;

public final class EnumEntryUtil {

    private EnumEntryUtil() {}

    public static boolean enumAllWellDefined(Collection<EnumEntry> entries) {
        return entries.size() == entries
                .stream()
                .filter(EnumEntryUtil::wellDefined)
                .count();
    }

    public static boolean wellDefined(EnumEntry entry) {
        return Optional.ofNullable(entry)
                .filter(VALUE_NOT_NULL)
                .filter(VALUE_NOT_EMPTY)
                .filter(VALUE_MATCHES_RULE)
                .map(thenTrue())
                .orElse(false);
    }

    private static final Predicate<EnumEntry> VALUE_NOT_NULL = e -> e.getValue() != null;

    private static final Predicate<EnumEntry> VALUE_NOT_EMPTY = e -> !e.getValue().isEmpty();

    private static final Predicate<EnumEntry> VALUE_MATCHES_RULE = e -> ENUM_VALUE_PATTERN.matching(e.getValue());
}
