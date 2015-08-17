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

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.mikeneck.gradle.plugin.test.support.TemporaryProject
import spock.lang.Specification

import java.nio.file.Files

class PluginSpec extends Specification {

    @Rule
    TemporaryProject project = TemporaryProject.testTargetClass(ModelGeneration)

    def setup() {
        project.ready()
    }

    def cleanup() {
        project.end()
    }

    def 'When model is invalid, build will fail.' () {
        given:
        def script = """|apply plugin: 'org.mikeneck.rule-based-model-generation'
                |
                |model {
                |    metaModel {
                |        srcDir = 'src/main/java'
                |        packageName = 'gradle.meta.model'
                |        classes.create {
                |            name = 'Person'
                |            type = INTERFACE
                |            fields.create {
                |                name = 'name'
                |                type = STRING
                |            }
                |            fields.create {
                |                name = 'favorites'
                |                type = ENUM
                |                refType = 'Fav'
                |            }
                |        }
                |        classes.create {
                |            name = 'Favorite'
                |            type = ENUM
                |            values.create {
                |                value = 'GAME'
                |            }
                |            values.create {
                |                value = 'READING_BOOK'
                |            }
                |        }
                |    }
                |}
                |""".stripMargin()
        project.buildGradle(script)

        when:
        def result = GradleRunner.create()
                .withProjectDir(project.root)
                .withArguments(ModelGeneration.TASK_NAME)
                .buildAndFail()

        then:
        result.standardError.contains('FAILURE')
        result.standardError.contains($/\
         |  Available types are :
         |    * Person
         |    * Favorite/$.stripMargin())
    }

    def 'When model is valid, build will success'() {
        given:
        def script = """|apply plugin: 'org.mikeneck.rule-based-model-generation'
                |
                |model {
                |    metaModel {
                |        srcDir = 'src/main/java'
                |        packageName = 'gradle.meta.model'
                |        classes.create {
                |            name = 'Person'
                |            type = INTERFACE
                |            fields.create {
                |                name = 'name'
                |                type = STRING
                |            }
                |            fields.create {
                |                name = 'favorites'
                |                type = ENUM
                |                refType = 'Favorite'
                |            }
                |        }
                |        classes.create {
                |            name = 'Favorite'
                |            type = ENUM
                |            values.create {
                |                value = 'GAME'
                |            }
                |            values.create {
                |                value = 'READING_BOOK'
                |            }
                |        }
                |    }
                |}
                |""".stripMargin()
        project.buildGradle(script)

        when:
        def result = GradleRunner.create()
                .withProjectDir(project.root)
                .withArguments(ModelGeneration.TASK_NAME)
                .build()

        then:
        result.standardOutput.contains('BUILD SUCCESS')
        result.task(":${ModelGeneration.TASK_NAME}").outcome == TaskOutcome.SUCCESS

        then:
        def person = project.root.toPath().resolve("src/main/java/gradle/meta/model/Person.java")
        def favorite = project.root.toPath().resolve('src/main/java/gradle/meta/model/Favorite.java')

        expect:
        Files.exists(person)
        Files.exists(favorite)
    }
}
