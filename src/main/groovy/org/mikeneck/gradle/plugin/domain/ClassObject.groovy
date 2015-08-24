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
import org.mikeneck.gradle.plugin.templates.InterfaceTemplate
import org.mikeneck.gradle.plugin.templates.PojoTemplate
import org.mikeneck.gradle.plugin.util.PropertyEntryUtil

/**
 * This class represents {@code Class}.
 */
class ClassObject {

    final ClassEntry klass

    final ModelDefinition model

    final Set<String> enumValueUsed = [] as Set

    final Collection<FieldObject> fieldObjects

    ClassObject(ClassEntry klass, ModelDefinition model) {
        this.klass = klass
        this.model = model
        this.fieldObjects = klass.type == ClassType.ENUM ?
                Collections.emptySet():
                klass.fields.collect {
                    new FieldObject(it, model.definedTypes)
                }
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
     * Returns Pojo file name.
     * @return Pojo file name.
     */
    String getPojoName() {
        "${klass.name}Pojo.java"
    }

    /**
     * Returns Java file path.
     * @return Java file path.
     */
    String getJavaFilePath() {
        "${model.sourceDir}/${this.javaName}"
    }

    /**
     * Returns Pojo file path.
     * @return Pojo file path.
     */
    String getPojoFilePath() {
        "${model.sourceDir}/${this.pojoName}"
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

    String getDefaultConstructor() {
        """\
           |    public ${klass.name}Pojo() {
           |    }
           |""".stripMargin()
    }

    String getConstructorWithInterface() {
        """\
           |    public ${klass.name}Pojo(${klass.name} model) {
           |${fieldObjects.collect {"        ${it.thisField} = model.${it.getterMethod}();"}.join('\n')}
           |    }
           |""".stripMargin()
    }

    String getConstructorWithParameters() {
        """\
           |    public ${klass.name}Pojo(${fieldObjects.collect {it.typeAndName}.join(', ')}) {
           |${fieldObjects.collect {it.settingToThisField}.join('\n')}
           |    }
           |""".stripMargin()
    }

    /**
     * Returns method declarations or enum values.
     * @return Method declarations - when this class is interface. Enum values - if this class is enum.
     */
    List<String> getEntries() {
        isEnumType() ?
                klass.values.collect { it.value } + [''] :
                fieldObjects.collect {
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
     * Returns getter/setter implementations.
     * @return getter/setter implementations.
     */
    List<String> getAccessorImpl() {
        isEnumType() ?
                Collections.emptyList() :
                fieldObjects.collect {
                    if (it.unableToGenerateAccessors) {
                        throw new IllegalArgumentException(it.exceptionMessage(klass.name))
                    }
                    [it.publicGetter, it.publicSetter]
                }.flatten()
    }

    List<String> getPrivateFields() {
        isEnumType() ?
                Collections.emptyList():
                fieldObjects.collect {
                    it.field
                }
    }

    /**
     * Returns Java interface file contents
     * @return interface file contents
     */
    String getInterfaceFileContents() {
        def template = new InterfaceTemplate(
                pkg: model.packageName,
                imps: this.imports,
                isEnum: this.enumType,
                name: klass.name,
                entries: this.entries)
        template.contents
    }

    /**
     * Returns POJO class file contents which implements interface generated by this plugin.
     * @return POJO class file contents
     */
    String getPojoFileContents() {
        def template = new PojoTemplate(
                pkg: model.packageName,
                imps: this.imports,
                name: klass.name,
                constructors: [
                        this.defaultConstructor,
                        this.constructorWithInterface,
                        this.constructorWithParameters],
                fields: this.privateFields,
                accessor: this.accessorImpl)
        template.contents
    }
}
