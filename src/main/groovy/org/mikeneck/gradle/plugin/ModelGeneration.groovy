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
package org.mikeneck.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.mikeneck.gradle.plugin.model.ClassType
import org.mikeneck.gradle.plugin.model.ManagedModel
import org.mikeneck.gradle.plugin.model.Type
import org.mikeneck.gradle.plugin.util.PropertyEntryUtil

import static java.nio.file.Files.createDirectories
import static java.nio.file.Files.exists
import static org.mikeneck.gradle.plugin.util.ManagedModelUtil.wellDefined

class ModelGeneration extends RuleSource {

    public static final String TASK_NAME = 'generateModels'

    @Model
    public static void metaModel(ManagedModel model) {
        model.srcDir = 'src/main/java'
    }

    @Mutate
    public static void createTask(ModelMap<Task> tasks, ManagedModel model) {
        tasks.create('generateModels') {
            Project pj = project
            group = 'Meta Model Generation'
            description = 'generates Managed Interface'
            doLast {
                if(!wellDefined(model)) {
                    throw new IllegalArgumentException("ManagedModel is not well defined.")
                }
                def destDir = pj.file("${pj.projectDir}/${model.srcDir}")
                def dir = pj.file("${destDir}/${model.packageName.replace('.', '/')}")
                if (!exists(dir.toPath())) {
                    createDirectories(dir.toPath())
                }
                def definedTypes = model.classes.collect {
                    it.name
                }
                Map<String, List<String>> enumValues = model.classes.findAll {
                    it.type == ClassType.ENUM
                }.collectEntries {
                    def typeName = it.name
                    def values = it.values.collect {
                        it.value
                    }.collect {
                        "${typeName} ${it} = ${typeName}.${it};"
                    }
                    [typeName, values]
                }
                model.classes.each {klass ->
                    boolean enumType = klass.type == ClassType.ENUM
                    def imports = enumType ?
                            Collections.emptySet() :
                            PropertyEntryUtil.collectImports(klass.fields)
                    def entries = enumType ?
                            klass.values.collect{it.value} :
                            klass.fields.collect{
                                if (!it.type.canUseAsType && !definedTypes.contains(it.refType)) {
                                    throw new IllegalArgumentException($/|There is illegal definition.
                                            |The type of a field [${it.name}] in type [${klass.name}] is defined as a type [${it.refType}], but it is not defined.
                                            |Available types are :
                                            |${definedTypes.collect{"  * ${it}"}.join('\n')}
                                            |/$.stripMargin())
                                }
                                def type = !it.type.canUseAsType ?
                                        it.refType :
                                        it.type.isCollection ?
                                                "${it.type.asString}<${it.refType}>" :
                                                it.type.asString
                                def camel = "${it.name.substring(0,1).toUpperCase()}${it.name.substring(1)}"
                                def getter = "${type} get${camel}();"
                                List<String> ev = it.type.equals(Type.ENUM) ?
                                        enumValues[it.refType] :
                                        []
                                it.type.requireSetter() ?
                                        [getter, "void set${camel}(${type} ${it.name});", '\n', ev, '\n'].flatten():
                                        [getter, '\n']
                            }.flatten()
                    def contents = $/\
                            |package ${model.packageName};
                            |
                            |${enumType ? '' : 'import org.gradle.model.Managed;'}
                            |${imports.join('\n')}
                            |
                            |${enumType ? '' : '@Managed'}
                            |public ${klass.type.identifier} ${klass.name} {
                            |
                            |${entries.collect{"    ${it}"}.join(enumType ? ',\n' : '\n')}
                            |}
                            |/$.stripMargin()
                    pj.file("${dir}/${klass.name}.java").write(contents, 'UTF-8')
                }
            }
        }
    }
}
