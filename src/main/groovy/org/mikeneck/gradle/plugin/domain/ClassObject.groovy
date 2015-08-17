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
package org.mikeneck.gradle.plugin.domain

import org.mikeneck.gradle.plugin.model.ClassEntry
import org.mikeneck.gradle.plugin.model.ClassType
import org.mikeneck.gradle.plugin.util.PropertyEntryUtil

/**
 * This class represents {@code Class}.
 */
class ClassObject {

    final ClassEntry klass

    final ModelDefinition model

    final Set<String> enumValueUsed = [] as Set

    ClassObject(ClassEntry klass, ModelDefinition model) {
        this.klass = klass
        this.model = model
    }

    /**
     * Returns this whether this class is enum or not.
     * @return {@code true} - when this class is enum. {@code false} - when this class is not enum.
     */
    boolean isEnumType() {
        klass.type == ClassType.ENUM
    }

    /**
     * Returns Java file name.
     * @return Java file name.
     */
    String getJavaName() {
        "${klass.name}.java"
    }

    /**
     * Returns Java file path.
     * @return Java file path.
     */
    String getJavaFilePath() {
        "${model.sourceDir}/${this.javaName}"
    }

    /**
     * Returns imports.
     * @return import lines.
     */
    Set<String> getImports() {
        isEnumType() ?
                Collections.emptySet():
                PropertyEntryUtil.collectImports(klass.fields)
    }

    /**
     * Returns method declarations or enum values.
     * @return Method declarations - when this class is interface. Enum values - if this class is enum.
     */
    List<String> getEntries() {
        isEnumType() ?
                klass.values.collect { it.value } + [''] :
                klass.fields.collect {
                    new FieldObject(it, model.definedTypes)
                }.collect {
                    if (it.unableToGenerateAccessors) {
                        throw new IllegalArgumentException(it.exceptionMessage(klass.name))
                    }
                    if (it.enumField && !enumValueUsed.contains(it.typeDeclaration)) {
                        enumValueUsed << it.typeDeclaration
                        return model.enumValues[it.typeDeclaration] + [it.getter, it.setter, '']
                    } else if (it.enumField || it.setterRequired) {
                        return [it.getter, it.setter, '']
                    } else {
                        return [it.getter, '']
                    }
                }.flatten()
    }

    /**
     * Returns Java interface file contents
     * @return interface file contents
     */
    String getInterfaceFileContents() {
        """|package ${model.packageName};
           |
           |${this.imports.join('\n')}${this.enumType ? '' : '\nimport org.gradle.model.Managed;'}
           |${this.enumType ? '' : '\n@Managed'}
           |public ${klass.type.identifier} ${klass.name} {
           |
           |${this.entries.collect {it.size() == 0 ? '' : "    ${it}"}.join(this.enumType ? ',\n' : '\n')}
           |}""".stripMargin()
    }
}
