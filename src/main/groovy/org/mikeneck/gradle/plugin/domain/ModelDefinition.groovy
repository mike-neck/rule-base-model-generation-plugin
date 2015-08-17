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

    /**
     * Collect all type names.
     * @return - {@link Set} of type names.
     */
    Set<String> getDefinedTypes() {
        model.classes.collect {
            it.name
        } as Set
    }

    /**
     * Collect {@code enum} type classes and its value entries.
     * Return value is {@link Map} whose key is type name and value is its entry values.
     * @return {@link Map} - key is type name, value is its entry values.
     */
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

    /**
     * Apply {@link Closure} operation to all {@link ClassObject} belongs to the model.
     * @param closure - operation of {@link ClassObject}.
     */
    void eachClasses(Closure closure) {
        model.classes.each {
            def obj = new ClassObject(it, this)
            closure.call(obj)
        }
    }

    /**
     * Filter classes. {@link ClassObject} will be applied to given {@link Closure}.
     * @param closure - should take {@link ClassObject} and should return {@code boolean}.
     * @return - {@code Collection} of {@link ClassObject} which matches given {@link Closure} condition.
     */
    Collection<ClassObject> findClasses(Closure<Boolean> closure) {
        def list = []
        model.classes.each {
            def obj = new ClassObject(it, this)
            if (closure.call(obj)) {
                list << obj
            }
        }
        return list
    }
}
