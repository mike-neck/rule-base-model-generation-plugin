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

import org.mikeneck.gradle.plugin.model.ManagedModel;

import java.util.Optional;
import java.util.function.Predicate;

import static org.mikeneck.gradle.plugin.util.ClassEntryUtil.classesAllWellDefined;
import static org.mikeneck.gradle.plugin.util.NameRule.thenTrue;
import static org.mikeneck.gradle.plugin.util.NameRule.PACKAGE_NAME_PATTERN;

public final class ManagedModelUtil {

    private ManagedModelUtil() {}

    public static boolean wellDefined(ManagedModel model) {
        return Optional.ofNullable(model)
                .filter(SRC_DIR_NOT_NULL)
                .filter(SRC_DIR_NOT_EMPTY)
                .filter(PACKAGE_NAME_NOT_NULL)
                .filter(PACKAGE_NAME_NOT_EMPTY)
                .filter(PACKAGE_NAME_MATCHES_RULE)
                .filter(CLASSES_NOT_EMPTY)
                .filter(CLASSES_WELL_DEFINED)
                .map(thenTrue())
                .orElse(false);
    }

    private static final Predicate<ManagedModel> SRC_DIR_NOT_NULL = m -> m.getSrcDir() != null;

    private static final Predicate<ManagedModel> SRC_DIR_NOT_EMPTY = m -> m.getSrcDir().isEmpty() == false;

    private static final Predicate<ManagedModel> PACKAGE_NAME_NOT_NULL = m -> m.getPackageName() != null;

    private static final Predicate<ManagedModel> PACKAGE_NAME_NOT_EMPTY = m -> m.getPackageName().isEmpty() == false;

    private static final Predicate<ManagedModel> PACKAGE_NAME_MATCHES_RULE = m -> PACKAGE_NAME_PATTERN.matching(m.getPackageName());

    private static final Predicate<ManagedModel> CLASSES_NOT_EMPTY = m -> m.getClasses().size() > 0;

    private static final Predicate<ManagedModel> CLASSES_WELL_DEFINED = m -> classesAllWellDefined(m.getClasses());
}
