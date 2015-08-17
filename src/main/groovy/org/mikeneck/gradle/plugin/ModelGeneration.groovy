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

import org.gradle.api.Task
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.mikeneck.gradle.plugin.model.ManagedModel

class ModelGeneration extends RuleSource {

    public static final String GROUP_NAME = 'Meta Model Generation'

    @Model
    public static void metaModel(ManagedModel model) {
        model.srcDir = 'src/main/java'
    }

    @Mutate
    public static void createTask(ModelMap<Task> tasks, ManagedModel model) {
        Tasks.values().each {
            it.createTask(tasks, model)
        }
    }
}
