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

import org.mikeneck.gradle.plugin.model.ClassType
import org.mikeneck.gradle.plugin.model.ManagedModel

class ModelDefinition {

    final File projectDir

    final ManagedModel model

    ModelDefinition(File projectDir, ManagedModel model) {
        this.projectDir = projectDir
        this.model = model
    }

    String getDestDir() {
        "${projectDir}/${model.srcDir}"
    }

    String getSourceDir() {
        "${this.destDir}/${model.packageName.replace('.', '/')}"
    }

    String getPackageName() {
        model.packageName
    }

    Set<String> getDefinedTypes() {
        model.classes.collect {
            it.name
        } as Set
    }

    Map<String, List<String>> getEnumValues() {
        model.classes.findAll {
            it.type == ClassType.ENUM
        }.collectEntries {
            def type = it.name
            def values = it.values.collect {
                it.value
            }.collect {
                "${type} ${it} = ${type}.${it};"
            }
            [type, values]
        }
    }

    void eachClasses(Closure closure) {
        model.classes.each {
            def obj = new ClassObject(it, this)
            closure.call(obj)
        }
    }
}
