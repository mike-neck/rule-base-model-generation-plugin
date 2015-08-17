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

import groovy.transform.ToString
import org.mikeneck.gradle.plugin.model.PropertyEntry
import org.mikeneck.gradle.plugin.model.Type

@ToString(includeNames = true)
class FieldObject {

    final PropertyEntry entry

    final Set<String> definedTypes

    FieldObject(PropertyEntry entry, Set<String> definedTypes) {
        this.entry = entry
        this.definedTypes = definedTypes
    }

    boolean isUnableToGenerateAccessors() {
        (!entry.type.canUseAsType && !definedTypes.contains(entry.refType)) ||
                (entry.type.isCollection && !definedTypes.contains(entry.refType))
    }

    String exceptionMessage(String type) {
        """|There is illegal definition.
           |The type of a field [${entry.name}] in type [${type}] is defined as a type [${entry.refType}], but it is not defined.
           |Available types are :
           |${definedTypes.collect { "  * ${it}" }.join('\n')}
           |""".stripMargin()
    }

    String getTypeDeclaration() {
        !entry.type.canUseAsType ?
                entry.refType :
                entry.type.isCollection ?
                        "${entry.type.asString}<${entry.refType}>" :
                        entry.type.asString
    }

    boolean isEnumField() {
        entry.type == Type.ENUM
    }

    String getCamelName() {
        "${entry.name.substring(0, 1).toUpperCase()}${entry.name.substring(1)}"
    }

    String getGetter() {
        "${this.typeDeclaration} get${this.camelName}();"
    }

    String getPublicGetter() {
        """|    @Override
           |    public ${this.typeDeclaration} get${this.camelName}() {
           |        return ${entry.name};
           |    }
           |""".stripMargin()
    }

    boolean isSetterRequired() {
        entry.type.requireSetter()
    }

    String getSetter() {
        "void set${this.camelName}(${this.typeDeclaration} ${entry.name});"
    }

    String getPublicSetter() {
        def override = isSetterRequired() ? "    @Override\n" : ''
        """|${override}    public void set${this.camelName}(${this.typeDeclaration} ${entry.name}) {
           |        this.${entry.name} = ${entry.name};
           |    }
           |""".stripMargin()
    }

    String getField() {
        """|    private ${typeDeclaration} ${entry.name};
           |""".stripMargin()
    }
}
