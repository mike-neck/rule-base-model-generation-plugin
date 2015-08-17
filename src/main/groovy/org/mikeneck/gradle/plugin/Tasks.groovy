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
import org.gradle.model.ModelMap
import org.mikeneck.gradle.plugin.domain.ClassObject
import org.mikeneck.gradle.plugin.domain.ModelDefinition
import org.mikeneck.gradle.plugin.model.ManagedModel

import static java.nio.file.Files.createDirectories
import static java.nio.file.Files.exists
import static org.mikeneck.gradle.plugin.util.ManagedModelUtil.wellDefined

enum Tasks implements TaskCreator {

    GENERATE_INTERFACE('generateModels'){
        @Override
        void createTask(ModelMap<Task> tasks, ManagedModel model) {
            tasks.create(getTaskName()) {
                Project pj = project
                group = 'Meta Model Generation'
                description = 'generates Managed Interface'
                doLast {
                    // validate model
                    if(!wellDefined(model)) {
                        throw new IllegalArgumentException("ManagedModel is not well defined.")
                    }

                    // configure variable
                    def modelDef = new ModelDefinition(pj.projectDir, model)

                    // create directory if not present
                    if (!exists(pj.file(modelDef.sourceDir).toPath())) {
                        createDirectories(pj.file(modelDef.sourceDir).toPath())
                    }
                    // write files
                    modelDef.eachClasses {ClassObject cls ->
                        pj.file(cls.javaFilePath).write(cls.interfaceFileContents, 'UTF-8')
                    }
                }
            }
        }
    },
    GENERATE_POJO('generatePojos'){
        @Override
        void createTask(ModelMap<Task> tasks, ManagedModel model) {

        }
    }

    private String taskName

    Tasks(String taskName) {
        this.taskName = taskName
    }

    String getTaskName() {
        taskName
    }

    @Override
    abstract void createTask(ModelMap<Task> tasks, ManagedModel model)
}
